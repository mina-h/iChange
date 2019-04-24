package com.example.ichange;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    public void onMapReady(GoogleMap googleMap) {


        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;


        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);

        }

    }

    public static final String EXTRA_MESSAGE = "com.example.myapplication2.MESSAGE";
    public static final String EXTRA_MESSAGE1 = "com.example.myapplication2.MESSAGE1";
    public static final String EXTRA_MESSAGE2 = "com.example.myapplication2.MESSAGE2";



    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Button mTextViewShowUploads;
    Button logoutBtn;
// vase sign in az to main avordam:
    private FirebaseAuth mAuth;

//    FirebaseAuth mAuth;
//    FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseFirestore db;
    private List<Upload> mUploads;




//    @Override
//    protected void onStart() {
//        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mAuth = FirebaseAuth.getInstance();
        onStart();



        getLocationPermission();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        mTextViewShowUploads = findViewById(R.id.text_view_show_uploads);
        mTextViewShowUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagesActivity();
            }
        });

         logoutBtn = findViewById(R.id.log_out);
//        mAuth = FirebaseAuth.getInstance();

//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                if (firebaseAuth.getCurrentUser() == null) {
//                    startActivity(new Intent(MapActivity.this, SigninActivity.class));
//                }
//            }
//        };
//
//        logoutBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mAuth.signOut();
//            }
//        });


        mUploads = new ArrayList<>();

        db = FirebaseFirestore.getInstance();



        CollectionReference uploadsRef = db.collection("uploads");
        Query query = uploadsRef.orderBy("timeStamp");

        //uploadsRef
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Upload upload = document.toObject(Upload.class);
                                upload.setId(document.getId());
                                mUploads.add(upload);


                            }
                            for (final Upload upload : mUploads) {
                                LatLng pos = new LatLng(upload.getmGeopoint().getLatitude(), upload.getmGeopoint().getLongitude());
                                MarkerOptions options = new MarkerOptions()
                                        .position(pos)
                                        .title(upload.getName());

                                Marker marker = mMap.addMarker(options);
                                marker.setTag(upload);
                                marker.setZIndex(-1);


                                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                    @Override
                                    public void onInfoWindowClick(Marker arg0) {
                                        // call an activity(xml file)
                                        Intent intent = new Intent(MapActivity.this, FinalImage.class);
                                        Upload upload = (Upload) arg0.getTag();
                                        intent.putExtra(EXTRA_MESSAGE, upload.getName());
                                        intent.putExtra(EXTRA_MESSAGE1, upload.getmXchange());

                                        startActivity(intent);
                                    }


                                });


                            }
                        }
                        else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }

                    }
                });




        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                sendToStart();
            }
        });    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionsGranted) {
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location");
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM,
                                    "My Location");

                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException" + e.getMessage());
        }
    }



    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null){
            sendToStart();
//            Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
//            startActivity(startIntent);
//            finish();
        }
// else {
//            Intent intent = new Intent(MapActivity.this,MapActivity.class);
//            startActivity(intent);
//        }
    }

    private void sendToStart(){
        Intent startIntent = new Intent(MapActivity.this, StartActivity.class);
        startActivity(startIntent);
        finish();
    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapActivity.this));


//        MarkerOptions options = new MarkerOptions()
//                .position(latLng)
//                .title(title);
//
////
//        Upload mUpload = null;
//
//        Marker marker = mMap.addMarker(options);
//
//        marker.setTag(mUpload);

    }

    private void initMap() {
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);


    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions");

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called. ");

        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    private void openImagesActivity() {
        Intent intent = new Intent(this, ListViewActivity.class);
        startActivity(intent);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    // Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            //      selectedFragment = new HomeFragment();

//                            Intent intent1 = new Intent(MainActivity.this, MapActivity.class);
//                            startActivity(intent1);
                            break;
                        case R.id.nav_chat:

                            Intent intent2 = new Intent(MapActivity.this, ChatActivity.class);
                            startActivity(intent2);

                            //        selectedFragment = new ChatFragment();
//                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                                    selectedFragment).commit();
                            break;
                        case R.id.nav_camera:

                            Intent intent3 = new Intent(MapActivity.this, CameraActivity.class);
                            startActivity(intent3);
                            //         selectedFragment = new CameraFragment();
//                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                                    selectedFragment).commit();
                            break;
                        case R.id.nav_notification:

                            Intent intent4 = new Intent(MapActivity.this, NotificationsActivity.class);
                            startActivity(intent4);
                            //         selectedFragment = new NotificationsFragment();
//                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                                    selectedFragment).commit();
                            break;
                        case R.id.nav_favorite:

                            Intent intent5 = new Intent(MapActivity.this, FavoritesActivity.class);
                            startActivity(intent5);
                            //         selectedFragment = new FavoritesFragment();
//                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                                    selectedFragment).commit();
                            break;

                    }

//                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        selectedFragment).commit();

                    return false;
                }
            };
}
