package com.example.hms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import com.parse.ParseInstallation;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (isFirstRun) {
            //show start activity

            startActivity(new Intent(MainActivity.this, Register.class));
            Toast.makeText(MainActivity.this, "Healthcare Managament System !", Toast.LENGTH_LONG).show();
            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun", false).apply();
            ParseInstallation.getCurrentInstallation().saveInBackground();
        }
        configureBloodBankButton();
        configureAppointmentButton();
        configurePrescriptionButton();
        configureRecordsButton();
        configueAmbulanceButton();
    }
    private void configureBloodBankButton(){
        Button nextScreen = (Button) findViewById(R.id.button4);
        nextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CheckBank.class));
            }
        });
    }
    private void configureAppointmentButton(){
        Button nextScreen = (Button) findViewById(R.id.button2);
        nextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CreateAppointment.class));
            }
        });
    }
    private void configurePrescriptionButton(){
        Button nextScreen = (Button) findViewById(R.id.button3);
        nextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Prescription.class));
            }
        });
    }
    private void configureRecordsButton(){
        Button nextScreen = (Button) findViewById(R.id.button);
        nextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MedicalHistory.class));
            }
        });
    }
    private void configueAmbulanceButton() {
        Button nextScreen = (Button) findViewById(R.id.button5);
        nextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = "UserDataHMS.txt";
                StringBuilder sb = new StringBuilder();
                try {
                    BufferedReader input = new BufferedReader(new InputStreamReader(openFileInput(filename)));
                    String dataFromFile;
                    while ((dataFromFile = input.readLine()) != null) {
                        sb.append(dataFromFile).append("\n");
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this, CheckUser.class));
            }
        });
    }
}
