package com.example.hms;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicalHistory extends AppCompatActivity {

    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final List<String> medicalFiles =  new ArrayList<String>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_history);
        ListView lv = findViewById(R.id.listViewRecords);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MedicalHistory.this, android.R.layout.simple_list_item_1, medicalFiles);
        lv.setAdapter(arrayAdapter);
        Toast.makeText(MedicalHistory.this,"Please wait while records are being fetched",Toast.LENGTH_SHORT).show();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL weblink = new URL("http://192.168.1.100/Project/listPatientRecords.php");
                    HttpURLConnection connect = (HttpURLConnection) weblink.openConnection();
                    connect.setRequestMethod("POST");
                    connect.setDoOutput(true);

                    Map<String, String> arguments = new HashMap<>();
                    arguments.put("phoneNumber", "9890391901");
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
                    for (String y : response.toString().split(";")) {
                        medicalFiles.add(y);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        t.start();
        try {
            t.join();
        }
        catch (Exception e ){
            e.printStackTrace();
        }
        arrayAdapter.notifyDataSetChanged();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String fileToBeDownloaded = (String) parent.getItemAtPosition(position);
                new DownloadFileFromURL().execute(fileToBeDownloaded);
            }
        });
    }
    class DownloadFileFromURL extends AsyncTask<String,String,String> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MedicalHistory.this);
            pDialog.setMessage("Loading ...Please wait");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... f_url) {
            String user = "9890391901";
            int count;
            try {
                String root = Environment.getExternalStorageDirectory().toString();

                System.out.println("Downloading");
                URL url = new URL("http://192.168.1.100/Project/uploads/"+user+"/"+f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                int lenghtOfFile = conection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                OutputStream output = new FileOutputStream(root+"/Download/HMS/"+f_url[0]);
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
                System.out.println("Download Complete");
                File f = new File (root+"/Download/HMS/",f_url[0]);
                System.out.println(f.toString());
                SevenZFile sevenZFile = new SevenZFile(f);
                SevenZArchiveEntry entry;
                while ((entry = sevenZFile.getNextEntry()) != null){
                    if (entry.isDirectory()){
                        continue;
                    }
                    File curfile = new File(root+"/Download/HMS/", entry.getName());
                    File parent = curfile.getParentFile();
                    if (!parent.exists()) {
                       parent.mkdirs();
                    }
                    FileOutputStream out = new FileOutputStream(curfile);
                    byte[] content = new byte[(int) entry.getSize()];
                    sevenZFile.read(content, 0, content.length);
                    out.write(content);
                    out.flush();
                    out.close();
                }
                f.delete();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
        }
    }

}
