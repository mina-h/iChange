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

public class FinalImageFromListViewActivity extends AppCompatActivity {


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
        setContentView(R.layout.activity_final_image_list);


        finalImage = findViewById(R.id.final_image_list);

        db = FirebaseFirestore.getInstance();
        finalImageName = findViewById(R.id.final_image_name_list);
        finalImageExchange = findViewById(R.id.final_image_exchange_with_list);



        Intent intentList = getIntent();
        String imageNameList = intentList.getStringExtra(ImageAdapter.EXTRA_MESSAGE3);
        finalImageName.setText(imageNameList);

        Intent intentList1 = getIntent();
        String exchangeWithList = intentList1.getStringExtra(ImageAdapter.EXTRA_MESSAGE4);
        finalImageExchange.setText(exchangeWithList);

        Intent intent3 = getIntent();
        String path1 = intent3.getStringExtra(ImageAdapter.EXTRA_MESSAGE5);
        Picasso.with(this).load(path1)
                .into(finalImage);

        Intent intent4 = getIntent();
        String owner = intent4.getStringExtra(ImageAdapter.EXTRA_MESSAGE6);


        db.collection("users").document(owner)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()) {


                            String dealer =  task.getResult().getString("Name");
                            dealerName = findViewById(R.id.dealer_name_list);
                            dealerName.setText(dealer);

                            String phone = task.getResult().getString("Phone");
                            dealerNumber = findViewById(R.id.dealer_number_list);
                            dealerNumber.setText(phone);


                            String email = task.getResult().getString("Email");
                            dealerEmail = findViewById(R.id.dealer_email_list);
                            dealerEmail.setText(email);


                        } else {
                            Log.w("Error", "Error getting documents.", task.getException());
                        }
                    }
                });

    }
}
