package com.example.umaradkhamov.signup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class AppointmentList extends AppCompatActivity {

    TextView tv;
    private String TAG = BankSelect.class.getSimpleName();
    private ListView lv;
    private String password, username;
    private ListAdapter adapter;
    ArrayList<HashMap<String, String>> applicationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_content);

        applicationList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.lv_appointment);
       // tv = (TextView) findViewById(R.id.selectedAppointment);
        username = getIntent().getStringExtra("intent_username");
        password = getIntent().getStringExtra("intent_psw");
        new AppointmentList.GetAppointments().execute();
    }


    private class GetAppointments extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {

                URL url = new URL("http://" +  IPContainer.IP + "/jrlu/getAppointments.php"); // here is your URL path

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));

                writer.write("username=" + username);
                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    String answer = sb.toString();

                    Log.e(TAG, "Response from url: " + answer);

                    if (answer != null) {
                        try {
                            JSONObject jsonObj = new JSONObject(answer);

                            // Getting JSON Array node
                            JSONArray contacts = jsonObj.getJSONArray("manageRecord");

                            // looping through All Contacts
                            for (int i = 0; i < contacts.length(); i++) {
                                JSONObject c = contacts.getJSONObject(i);
                                String appointmentID = c.getString("appointmentID");
                                String bankName = c.getString("bankName");
                                String serviceName = c.getString("serviceName");
                                String appointment_date = c.getString("appointment_date");
                                String time_interval = c.getString("time_interval");

                                // tmp hash map for single contact
                                HashMap<String, String> record = new HashMap<>();

                                // adding each child node to HashMap key => value
                                record.put("appointmentID", appointmentID);
                                record.put("bankName", bankName);
                                record.put("serviceName",  serviceName);
                                record.put("appointment_date", appointment_date);
                                record.put("time_interval", time_interval);

                                // adding contact to contact list
                                applicationList.add(record);
                            }
                        } catch (final JSONException e) {
                            Log.e(TAG, "Json parsing error: " + e.getMessage());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),
                                            "Json parsing error: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            });

                        }

                    } else {
                        Log.e(TAG, "Couldn't get json from server.");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Couldn't get json from server. Check LogCat for possible errors!",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } else {
                    // return new String("false : "+responseCode);
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "Cannot connect to php file",
                        Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            adapter = new SimpleAdapter(AppointmentList.this, applicationList,
                    R.layout.appointment_select, new String[]{"bankName", "serviceName", "appointment_date", "time_interval"},
                    new int[]{R.id.bankName, R.id.serviceName, R.id.appointment_date, R.id.time_interval});
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {

                    HashMap<String,String> map =(HashMap<String,String>)lv.getItemAtPosition(position);
                    String appointmentID = map.get("appointmentID");
                    String bankName = map.get("bankName");
                    String serviceName = map.get("serviceName");
                    String appointment_date = map.get("appointment_date");
                    String time_interval = map.get("time_interval");
                    Intent intent = new Intent(AppointmentList.this, DetailsOfAppointment.class);
                    intent.putExtra("appointmentID", appointmentID);
                    intent.putExtra("bankName", bankName);
                    intent.putExtra("appointment_date", appointment_date);
                    intent.putExtra("time_interval", time_interval);
                    intent.putExtra("intent_username", username);
                    intent.putExtra("intent_psw", password);
                    startActivity(intent);
                }
            });
        }

    }
}

