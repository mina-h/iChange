package com.example.ichange;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private FirebaseFirestore db;
    private List<Upload> mUploads;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.getMenu().findItem(R.id.nav_favorite).setChecked(true);

        mRecyclerView = findViewById(R.id.fav_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUploads = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        mAdapter = new ImageAdapter(FavoritesActivity.this, mUploads);
        mRecyclerView.setAdapter(mAdapter);

        CollectionReference uploadsRef =   db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("favorites");
        uploadsRef.orderBy("timeStamp", Query.Direction.ASCENDING);
        uploadsRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                String favoId = document.getString("id");
                                DocumentReference uploadRef = db.collection("uploads")
                                                                 .document(favoId);
                                uploadRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Upload upload = documentSnapshot.toObject(Upload.class);
                                        mUploads.add(upload);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener  navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            Intent intent1 = new Intent(FavoritesActivity.this, MapActivity.class);
                            startActivity(intent1);
                            break;

                        case R.id.nav_camera:

                            Intent intent3 = new Intent(FavoritesActivity.this, CameraActivity.class);
                            startActivity(intent3);
                            break;

                        case R.id.nav_favorite:
                            break;

                    }
                    return false;
                }
            };
}
