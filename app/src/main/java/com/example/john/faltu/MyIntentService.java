package com.example.john.faltu;

import android.os.Handler;

        import android.*;
        import android.app.IntentService;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.location.Location;
        import android.location.LocationListener;
        import android.location.LocationManager;
        import android.os.Bundle;
        import android.os.Handler;
        import android.support.annotation.Nullable;
        import android.support.v4.app.ActivityCompat;
        import android.util.Log;

        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by john on 2/5/2018.
 */

public class MyIntentService extends IntentService {
    public static final String TAG="INservice";
    Double latitude=0.0,longitude=0.0;
    private DatabaseReference mDatabase;
    String user = "shubham";
    String receiver = "shubham";
    Random r=new Random();
    Handler h=new Handler();
    ArrayList<String> intentresult5=new ArrayList<>();





    public MyIntentService() {



        super("MYSERVICE");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        /*
        intentresult5=getIntent().getStringArrayListExtra("user_and_receiver");
        user=intentresult5.get(0);
        receiver=intentresult5.get(1);
        send_latlong();
    */


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        send_latlong();

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent: ");
        intentresult5=intent.getStringArrayListExtra("user_and_receiver");
        user=intentresult5.get(0);
        receiver=intentresult5.get(1);
    }

    public void send_latlong() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        LocationListener locationListener = new LocationListener() {


            @Override
            public void onLocationChanged(Location location) {

                Log.d(TAG, "onLocationChanged: latitude = " + location.getLatitude());
                Log.d(TAG, "onLocationChanged: longitude = " + location.getLongitude());

                latitude = Double.valueOf(String.valueOf(location.getLatitude()));
                longitude = Double.valueOf(String.valueOf(location.getLongitude()));
                mDatabase = FirebaseDatabase.getInstance().getReference();

                mDatabase.child("locations").child("users").child(user).child("messages").child("sent").child(receiver).child("latestlocation").child("latitude").setValue(latitude);
                mDatabase.child("locations").child("users").child(user).child("messages").child("sent").child(receiver).child("latestlocation").child("longitude").setValue(longitude);

                mDatabase.child("locations").child("users").child(receiver).child("messages").child("received").child(user).child("latestlocation").child("latitude").setValue(latitude);
                mDatabase.child("locations").child("users").child(receiver).child("messages").child("received").child(user).child("latestlocation").child("longitude").setValue(longitude);


            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                100,
                0,
                locationListener
        );

    }

}

