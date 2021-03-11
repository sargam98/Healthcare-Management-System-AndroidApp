package com.example.hms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.sql.Driver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DriverRequestListActivity extends AppCompatActivity {

    List<String> passengerList = new ArrayList<String>();
    List<ParseGeoPoint> passengerLocationList = new ArrayList<ParseGeoPoint>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_request_list);
        ListView lv = findViewById(R.id.listViewDriver);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(DriverRequestListActivity.this, android.R.layout.simple_list_item_1, passengerList);
        passengerList.add("List of Passengers!");
        lv.setAdapter(arrayAdapter);
        ParseQuery<ParseObject> ambulanceRequestQuery = ParseQuery.getQuery("RequestAmbulance");
        ambulanceRequestQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects.size()>0 && e==null){
                    for (ParseObject entry : objects){
                        try {
                            passengerList.add((String)entry.get("username"));
                            passengerLocationList.add((ParseGeoPoint)entry.get("passengerLocation"));
                        }
                        catch (NullPointerException t){ t.printStackTrace(); }
                    }
                }
                else { Toast.makeText(DriverRequestListActivity.this, "No Passengers currently", Toast.LENGTH_SHORT).show(); }
                arrayAdapter.notifyDataSetChanged();
            }
        });

        // Set an item click listener for ListView
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ParseGeoPoint location = (ParseGeoPoint) passengerLocationList.get(passengerList.indexOf(parent.getItemAtPosition(position)));
                Double lat = location.getLatitude();
                Double lng = location.getLongitude();
                Toast.makeText(DriverRequestListActivity.this,(lat)+" "+(lng),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(
									android.content.Intent.ACTION_VIEW,      
									Uri.parse("http://maps.google.com/maps?daddr="+Double.toString(lat)+","+Double.toString(lng))
								);
                startActivity(intent);
            }
        });
    }
    public void onBackPressed() {
        super.onBackPressed();
        ParseUser.logOut();
        //Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345"));
    }
}
