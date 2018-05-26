package com.example.umaradkhamov.signup;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class Fields extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private static final String TAG = "Fields";
    //Customer data
    private String firstname, lastname, address, dob, passport, password, username;
    //Fields data
    private String serviceName, serviceID, fieldName, fieldType;
    private TextView fields_header, dobTV;
    private EditText firstnameET, lastnameET, passprtNo, email, addressET;
    private Button submitForm, dobBtn;
    private StringBuffer sb;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fields_activity);

        fields_header = (TextView) findViewById(R.id.fields_header);
        firstnameET = (EditText) findViewById(R.id.firstname);
        lastnameET = (EditText) findViewById(R.id.lastname);
        passprtNo = (EditText) findViewById(R.id.passportNo);
        email = (EditText) findViewById(R.id.email);
        dobBtn = (Button) findViewById(R.id.dobBtn);
        dobTV = (TextView) findViewById(R.id.dobTV);
        addressET = (EditText) findViewById(R.id.adress);
        submitForm = (Button) findViewById(R.id.submitForm);

        dobBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Fields.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
               // Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = day + "/" + month + "/" + year;
                dobTV.setText(date);
            }
        };


        //Getting intents
        serviceName = getIntent().getStringExtra("intent_serviceName");
        serviceID = getIntent().getStringExtra("intent_serviceID");
        password = BankSelect.password;
        username = getIntent().getStringExtra("intent_username");

        Toast.makeText(getApplicationContext(), password,Toast.LENGTH_LONG).show();


        //Setting intent strings into text views
        fields_header.setText(serviceName);

        new getFields().execute();
        new AutoFill().execute();

        //hard coded onclick method for button
        submitForm.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Submitted successfully",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Fields.this, Appointment.class);
                startActivity(intent);

            }
        });

    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }

    public class getFields extends AsyncTask<String, Void, Void> {

        protected void onPreExecute() {

        }
        protected Void doInBackground(String... arg0) {


            try {
                URL url = new URL("http://" +  IPContainer.IP + "/jrlu/getFields.php"); // here is your URL path


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));

                writer.write("serviceID=" + serviceID);
                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    //return sb.toString();
                    String answer = sb.toString();

                    Log.e(TAG, "Service ID: " + serviceID);
                    Log.e(TAG, "Response from url: " + answer);

                    if (answer != null) {
                        try {
                            JSONObject jsonObj = new JSONObject(answer);

                            // Getting JSON Array node
                            JSONArray contacts = jsonObj.getJSONArray("fields");

                            // looping through All Contacts
                            for (int i = 0; i < contacts.length(); i++) {
                                JSONObject c = contacts.getJSONObject(i);
                                fieldName = c.getString("fieldName");
                                fieldType = c.getString("fieldType");
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
                //Check it. If I comment it out --> crash
               /* Toast.makeText(getApplicationContext(),
                        "Cannot connect to php file",
                        Toast.LENGTH_LONG).show();*/
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            /*firstnameET.setText(firstname);
            lastnameET.setText(lastname);
            addressET.setText(address);
            dobTV.setText(dob);
            passprtNo.setText(passport);*/
        }
    }


    public class AutoFill extends AsyncTask<String, Void, Void> {

        protected void onPreExecute() {

        }
        protected Void doInBackground(String... arg0) {


            try {
                URL url = new URL("http://" +  IPContainer.IP + "/jrlu/autofill.php"); // here is your URL path


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));

                writer.write("name=" + username + "&psw=" + password);
                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    //return sb.toString();
                    String answer = sb.toString();

                    Log.e(TAG, "Response from url: " + answer);

                    if (answer != null) {
                        try {
                            JSONObject jsonObj = new JSONObject(answer);

                            // Getting JSON Array node
                            JSONArray contacts = jsonObj.getJSONArray("autofill");

                            // looping through All Contacts
                            for (int i = 0; i < contacts.length(); i++) {
                                JSONObject c = contacts.getJSONObject(i);
                                firstname = c.getString("firstname");
                                lastname = c.getString("lastname");
                                address = c.getString("address");
                                dob = c.getString("dob");
                                passport = c.getString("passport");
                                Toast.makeText(getApplicationContext(),
                                        firstname,
                                        Toast.LENGTH_LONG).show();
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
                //Check it. If I comment it out --> crash
               /* Toast.makeText(getApplicationContext(),
                        "Cannot connect to php file",
                        Toast.LENGTH_LONG).show();*/
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            firstnameET.setText(firstname);
            lastnameET.setText(lastname);
            addressET.setText(address);
            dobTV.setText(dob);
            passprtNo.setText(passport);
        }
    }
}


