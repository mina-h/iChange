package com.example.ichange;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FinalImage extends AppCompatActivity {


    private ImageView finalImage;
    private TextView finalImageName;
    private TextView finalImageExchange;
    private TextView dealerName;
    private TextView dealerNumber;
    private TextView dealerEmail;
    private FirebaseFirestore db;
    private CheckBox checkBox;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_image);


        finalImage = findViewById(R.id.final_image);
        db = FirebaseFirestore.getInstance();
        checkBox = findViewById(R.id.nav_checkbox);


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    Intent intent = getIntent();
                    String id = intent.getStringExtra((MapActivity.EXTRA_MESSAGE3));

                    Map<String, Object> favorites = new HashMap<>();
                    favorites.put("id", id);

                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    db.collection("users").document(uid).collection("favorites")
                    .add(favorites)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                }
            }
        });



        finalImageName = findViewById(R.id.final_image_name);
        finalImageExchange = findViewById(R.id.final_image_exchange_with);


        Intent intent = getIntent();
        String imageName = intent.getStringExtra(MapActivity.EXTRA_MESSAGE);
        finalImageName.setText(imageName);


        Intent intent1 = getIntent();
        String exchangeWith = intent1.getStringExtra(MapActivity.EXTRA_MESSAGE1);
        finalImageExchange.setText(exchangeWith);


        Intent intent2 = getIntent();
        String path = intent2.getStringExtra(MapActivity.EXTRA_MESSAGE2);
        Picasso.with(this).load(path)
                .into(finalImage);


        Intent intent3 = getIntent();
        String owner = intent3.getStringExtra(MapActivity.EXTRA_MESSAGE4);
        db.collection("users").document(owner)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            String dealer =  task.getResult().getString("Name");
                            dealerName = findViewById(R.id.dealer_name);
                            dealerName.setText(dealer);

                            String phone = task.getResult().getString("Phone");
                            dealerNumber = findViewById(R.id.dealer_number);
                            dealerNumber.setText(phone);

                            String email = task.getResult().getString("Email");
                            dealerEmail = findViewById(R.id.dealer_email);
                            dealerEmail.setText(email);

                        } else {
                            Log.w("Eror", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
