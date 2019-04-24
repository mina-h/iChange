package com.example.ichange;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;

public class FinalImage extends AppCompatActivity {


    private ImageView finalImage;
    private TextView finalImageName;
    private TextView finalImageExchange;
    private TextView dealerName;
    private TextView dealerNumber;
    private TextView dealerEmail;
    private FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_image);


        finalImage = findViewById(R.id.final_image);
//        dealerNumber = findViewById(R.id.dealer_number);
//        dealerEmail = findViewById(R.id.dealer_email);


        db = FirebaseFirestore.getInstance();


        Intent intent = getIntent();
        String imageName = intent.getStringExtra(MapActivity.EXTRA_MESSAGE);
        finalImageName = findViewById(R.id.final_image_name);
        finalImageName.setText(imageName);

        Intent intent1 = getIntent();
        String exchangeWith = intent1.getStringExtra(MapActivity.EXTRA_MESSAGE1);
        finalImageExchange = findViewById(R.id.final_image_exchange_with);
        finalImageExchange.setText(exchangeWith);


        Intent intent2 = getIntent();
        String path = intent2.getStringExtra(MapActivity.EXTRA_MESSAGE2);
        Picasso.with(this).load(path)
                .into(finalImage);


//        Bitmap bitmap = BitmapFactory.decodeFile((InputStream)new URL(path).getContent());
//        finalImage.setImageBitmap(bitmap);


        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("users").document(uid)
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




//                               Log.d("Doneee", document.getId() + " => " + document.getData());
                        } else {
                            Log.w("Erorrrr", "Error getting documents.", task.getException());
                        }
                    }
                });








    }
}
