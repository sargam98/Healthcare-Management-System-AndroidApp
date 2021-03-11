package com.example.hms;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class CheckBank extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_bank);

        Spinner dropdown = findViewById(R.id.spinner);

        String[] items = new String[]{"Select Blood group","A", "B", "AB","O"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        Spinner dropdown2 = findViewById(R.id.spinner2);
        String[] items2 = new String[]{"Rh Factor","+ve", "-ve"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items2);
        dropdown2.setAdapter(adapter2);



        Button submitQueryBtn = (Button) findViewById(R.id.button6);

        final List<String> queryReturnedList = new ArrayList<>();
        final ListView lv = findViewById(R.id.bloodBankList);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CheckBank.this, android.R.layout.simple_list_item_1, queryReturnedList);
        //queryReturnedList.add("List of Donors!");
        lv.setAdapter(arrayAdapter);

        submitQueryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Spinner blood = (Spinner) findViewById(R.id.spinner);
                            Spinner rh = (Spinner) findViewById((R.id.spinner2));
                            String bloodGroupType = blood.getSelectedItem().toString();
                            String rhFactor = rh.getSelectedItem().toString();
                            String queryType = bloodGroupType + " " + rhFactor;
                            URL weblink = new URL("http://192.168.1.100/Project/checkBlood.php");
                            HttpURLConnection connect = (HttpURLConnection) weblink.openConnection();
                            connect.setRequestMethod("POST");
                            connect.setDoOutput(true);
                            
                            Map<String, String> arguments = new HashMap<>();
                            arguments.put("type", queryType);
                            String str = "";
                            
                            for (Map.Entry<String, String> entry : arguments.entrySet()) {
                                if (str.equals("")) {
                                    str = URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8");
                                } else {
                                    str = str + "&" + URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8");
                                }
                            }
                            byte[] out = str.getBytes(StandardCharsets.UTF_8);
                            int length = out.length;
                            connect.setFixedLengthStreamingMode(length);
                            connect.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                            connect.connect();

                            try (OutputStream os = connect.getOutputStream()) {
                                os.write(out);
                            }

                            BufferedReader br = new BufferedReader(new InputStreamReader(connect.getInputStream()));
                            String line;
                            StringBuilder response = new StringBuilder();
                            while ((line = br.readLine()) != null) {
                                response.append(line);
                            }
                            br.close();
                            String[] list = response.toString().split(";");
                            int i=0;
                            if (list[0].equals("NONE")){
                                queryReturnedList.add("Not Available");
                                for (i=1;i<list.length;i++ ) {
                                    queryReturnedList.add(list[i]);
                                }
                            }
                            else {
                                queryReturnedList.add("Blood is Available !");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                t.start();
                try {
                    t.join();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                arrayAdapter.notifyDataSetChanged();
            }
        });

    }
}
