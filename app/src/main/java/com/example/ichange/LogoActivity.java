package com.example.ichange;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class LogoActivity extends AppCompatActivity {
    private Timer t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);


        t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent i = new Intent(LogoActivity.this, StartActivity.class);
                startActivity(i);
            }
        }, 1000);
    }



}
