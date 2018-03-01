package com.example.john.faltu;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Random;

public class welcome_screen extends AppCompatActivity {

    Random r=new Random();
    Handler h=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        Runnable r=new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(welcome_screen.this,firstscreen.class);
                startActivity(intent);
                finish();

            }
        };

        h.postDelayed(r,3000);


    }
}
