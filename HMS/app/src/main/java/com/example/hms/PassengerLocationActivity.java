package com.example.hms;

import androidx.appcompat.app.AppCompatActivity;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
public class PassengerLocationActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;
    ParseObject requestAmbulance = new ParseObject("RequestAmbulance");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_location);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                Toast.makeText(PassengerLocationActivity.this,"An Ambulance request had been made ! ",Toast.LENGTH_SHORT).show();
                                requestAmbulance.put("username", ParseUser.getCurrentUser().getUsername());
                                ParseGeoPoint userLocation = new ParseGeoPoint(location.getLatitude(),location.getLongitude());
                                requestAmbulance.put("passengerLocation",userLocation);
                                requestAmbulance.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null){
                                            Toast.makeText(PassengerLocationActivity.this,"An Ambulance is on the way ! ",Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                            Toast.makeText(PassengerLocationActivity.this,"Error occurred - could not save!",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else
                                Toast.makeText(PassengerLocationActivity.this,"Error occurred!Please try again",Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        catch (SecurityException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ParseUser.logOut();
    }
}
