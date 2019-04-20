package com.example.ichange;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class CameraActivity extends AppCompatActivity {

    private FusedLocationProviderClient locationProviderClient;
    private static final int REQUEST_LOCATION = 1;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button mButtonChooseImage;
    private Button mButtonUpload;
    // private TextView mTextViewShowUploads;
    private EditText mEditTextFileName;
    private EditText mEditTextXchange;
 //   private Timestamp timestamp;
    private GeoPoint geoPoint;
    private ImageView mImageView;
    private ProgressBar mProgressbar;

    private Uri mImageUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private FirebaseFirestore db;
    private StorageTask mUploadTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {

            locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            locationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        double lat = location.getLatitude();
                        double lng = location.getLongitude();
                    }
                }
            });
        }

        locationRequest =createLocationRequest();
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult){
                for(Location location : locationResult.getLocations())
                   // Log.d("LocationTest", "lat: " + location.getLatitude() + " , lng: " + location.getLongitude());

                 geoPoint = new GeoPoint(location.getLatitude() , location.getLongitude());
            }
        };

        // startLocationUpdate();

        mButtonChooseImage = findViewById(R.id.button_choose_image);
        mButtonUpload = findViewById(R.id.button_upload);
        //   mTextViewShowUploads = findViewById(R.id.text_view_show_uploads);
        mEditTextFileName = findViewById(R.id.edit_text_file_name);
        mEditTextXchange = findViewById(R.id.edit_text_exchange_with);
        mImageView = findViewById(R.id.image_view);
        mProgressbar = findViewById(R.id.progress_bar);


        mStorageRef = FirebaseStorage.getInstance().getReference("Uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        db = FirebaseFirestore.getInstance();


        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(CameraActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });

//        mTextViewShowUploads.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.getMenu().findItem(R.id.nav_camera).setChecked(true);
//        geoPoint = new GeoPoint(13 , -34);

    }

    @Override
    protected void onResume(){
          super.onResume();

          startLocationUpdate();

    }

    @Override
    protected void onPause(){
         super.onPause();

         startLocationUpdate();

    }


    private void startLocationUpdate(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
        PackageManager.PERMISSION_GRANTED){
            locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    private void stopLocationUpdates(){
        locationProviderClient.removeLocationUpdates(locationCallback);
    }

    LocationRequest createLocationRequest(){
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(7000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_LOCATION){
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                     startLocationUpdate();
            } else {
                
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.with(this).load(mImageUri).into(mImageView);

        }
    }

    private String getFileExtention(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {

        Log.d("!!!", "uploadFile: !!");


        if (mImageUri != null) {
            Log.d("!!!", "uploadFile: 1");
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtention(mImageUri));

            UploadTask task = fileReference.putFile(mImageUri);


            task
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressbar.setProgress((int) progress);
                        }
                    })
                    .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                Log.d("!!!", "uploadFile: 2");
                                throw task.getException();
                            }

                            Log.d("!!!", "uploadFile: 3");
                            // Continue with the task to get the download URL
                            return fileReference.getDownloadUrl();
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            Log.d("!!!", "uploadFile: complete");

                            if (task.isSuccessful()) {
                                Log.d("!!!", "uploadFile: success");

                                Uri downloadUri = task.getResult();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgressbar.setProgress(0);
                                    }
                                }, 500);

                                Toast.makeText(CameraActivity.this, "Upload successful", Toast.LENGTH_LONG).show();

                                //mStorageRef.child(System.currentTimeMillis()
                                //      + "." + getFileExtention(mImageUri)).getDownloadUrl().toString(),

                                Upload upload = new Upload(mEditTextFileName.getText().toString().trim(),
                                        downloadUri.toString()
                                        , mEditTextXchange.getText().toString().trim(),
                                        geoPoint); //tasksnapshot ... mstorage

                                String uploadId = mDatabaseRef.push().getKey();

                                db.collection("uploads").add(upload);
                                //mDatabaseRef.child(uploadId).setValue(upload);

                                Intent intent = new Intent(CameraActivity.this, ListViewActivity.class);
                                        startActivity(intent);

                            } else {
                                Toast.makeText(CameraActivity.this, "Upload unsuccessful", Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CameraActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    // Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            //      selectedFragment = new HomeFragment();

                            Intent intent1 = new Intent(CameraActivity.this, MapActivity.class);
                            startActivity(intent1);
                            break;
                        case R.id.nav_chat:

                            Intent intent2 = new Intent(CameraActivity.this, ChatActivity.class);
                            startActivity(intent2);

                            //        selectedFragment = new ChatFragment();
//                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                                    selectedFragment).commit();
                            break;
                        case R.id.nav_camera:

//                            Intent intent3 = new Intent(MapActivity.this, CameraActivity.class);
//                            startActivity(intent3);
                            //         selectedFragment = new CameraFragment();
//                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                                    selectedFragment).commit();
                            break;
                        case R.id.nav_notification:

                            Intent intent4 = new Intent(CameraActivity.this, NotificationsActivity.class);
                            startActivity(intent4);
                            //         selectedFragment = new NotificationsFragment();
//                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                                    selectedFragment).commit();
                            break;
                        case R.id.nav_favorite:

                            Intent intent5 = new Intent(CameraActivity.this, FavoritesActivity.class);
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
