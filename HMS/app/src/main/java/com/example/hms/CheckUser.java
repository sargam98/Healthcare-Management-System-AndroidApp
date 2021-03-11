package com.example.hms;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;

import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CheckUser extends AppCompatActivity {

    RadioButton rb,rbt;
    RadioGroup rg;
    String userType = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_user);
        rg = findViewById(R.id.radioGroup2);
        if (ParseUser.getCurrentUser()!=null){
            ParseUser.logOut();
        }
        Button buttonApply = findViewById(R.id.applyUserBtn);
        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int option = rg.getCheckedRadioButtonId();
                rbt = findViewById(option);

                switch (rbt.getId()){
                    case R.id.Passenger :
                        //Toast.makeText(CheckUser.this,"Help will arrive shortly",Toast.LENGTH_SHORT).show();
                        userType = "Passenger";
                        break;
                    case R.id.Driver :
                        //Toast.makeText(CheckUser.this,"I live to assist!",Toast.LENGTH_SHORT).show();
                        userType = "Driver";
                        break;
                }
                if (ParseUser.getCurrentUser()==null){
                    ParseAnonymousUtils.logIn(new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (user!=null && e == null ){
                                //Toast.makeText(CheckUser.this, "We have an anonymous user", Toast.LENGTH_SHORT).show();
                                user.put("as", userType);
                                user.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (ParseUser.getCurrentUser()!=null){
                                            //Toast.makeText(CheckUser.this, "Yaaaaay", Toast.LENGTH_SHORT).show();
                                            if (ParseUser.getCurrentUser().get("as").equals("Passenger")){
                                                startActivity(new Intent (CheckUser.this,PassengerLocationActivity.class));

                                            }
                                            else if (ParseUser.getCurrentUser().get("as").equals("Driver")) {
                                                startActivity(new Intent (CheckUser.this,DriverRequestListActivity.class));
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
                else {
                    if (ParseUser.getCurrentUser().get("as").equals("Passenger")){
                        startActivity(new Intent (CheckUser.this,PassengerLocationActivity.class));
                    }
                    else if (ParseUser.getCurrentUser().get("as").equals("Driver")) {
                        startActivity(new Intent (CheckUser.this,DriverRequestListActivity.class));
                    }

                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ParseUser.logOut();
    }

    public void createAnonymous(View v){
        int radioButton = rg.getCheckedRadioButtonId();
        rb = findViewById((radioButton));
        //Toast.makeText(CheckUser.this,rb.getText(),Toast.LENGTH_SHORT).show();
    }
}
