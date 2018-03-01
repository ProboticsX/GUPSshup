package com.example.john.faltu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Random r=new Random();
    Handler h=new Handler();
    Double latitude=0.0,longitude=0.0;
    private DatabaseReference mDatabase;
    String user = "shubham";
    String receiver = "shubham";
    ArrayList<String> intentresult3=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        intentresult3=getIntent().getStringArrayListExtra("user_and_receiver");
        user=intentresult3.get(0);
        receiver=intentresult3.get(1);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final Double[] cblat = {latitude};
        final Double[] cblng = {longitude};
        final Double[] prevcblat = {cblat[0]};
        final Double[] prevcblng = {cblng[0]};

        final LatLng[] sydney = {new LatLng(cblat[0], cblng[0])};
        mMap.addMarker(new MarkerOptions().position(sydney[0]).title(receiver+"'s location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney[0]));



        mDatabase.child("locations").child("users").child(user).child("messages").child("received").child(receiver).child("latestlocation").child("latitude").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                latitude = dataSnapshot.getValue(Double.class);
                cblat[0]=latitude;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.child("locations").child("users").child(user).child("messages").child("received").child(receiver).child("latestlocation").child("longitude").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                longitude = dataSnapshot.getValue(Double.class);
                cblng[0]=longitude;

                Log.d("maossss", "onMapReady: hiiiiiii");

                final LatLng[][] sydney = {{new LatLng(cblat[0], cblng[0])}};
                mMap.addMarker(new MarkerOptions().position(sydney[0][0]).title(receiver+"'s location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney[0][0]));


                PolygonOptions cbline = new PolygonOptions()
                        .strokeColor(Color.RED)
                        .fillColor(Color.argb(100,100,100,100))
                        .add(new LatLng(prevcblat[0], prevcblng[0]))
                        .add(new LatLng(cblat[0],cblng[0]));

                if (prevcblat[0]!=0 && prevcblng[0]!=0)
                    mMap.addPolygon(cbline);

                prevcblat[0] =cblat[0];
                prevcblng[0] =cblng[0];






            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}