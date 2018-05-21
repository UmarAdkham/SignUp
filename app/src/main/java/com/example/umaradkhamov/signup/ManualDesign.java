package com.example.umaradkhamov.signup;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import javax.net.ssl.HttpsURLConnection;

public class ManualDesign extends AppCompatActivity {
    private static final String TAG = "ManualDesign";
    private String serviceName, serviceID, fieldName, fieldType;
    private String firstname, lastname, address, dob, passport, password, username;
    List<String> myList;
    private int numOfFields;
    private StringBuffer sb;
    private JSONArray fields;
    private JSONObject c;
    Map<String, String> map;
    private Void autofill_result;
    private RadioButton[] rb;
    private RadioGroup rg;
    private DatePickerDialog.OnDateSetListener mDateSetListener;


    @SuppressLint("ResourceType")
    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Getting intents
        serviceID = getIntent().getStringExtra("intent_serviceID");
        password = getIntent().getStringExtra("intent_psw");
        serviceName = getIntent().getStringExtra("intent_serviceName");
        username = getIntent().getStringExtra("intent_username");

        //Execute autofill
        try {
            autofill_result = new AutoFill().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        map = new HashMap<>();
        map.put("Firstname", firstname);
        map.put("Lastname", lastname);
        map.put("Address", address);
        map.put("Passport No", passport);

        //Execute getFields
        try {
            fields = new getFields().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        numOfFields = fields.length();
        //Setting design programmatically
        LinearLayout myLayout = new LinearLayout(this);
        myLayout.setOrientation(LinearLayout.VERTICAL);
        for (int i=1; i<= numOfFields; i++){
            //get instance of JSON array
            try {
                c = fields.getJSONObject(i-1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //get fieldName of the instance
            try {
                fieldName = c.getString("fieldName");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            EditText myET = new EditText(this);
            myET.setId(i);

            //Autofill with data if fields are matched
            if(map.containsKey(fieldName)) {
                Log.e(TAG, fieldName + map.get(fieldName));
                myET.setText(map.get(fieldName));
            }//Make dob button
            else if(fieldName.equalsIgnoreCase("Date of Birth")){
                Button dobBtn = new Button(this);
                dobBtn.setText("Date");
                RelativeLayout.LayoutParams dobParams =
                        new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);
                dobParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
               //buttonParams.addRule(RelativeLayout.BELOW, numOfFields+1);
                dobBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar cal = Calendar.getInstance();
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        int day = cal.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dialog = new DatePickerDialog(
                                ManualDesign.this,
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
                        //dobTV.setText(date);
                    }
                };

                myLayout.addView(dobBtn, dobParams);

            }else{
                myET.setHint(fieldName);
            }

            myET.setWidth(800);
            RelativeLayout.LayoutParams etParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            if (i > 1){
                int k = i-1;
                etParam.addRule(RelativeLayout.BELOW, k);

            }
            etParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
            etParam.setMargins(0,0,0,80);

            myLayout.addView(myET, etParam);
            setContentView(myLayout);
        }

        //Radio button try
        rb = new RadioButton[5];
        rg = new RadioGroup(this);
        rg.setOrientation(RadioGroup.HORIZONTAL);
        for(int i=0; i<5; i++){
            rb[i]  = new RadioButton(this);
            rg.addView(rb[i]); //the RadioButtons are added to the radioGroup instead of the layout
            rb[i].setText("Test");
        }
        rg.setId(numOfFields+1);
        RelativeLayout.LayoutParams radioParams =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        radioParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        radioParams.addRule(RelativeLayout.BELOW, numOfFields);




        //Button after fields
        Button myButton = new Button(this);
        myButton.setText("Submit");
        RelativeLayout.LayoutParams buttonParams =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        buttonParams.addRule(RelativeLayout.BELOW, numOfFields+1);
        //End of button
        myLayout.addView(rg);
        myLayout.addView(myButton, buttonParams);


    }

    public  class getFields extends AsyncTask<String, Void, JSONArray> {

        protected void onPreExecute() {

        }
        protected JSONArray doInBackground(String... arg0) {


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
                            int number = contacts.length();
                            Log.e(TAG, "Length: " + number);

                            return contacts;
                            /*// looping through All Contacts
                            for (int i = 0; i < numOfFields; i++) {
                                JSONObject c = contacts.getJSONObject(i);
                                fieldName = c.getString("fieldName");
                                fieldType = c.getString("fieldType");
                            }*/
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
        protected void onPostExecute(JSONArray result) {

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

        }
    }
}
