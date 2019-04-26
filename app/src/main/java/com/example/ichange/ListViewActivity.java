package com.example.ichange;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ListViewActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;

    private FirebaseFirestore db;
    private List<Upload> mUploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);


        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUploads = new ArrayList<>();

        db = FirebaseFirestore.getInstance();

        CollectionReference uploadsRef =   db.collection("uploads");
        Query query = uploadsRef.orderBy("timeStamp", Query.Direction.DESCENDING);

      //uploadsRef
              query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                            Upload upload = document.toObject(Upload.class);
                            mUploads.add(upload);
                            }
                            mAdapter = new ImageAdapter(ListViewActivity.this, mUploads);
                            mRecyclerView.setAdapter(mAdapter);

                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }

                        }
                    });


//        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
//        bottomNav.setOnNavigationItemSelectedListener(navListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    // Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            //      selectedFragment = new HomeFragment();

                            Intent intent1 = new Intent(ListViewActivity.this, MapActivity.class);
                            startActivity(intent1);
                            break;


//                        case R.id.nav_chat:
//
//                            Intent intent2 = new Intent(ListViewActivity.this, ChatActivity.class);
//                            startActivity(intent2);
//                            break;


                        case R.id.nav_camera:

                            Intent intent3 = new Intent(ListViewActivity.this, CameraActivity.class);
                            startActivity(intent3);
                            break;



//                        case R.id.nav_notification:
//
//                            Intent intent4 = new Intent(ListViewActivity.this, NotificationsActivity.class);
//                            startActivity(intent4);
//                            break;


                        case R.id.nav_favorite:

                            Intent intent5 = new Intent(ListViewActivity.this, FavoritesActivity.class);
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
