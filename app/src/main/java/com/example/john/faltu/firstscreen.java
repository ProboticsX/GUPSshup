package com.example.john.faltu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class firstscreen extends AppCompatActivity {

    Button bt_reg,bt_chat;
    EditText et_entry,et_second_entry,et_reg;
    private DatabaseReference mDatabase2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstscreen);


        bt_reg=findViewById(R.id.bt_reg);
        bt_chat=findViewById(R.id.bt_chat);
        et_entry=findViewById(R.id.et_entry);
        et_second_entry=findViewById(R.id.et_second_entry);
        et_reg=findViewById(R.id.et_reg);
        mDatabase2 = FirebaseDatabase.getInstance().getReference();


        bt_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> userUpdates = new HashMap<>();
                userUpdates.put(et_reg.getText().toString(),et_reg.getText().toString());
                mDatabase2.updateChildren(userUpdates);
                mDatabase2.child("users").child(et_reg.getText().toString()).child("messages").child("sent").setValue("");
                mDatabase2.child("users").child(et_reg.getText().toString()).child("messages").child("received").setValue("");
                Toast.makeText(firstscreen.this, "registered !!", Toast.LENGTH_SHORT).show();
            }
        });

        final int[] result = {0};
        final int[] result2 = {0};
        final ArrayList<String> intentresult=new ArrayList<>();


        bt_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {


                            if (postSnapshot.getValue().toString().equals(et_entry.getText().toString())){
                                result[0] =1;
                                Log.d("senderrrrrrrrrrrrrrrrr", "onDataChange: "+postSnapshot.getValue().toString());
                                intentresult.add(0,et_entry.getText().toString());

                                mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        for(DataSnapshot postSnapshot2:dataSnapshot.getChildren()){

                                            if(postSnapshot2.getValue().toString().equals(et_second_entry.getText().toString())){
                                                result2[0]=1;
                                                Log.d("receiverrrrrrrrrr", "onDataChange: "+postSnapshot2.getValue().toString());
                                                intentresult.add(1,et_second_entry.getText().toString());

                                                Intent intent=new Intent(firstscreen.this,MainActivity.class);
                                                intent.putExtra("user_and_receiver",intentresult);
                                                startActivity(intent);
                                            }

                                        }

                                        if (result2[0]==0){
                                            Toast.makeText(firstscreen.this, "the second person is not registered", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                            }
                        }

                        if (result[0]==0){
                            Toast.makeText(firstscreen.this, "user name is not registered !!", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}
