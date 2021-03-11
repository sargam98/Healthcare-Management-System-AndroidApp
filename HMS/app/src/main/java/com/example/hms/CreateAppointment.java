package com.example.hms;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CreateAppointment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_appointment);

        Spinner hospitaldropdown = findViewById(R.id.spinner3);
        String[] hospitals = new String[]{"Select hospital","Goa Medical College", "Galaxy Hospital,Duler","Healthway Hospitals","Dr. Dhulapkar's Hospital"};
        ArrayAdapter<String> adapterHospital = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, hospitals);
        hospitaldropdown.setAdapter(adapterHospital);

        Spinner specializationdropdown = findViewById(R.id.spinner4);
        String[] specialization = new String[]{"Cardiologist","Anesthesiologist", "Dermatologist","Gatroenterologist","Gynecologist","Nephrologist","Neurologist","Oncologist"};
        ArrayAdapter<String> adapterSpecialization = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, specialization);
        specializationdropdown.setAdapter(adapterSpecialization);
    }

}
