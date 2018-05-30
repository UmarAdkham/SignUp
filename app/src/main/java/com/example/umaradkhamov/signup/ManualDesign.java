package com.example.umaradkhamov.signup;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

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
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import javax.net.ssl.HttpsURLConnection;

public class ManualDesign extends AppCompatActivity {
    private static final String TAG = "ManualDesign";
    //Strings to get
    private String bankID, serviceName, serviceID, fieldName, fieldType;
    private String encryption_key, firstname, lastname, address, dob, passport, password, username, email, gender, postcode, json;
    private int numOfFields;
    private StringBuffer sb;
    private JSONArray fields;
    private JSONObject c;
    private Map<String, String> map;
    ArrayList<String> fieldNames = new ArrayList<String>();
    private Void autofill_result;
    private RadioButton[] rb;
    private RadioGroup rgGender, rgEdu, rgMarital;
    private DatePickerDialog.OnDateSetListener mDateSetListener;


    @SuppressLint("ResourceType")
    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Submit your info");
        //Getting intents
        serviceID = getIntent().getStringExtra("intent_serviceID");
        password = getIntent().getStringExtra("intent_psw");
        serviceName = getIntent().getStringExtra("intent_serviceName");
        username = getIntent().getStringExtra("intent_username");
        bankID = getIntent().getStringExtra("bankID");

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
        map.put("MyKad No/Passport No", passport);
        map.put("Postcode", postcode);
        map.put("Email", email);

        //Execute getFields
        try {
            fields = new getFields().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        numOfFields = fields.length();
        //initialize json string
        json = "[";

        //Setting design programmatically
        ScrollView scrl=new ScrollView(this);

        //LinearLayout
        LinearLayout myLayout = new LinearLayout(this);
        myLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        myLayout.setLayoutParams(llParams);
        scrl.addView(myLayout);
        //#LinearLayout

        //TextView for serviceName
        TextView serviceNameTV = new TextView(this);
        serviceNameTV.setText(serviceName);
        LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        nameParams.topMargin = 30;
        serviceNameTV.setTextSize(30);
        serviceNameTV.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
        serviceNameTV.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        serviceNameTV.setTypeface(Typeface.DEFAULT_BOLD);
        myLayout.addView(serviceNameTV, nameParams);
        //#TextView for serviceName

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
                fieldType = c.getString("fieldType");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Autofill with data if fields are matched
            if(map.containsKey(fieldName) && fieldType.equalsIgnoreCase("EditText")) {
                //TextView
                TextView tv = new TextView(this);
                tv.setText(fieldName);
                LinearLayout.LayoutParams etTvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                etTvParams.setMargins(90, 85, 0, 0);
                tv.setTextSize(19);
                tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                tv.setTypeface(Typeface.DEFAULT_BOLD);
                //#TextView

                //EditText
                EditText myET = new EditText(this);
                myET.setId(i);
                myET.setText(map.get(fieldName));
                LinearLayout.LayoutParams etParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                etParams.setMargins(90, 0, 90, 0);
                //#EditText

                myLayout.addView(tv, etTvParams);
                myLayout.addView(myET, etParams);
            } //Display Date of Birth
            else if(fieldName.equalsIgnoreCase("Date of Birth")){
                    //TextView
                    TextView tv=new TextView(this);
                    tv.setText(fieldName);
                    LinearLayout.LayoutParams dobTvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    dobTvParams.setMargins(90, 85, 0, 0);
                    tv.setTextSize(19);
                    tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                    tv.setTypeface(Typeface.DEFAULT_BOLD);
                    //#TextView

                    //DOB Value
                    TextView dobTv=new TextView(this);
                    dobTv.setId(i);
                    dobTv.setText(dob);
                    LinearLayout.LayoutParams dobValueTvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    dobValueTvParams.setMargins(90, 0, 0, 0);
                    dobTv.setTextSize(18);
                    dobTv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                    //myLayout.addView(dobBtn, dobParams);
                    myLayout.addView(tv, dobTvParams);
                    myLayout.addView(dobTv, dobValueTvParams);
            }
            else if(fieldName.equalsIgnoreCase("Educational Level")){
                //TextView
                TextView tv=new TextView(this);
                tv.setText(fieldName);
                LinearLayout.LayoutParams etTvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                etTvParams.setMargins(90, 85, 0, 0);
                tv.setTextSize(19);
                tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                tv.setTypeface(Typeface.DEFAULT_BOLD);
                //#TextView

                //LinearLayout for RadioButtons
                LinearLayout rbLayout = new LinearLayout(this);
                rbLayout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams rbParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                rbParams.setMargins(90, 0, 90, 0);
                rbLayout.setLayoutParams(rbParams);
                //#LinearLayout for RadioButtons

                //Radio button try
                rb = new RadioButton[5];
                rgEdu = new RadioGroup(this);
                rgEdu.setOrientation(RadioGroup.VERTICAL);
                for(int k=1; k<=4; k++){
                    rb[k] = new RadioButton(this);
                    rb[k].setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                    rb[k].setTextSize(18);
                    rgEdu.addView(rb[k]); //the RadioButtons are added to the radioGroup instead of the layout
                    if (k==1){
                        int newID = k + 123;
                        rb[k].setId(newID);
                        rb[k].setText("Primary");
                    }else if(k==2) {
                        //to make the id different
                        int newID = k + 123;
                        rb[k].setId(newID);
                        rb[k].setText("Secondary");
                        rgEdu.check(rb[k].getId());
                    }else if(k==3) {
                        int newID = k + 123;
                        rb[k].setId(newID);
                        rb[k].setText("Degree");
                    }else{
                        int newID = k + 123;
                        rb[k].setId(newID);
                        rb[k].setText("Master");
                    }
                }

                LinearLayout.LayoutParams radioParams =
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                myLayout.addView(tv, etTvParams);
                rbLayout.addView(rgEdu,radioParams);
                myLayout.addView(rbLayout, rbParams);
            }else if(fieldName.equalsIgnoreCase("Marital Status")){
                //TextView
                TextView tv=new TextView(this);
                tv.setText(fieldName);
                LinearLayout.LayoutParams etTvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                etTvParams.setMargins(90, 85, 0, 0);
                tv.setTextSize(19);
                tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                tv.setTypeface(Typeface.DEFAULT_BOLD);
                //#TextView

                //LinearLayout for RadioButtons
                LinearLayout rbLayout = new LinearLayout(this);
                rbLayout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams rbParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                rbParams.setMargins(90, 0, 90, 0);
                rbLayout.setLayoutParams(rbParams);
                //#LinearLayout for RadioButtons

                //Radio button try
                rb = new RadioButton[3];
                rgMarital = new RadioGroup(this);
                rgMarital.setOrientation(RadioGroup.VERTICAL);
                for(int k=1; k<=2; k++){
                    rb[k] = new RadioButton(this);
                    rb[k].setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                    rb[k].setTextSize(18);
                    rgMarital.addView(rb[k]); //the RadioButtons are added to the radioGroup instead of the layout
                    if (k==1){
                        int newID = k + 223;
                        rb[k].setId(newID);
                        rb[k].setText("Single");
                        rgMarital.check(rb[k].getId());

                    }else {
                        int newID = k + 223;
                        rb[k].setId(newID);
                        rb[k].setText("Married");
                    }
                }
                LinearLayout.LayoutParams radioParams =
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);

                myLayout.addView(tv, etTvParams);
                rbLayout.addView(rgMarital,radioParams);
                myLayout.addView(rbLayout, rbParams);
            }else if(fieldName.equalsIgnoreCase("Gender")){
                //TextView
                TextView tv=new TextView(this);
                tv.setText(fieldName);
                LinearLayout.LayoutParams etTvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                etTvParams.setMargins(90, 85, 0, 0);
                tv.setTextSize(19);
                tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                tv.setTypeface(Typeface.DEFAULT_BOLD);
                //#TextView

                //Radio button try
               /* rb = new RadioButton[5];
                rgGender = new RadioGroup(this);
                rgGender.setOrientation(RadioGroup.HORIZONTAL);
                for(int k=1; k<=2; k++){
                    rb[k] = new RadioButton(this);
                    rgGender.addView(rb[k]); //the RadioButtons are added to the radioGroup instead of the layout
                    if (k==1){
                        int newID = k + 113;
                        rb[k].setId(newID);
                        rb[k].setText("Male");
                        rgGender.check(rb[k].getId());
                    }else {
                        int newID = k + 113;
                        rb[k].setId(newID);
                        rb[k].setText("Female");
                    }
                }
                LinearLayout.LayoutParams radioParams =
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);*/

                //Gender Value
                TextView genderTV=new TextView(this);
                genderTV.setId(i);
                genderTV.setText(gender);
                LinearLayout.LayoutParams genderParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                genderParams.setMargins(90, 0, 0, 0);
                genderTV.setTextSize(18);
                genderTV.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                //myLayout.addView(dobBtn, dobParams);
                myLayout.addView(tv, etTvParams);
                myLayout.addView(genderTV, genderParams);
            }else{
                //TextView
                TextView tv=new TextView(this);
                tv.setText(fieldName);
                LinearLayout.LayoutParams etTvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                etTvParams.setMargins(90, 85, 0, 0);
                tv.setTextSize(19);
                tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                tv.setTypeface(Typeface.DEFAULT_BOLD);
                //#TextView

                //EditText
                EditText myET = new EditText(this);
                myET.setId(i);
                myET.setHint(fieldName);
                LinearLayout.LayoutParams etParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                etParams.setMargins(90, 0, 90, 0);
                //#EditText

                myLayout.addView(tv, etTvParams);
                myLayout.addView(myET, etParams);
            }

        }

        //Submit Button after fields
        Button myButton = new Button(this);
        myButton.setText("Submit");
        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
        buttonParams.setMargins(90,90,90,90);
        myButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        myButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
        myButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                int a = 0;
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
                        fieldType = c.getString("fieldType");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (a == 1) {
                        json += ',';
                    }

                    //Autofill with data if fields are matched
                    if(map.containsKey(fieldName) && fieldType.equalsIgnoreCase("EditText")) {
                        EditText myET = (EditText) findViewById(i);
                        json += "{\"Fieldname\":\"" +fieldName + "\",";
                        json += "\"Value\":\"" + myET.getText().toString() + "\"}";

                    } else if(fieldName.equalsIgnoreCase("Date of Birth")){
                        TextView dobTv= (TextView) findViewById(i);
                        json += "{\"Fieldname\":\"" +fieldName + "\",";
                        json += "\"Value\":\"" + dobTv.getText().toString() + "\"}";
                    }
                    else if(fieldName.equalsIgnoreCase("Educational Level")){
                        int selectedId = rgEdu.getCheckedRadioButtonId();
                        // find the radiobutton by returned id
                        RadioButton radioButton = (RadioButton) findViewById(selectedId);
                        String s = radioButton.getText().toString();
                        json += "{\"Fieldname\":\"" +fieldName + "\",";
                        json += "\"Value\":\"" + s + "\"}";
                    }else if(fieldName.equalsIgnoreCase("Marital Status")){
                        int selectedId = rgMarital.getCheckedRadioButtonId();
                        // find the radiobutton by returned id
                        RadioButton radioButton = (RadioButton) findViewById(selectedId);
                        String s = radioButton.getText().toString();
                        json += "{\"Fieldname\":\"" +fieldName + "\",";
                        json += "\"Value\":\"" + s + "\"}";
                    }else if(fieldName.equalsIgnoreCase("Gender")){
                        int selectedId = rgGender.getCheckedRadioButtonId();
                        // find the radiobutton by returned id
                        RadioButton radioButton = (RadioButton) findViewById(selectedId);
                        String s = radioButton.getText().toString();
                        json += "{\"Fieldname\":\"" +fieldName + "\",";
                        json += "\"Value\":\"" + s + "\"}";
                    }else{
                        EditText myET = (EditText) findViewById(i);
                        String s = " ";
                        if(!myET.getText().toString().isEmpty()){
                            s = myET.getText().toString();
                        }
                        json += "{\"Fieldname\":\"" +fieldName + "\",";
                        json += "\"Value\":\"" + s + "\"}";
                    }

                    a=1;
                }

                json += "]";
                new ManualDesign.submitInfo().execute();
            }
        });
        //End of button
        myLayout.addView(myButton, buttonParams);
        setContentView(scrl);
    }

    public class submitInfo extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://" +  IPContainer.IP + "/jrlu/submitInfo.php"); // here is your URL path

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));

                Log.e(TAG, "Array" + json);
                Log.e(TAG, "serviceID" + serviceID);
                Log.e(TAG, "customerID" + username);

                //Generate random encryption key
                int leftLimit = 97; // letter 'a'
                int rightLimit = 122; // letter 'z'
                int targetStringLength = 10;
                Random random = new Random();
                StringBuilder buffer = new StringBuilder(targetStringLength);
                for (int i = 0; i < targetStringLength; i++) {
                    int randomLimitedInt = leftLimit + (int)
                            (random.nextFloat() * (rightLimit - leftLimit + 1));
                    buffer.append((char) randomLimitedInt);
                }
                encryption_key = buffer.toString();

                writer.write("serviceID=" + serviceID + "&customerID=" + username + "&fieldNames=" + json +
                "&key=" + encryption_key);

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
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            if(android.text.TextUtils.isDigitsOnly(sb.toString())){
                String appointmentID = sb.toString();
                String qrcode = appointmentID + "<>" + encryption_key;
                Log.e(TAG, "QRCode" + qrcode);
                Log.e(TAG, "appoitnmentID" + appointmentID);
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ManualDesign.this, Appointment.class);
                intent.putExtra("qrcode", qrcode);
                intent.putExtra("appointmentID", appointmentID);
                intent.putExtra("bankID", bankID);
                intent.putExtra("serviceID", serviceID);
                intent.putExtra("serviceName", serviceName);
                intent.putExtra("intent_psw", password);
                intent.putExtra("intent_username", username);
                startActivity(intent);

            }else if(sb.toString().contains("f")) {
                Toast.makeText(getApplicationContext(), "Sorry, something went wrong", Toast.LENGTH_SHORT).show();
            }
            else {
                Log.e(TAG, "Error" + sb.toString());
            }
        }
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
                                postcode = c.getString("postcode");
                                email = c.getString("email");
                                gender = c.getString("gender");
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return true;
    }
}
