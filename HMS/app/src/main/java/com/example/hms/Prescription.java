package com.example.hms;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Prescription extends AppCompatActivity {

    List<String> prescriptionList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);
        ListView lv = findViewById(R.id.listViewPrescription);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Prescription.this, android.R.layout.simple_list_item_1, prescriptionList);
        prescriptionList.add("Prescription 2020-03-05");
        lv.setAdapter(arrayAdapter);
    }
}
