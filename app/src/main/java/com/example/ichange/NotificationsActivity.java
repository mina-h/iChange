package com.example.ichange;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class NotificationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.getMenu().findItem(R.id.nav_notification).setChecked(true);


    }

    private BottomNavigationView.OnNavigationItemSelectedListener  navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    // Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            //      selectedFragment = new HomeFragment();

                            Intent intent1 = new Intent(NotificationsActivity.this, MapActivity.class);
                            startActivity(intent1);
                            break;
                        case R.id.nav_chat:

                            Intent intent2 = new Intent(NotificationsActivity.this, ChatActivity.class);
                            startActivity(intent2);

                            //        selectedFragment = new ChatFragment();
//                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                                    selectedFragment).commit();
                            break;
                        case R.id.nav_camera:

                            Intent intent3 = new Intent(NotificationsActivity.this, CameraActivity.class);
                            startActivity(intent3);
                            //         selectedFragment = new CameraFragment();
//                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                                    selectedFragment).commit();
                            break;
                        case R.id.nav_notification:

//                            Intent intent4 = new Intent(FavoritesActivity.this, NotificationsActivity.class);
//                            startActivity(intent4);
                            //         selectedFragment = new NotificationsFragment();
//                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                                    selectedFragment).commit();
                            break;
                        case R.id.nav_favorite:

                            Intent intent5 = new Intent(NotificationsActivity.this, FavoritesActivity.class);
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
