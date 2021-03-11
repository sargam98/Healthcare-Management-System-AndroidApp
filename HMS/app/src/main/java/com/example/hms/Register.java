package com.example.hms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Spinner dropdownBloodType = findViewById(R.id.spinner5);
        String[] groups = new String[]{"Select Blood group","A", "B", "AB","O"};
        ArrayAdapter<String> adapterBB = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, groups);
        dropdownBloodType.setAdapter(adapterBB);

        Spinner dropdownRh = findViewById(R.id.spinner6);
        String[] Rhsign = new String[]{"Rh Factor","+ve", "-ve"};
        ArrayAdapter<String> adapterRH = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Rhsign);
        dropdownRh.setAdapter(adapterRH);

        Spinner dropdownG = findViewById(R.id.spinner7);
        String[] genders = new String[]{"Select Gender","Male", "Female","Other"};
        ArrayAdapter<String> adapterG = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genders);
        dropdownG.setAdapter(adapterG);
        configureSubmitButton();
    }
    private void configureSubmitButton(){
        Button nextScreen = (Button) findViewById(R.id.button8);
        nextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText userPhnoField = (EditText) findViewById(R.id.editText2);
                EditText userNameField = (EditText) findViewById(R.id.editText3);
                EditText userAddrField = (EditText) findViewById(R.id.editText4);
                EditText userAgeField  = (EditText) findViewById(R.id.editText5);
                Spinner bloodTypeDrop  = (Spinner)  findViewById((R.id.spinner5));
                Spinner rhFactorDrop   = (Spinner)  findViewById((R.id.spinner6));
                Spinner usrGenderDrop  = (Spinner)  findViewById((R.id.spinner7));
                
                String userPhone = userPhnoField.getText().toString();
                String userName  = userNameField.getText().toString();
                String userAddr  = userAddrField.getText().toString();
                String userAge   = userAgeField.getText().toString();
                String bloodType = bloodTypeDrop.getSelectedItem().toString();
                String rhFactor  = rhFactorDrop.getSelectedItem().toString();
                String usrGender = usrGenderDrop.getSelectedItem().toString();

                String UserData = "PhoneNumber : "+userPhone + "\n"
                                 +"UserName : "+userName + "\n"
                                 +"UserAddress : "+userAddr + "\n"
                                 +"UserAge : "+userAge + "\n"
                                 +"BloodType : "+bloodType + "\n"
                                 +"RhFactor : "+rhFactor + "\n"
                                 +"UserGender : "+usrGender;

                FileOutputStream fos;
                try {
                    fos = openFileOutput("UserDataHMS.txt", Context.MODE_PRIVATE);
                    fos.write(UserData.getBytes());
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(Register.this,"Data Saved Successfully :)", Toast.LENGTH_LONG).show();
                startActivity(new Intent(Register.this,MainActivity.class));
            }
        });
    }
}
