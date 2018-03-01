package com.example.john.faltu;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Random;

public class Main2Activity extends AppCompatActivity {

    Button btset,btview;
    EditText et_time;
    String user = "shubham";
    String receiver = "shubham";
    private DatabaseReference mDatabase;
    int time = 0;
    public static final String TAG = "MAAAPPPSSS";
    Double latitude=0.0,longitude=0.0;
    Random r=new Random();
    Handler h=new Handler();
    ArrayList<String> intentresult3=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        et_time = findViewById(R.id.et_time);
        btview = findViewById(R.id.btview);
        btset=findViewById(R.id.btset);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        intentresult3=getIntent().getStringArrayListExtra("user_and_receiver");
        user=intentresult3.get(0);
        receiver=intentresult3.get(1);

        int perm = ContextCompat.checkSelfPermission(Main2Activity.this, android.Manifest.permission.ACCESS_FINE_LOCATION);

        if (perm == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(Main2Activity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    44);
        }








        btset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                time = Integer.valueOf(String.valueOf(et_time.getText())) * 1000;

                mDatabase.child("locations").child("users").child(user).child("messages").child("sent").child(receiver).child("time").setValue(time);
                mDatabase.child("locations").child("users").child(receiver).child("messages").child("received").child(user).child("time").setValue(time);
                Toast.makeText(Main2Activity.this, "location shared for "+et_time.getText()+" seconds", Toast.LENGTH_SHORT).show();

                final Intent i = new Intent(Main2Activity.this,MyIntentService.class);
                i.putExtra("user_and_receiver",intentresult3);
                startService(i);



                Runnable r=new Runnable() {

                    @Override
                    public void run() {
                        Log.d(TAG, "run: service endssssssss");
                        stopService(i);
                    }
                };

                h.postDelayed(r,time);





            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(this, "GPS permission required", Toast.LENGTH_SHORT).show();
            }
        }
    }


    }

