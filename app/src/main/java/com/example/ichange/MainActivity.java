package com.example.ichange;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    //private FirebaseAuth mAuth;

    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private Timer t;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // FirebaseFirestore db = FirebaseFirestore.getInstance();


//        if(isServicesOk()){
//            init();
//        }
        isServicesOk();


//        mAuth = FirebaseAuth.getInstance();

//        onStart();


//        t = new Timer();
//        t.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                Intent i = new Intent(MainActivity.this, MapActivity.class);
//                startActivity(i);
//            }
//        }, 3000);

        Intent intent = new Intent(MainActivity.this,MapActivity.class);
        startActivity(intent);

//        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
//        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }


//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//        if (currentUser == null){
//            Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
//            startActivity(startIntent);
//            finish();
//        } else {
//            Intent intent = new Intent(MainActivity.this,MapActivity.class);
//            startActivity(intent);
//        }
//    }

//    private BottomNavigationView.OnNavigationItemSelectedListener  navListener =
//            new BottomNavigationView.OnNavigationItemSelectedListener() {
//                @Override
//                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                   // Fragment selectedFragment = null;
//
//                    switch (item.getItemId()) {
//                        case R.id.nav_home:
//                   //      selectedFragment = new HomeFragment();
//
//                           Intent intent1 = new Intent(MainActivity.this, MapActivity.class);
//                           startActivity(intent1);
//                            break;
//                        case R.id.nav_chat:
//
//                            Intent intent2 = new Intent(MainActivity.this, ChatActivity.class);
//                            startActivity(intent2);
//
//                   //        selectedFragment = new ChatFragment();
////                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
////                                    selectedFragment).commit();
//                            break;
//                        case R.id.nav_camera:
//
//                            Intent intent3 = new Intent(MainActivity.this, CameraActivity.class);
//                            startActivity(intent3);
//                  //         selectedFragment = new CameraFragment();
////                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
////                                    selectedFragment).commit();
//                            break;
//                        case R.id.nav_notification:
//
//                            Intent intent4 = new Intent(MainActivity.this, NotificationsActivity.class);
//                            startActivity(intent4);
//                  //         selectedFragment = new NotificationsFragment();
////                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
////                                    selectedFragment).commit();
//                            break;
//                        case R.id.nav_favorite:
//
//                            Intent intent5 = new Intent(MainActivity.this, FavoritesActivity.class);
//                            startActivity(intent5);
//                   //         selectedFragment = new FavoritesFragment();
////                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
////                                    selectedFragment).commit();
//                            break;
//
//                    }
//
////                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
////                        selectedFragment).commit();
//
//                    return false;
//                }
//            };

//    private void init(){
//        Button btnMap = findViewById(R.id.btnMap);
//        btnMap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, MapActivity.class);
//                startActivity(intent);
//            }
//        });
//
//    }
    public boolean isServicesOk(){
        Log.d(TAG, "isServicesOk: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map request
            Log.d(TAG, "isServiceOk: Google Play Services is working");
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)){
           //an error occured but we can resolve it
           Log.d(TAG, "isServicesOk: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else {
            Toast.makeText(this, "You cannot make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }



}
