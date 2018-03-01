package com.example.john.faltu;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button sendbtn,delbtn,btshare,btview;
    LinearLayout ll1,ll2,ll3;
    EditText et1;
    LinearLayout.LayoutParams layoutParams;
    private DatabaseReference mDatabase;
    ArrayList<String> intentresult2=new ArrayList<>();

    String user="shubham";
    String receiver="shubham";
    boolean btcolor=false;
    Random r=new Random();
    Handler h=new Handler();

    int sent_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btshare = findViewById(R.id.btshare);
        btview=findViewById(R.id.btview);
        sendbtn = findViewById(R.id.sendbtn);
        delbtn=findViewById(R.id.delbtn);
        et1 = findViewById(R.id.et1);
        ll1=findViewById(R.id.ll1);
        ll2=findViewById(R.id.ll2);
        ll3=findViewById(R.id.ll3);

        layoutParams = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        intentresult2=getIntent().getStringArrayListExtra("user_and_receiver");
        user=intentresult2.get(0);
        receiver=intentresult2.get(1);
        btview.setText(receiver+"'s location");
        btview.setBackgroundColor(Color.RED);
        final boolean[] res = {false};
        final Long[] value3 = {null};
        Log.d("SJJJJJJJ", "onCreate: "+user+"         "+receiver);



        mDatabase.child("users").child(user).child("messages").child("sent").child(receiver).addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Log.d("SJJJJJJJJJ", "onDataChange: "+postSnapshot.getKey());
                    TextView textView = new TextView(MainActivity.this);
                    textView.setText(postSnapshot.getKey().toString());
                    ll2.addView(textView,layoutParams);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.child("users").child(receiver).child("messages").child("sent").child(user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot2:dataSnapshot.getChildren()){
                    TextView textView = new TextView(MainActivity.this);
                    textView.setText(postSnapshot2.getKey().toString());
                    ll3.addView(textView,layoutParams);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mDatabase.child("locations").child("users").child(user).child("messages").child("received").child(receiver).child("time").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Long value2= dataSnapshot.getValue(Long.class);
                if (value2!=null) {
                    value3[0] =value2;
                    btview.setBackgroundColor(Color.GREEN);
                    btcolor=true;
                    Log.d("sjjjjjjjjjjjjjjjjjjj", user + " shared location for " + value2 / 1000 + " seconds" +btcolor);


                    Runnable r=new Runnable() {

                        @Override
                        public void run() {
                            mDatabase.child("locations").child("users").child(user).child("messages").child("sent").child(receiver).child("time").setValue(null);
                            mDatabase.child("locations").child("users").child(receiver).child("messages").child("received").child(user).child("time").setValue(null);

                            btcolor=false;
                            btview.setBackgroundColor(Color.RED);

                        }
                    };

                    h.postDelayed(r,value2);

                    res[0] =true;

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mDatabase.child("locations").child("users").child(user).child("messages").child("sent").child(receiver).child("time").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Long value2= dataSnapshot.getValue(Long.class);
                if (value2!=null) {
                    value3[0] =value2;
                    //btview.setBackgroundColor(Color.GREEN);
                    btcolor=true;
                    Log.d("sjjjjjjjjjjjjjjjjjjj", user + " shared location for " + value2 / 1000 + " seconds" +btcolor);



                    Runnable r=new Runnable() {

                        @Override
                        public void run() {
                            mDatabase.child("locations").child("users").child(user).child("messages").child("sent").child(receiver).child("time").setValue(null);
                            mDatabase.child("locations").child("users").child(receiver).child("messages").child("received").child(user).child("time").setValue(null);

                            btcolor=false;
                            btview.setBackgroundColor(Color.RED);

                        }
                    };

                    h.postDelayed(r,value2);

                    res[0] =true;

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




/*delbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        mDatabase.child("users").child(user).child("messages").child("sent").setValue("");
        mDatabase.child("users").child(user).child("messages").child("received").setValue("");


    }
});
*/

        btview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (btcolor){
                    Log.d("from main activity", "onClick:successssss ");
                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                    intent.putExtra("user_and_receiver",intentresult2);
                    startActivity(intent);

                }
            }
        });


        btshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                intent.putExtra("user_and_receiver",intentresult2);
                startActivity(intent);
            }
        });



        final String[] msg = {""};
sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                msg[0] = et1.getText().toString();

                TextView textView = new TextView(MainActivity.this);
                textView.setText(et1.getText().toString());
                ll2.addView(textView,layoutParams);


                mDatabase.child("users").child(user).child("messages").child("sent").child(receiver).child(et1.getText().toString()).setValue("");
                mDatabase.child("users").child(receiver).child("messages").child("received").child(user).child(et1.getText().toString()).setValue("");

                et1.setText("");
            }
        });

    }





    }
