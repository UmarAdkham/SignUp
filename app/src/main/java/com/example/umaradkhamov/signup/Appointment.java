package com.example.umaradkhamov.signup;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Appointment extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener{

    private static final String TAG_APPOINTMENT = "Appointment";
    private Button appDateBtn;
    private TextView appDateTV;
    Spinner spinnerFrom, spinnerTime;
    List<String> categories, branchIDs, times, intervals;
    ArrayAdapter dataAdapter, timeAdapter;

    private ListView lv;
    private String TAG = Appointment.class.getSimpleName();
    //Intents
    private String bankID, serviceID, serviceName, appointmentID, username, password, qrcode;
    private String branchID, date, time, interval_id;
    private Button submitAppointment;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Void getBranches;
    private StringBuffer sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_activity);

        //Getting intents

        appointmentID = getIntent().getStringExtra("appointmentID");
        qrcode = getIntent().getStringExtra("qrcode");
        serviceName = getIntent().getStringExtra("intent_serviceName");
        serviceID = getIntent().getStringExtra("serviceID");
        bankID = getIntent().getStringExtra("bankID");
        username = getIntent().getStringExtra("intent_username");
        password = getIntent().getStringExtra("intent_psw");



        appDateBtn = (Button) findViewById(R.id.appDateBtn);
        appDateTV = (TextView) findViewById(R.id.appDateTV);


        appDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Appointment.this,
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
                date = year + "-" + month + "-" + day ;
                appDateTV.setText(date);
                new GetTimesForBranch().execute();

            }
        };

        categories = new ArrayList<String>();
        branchIDs = new ArrayList<String>();
        times = new ArrayList<String>();
        intervals = new ArrayList<String>();

        spinnerFrom = (Spinner) findViewById(R.id.branchSpinner);
        spinnerTime = (Spinner) findViewById(R.id.timeSpinner);
        spinnerFrom.setOnItemSelectedListener(this);
        spinnerTime.setOnItemSelectedListener(this);

        submitAppointment = (Button) findViewById(R.id.submitAppointment);
        submitAppointment.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                /*Intent intent = new Intent(Appointment.this, SeeYou.class);
                startActivity(intent);*/
                if (date == null){
                    Toast.makeText(getApplicationContext(), "Please select a date and time", Toast.LENGTH_LONG).show();
                }
                else {
                    new MakeAppointment().execute();
                }

            }
        });

        try {
            getBranches = new GetBranches().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()){
            case R.id.branchSpinner:
                int selectedBranchID = parent.getSelectedItemPosition();
                branchID = branchIDs.get(selectedBranchID);
                if(date != null) {
                    Toast.makeText(this, "Date 1: " + date, Toast.LENGTH_SHORT).show();
                    new GetTimesForBranch().execute();
                }
                break;
            case R.id.timeSpinner:
                int selectedTimeID = parent.getSelectedItemPosition();
                //time = parent.getSelectedItem().toString();
                interval_id = intervals.get(selectedTimeID);
                Toast.makeText(this, "Interval:  " + interval_id, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }

    private class GetBranches extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Toast.makeText(IPContainer.this,"XML Data is downloading",Toast.LENGTH_LONG).show();
        }


        @Override
        protected Void doInBackground(Void... arg0) {

            try {

                URL url = new URL("http://" + IPContainer.IP + "/jrlu/getBranches.php"); // here is your URL path

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                //conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));

                writer.write("bankID=" + bankID);
                writer.write("");
                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    try {
                        InputStream is = conn.getInputStream();

                        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                        Document doc = dBuilder.parse(is);

                        Element element=doc.getDocumentElement();
                        element.normalize();

                        NodeList nList = doc.getElementsByTagName("branch");

                        for (int i=0; i<nList.getLength(); i++) {

                            Node node = nList.item(i);
                            if (node.getNodeType() == Node.ELEMENT_NODE) {
                                Element element2 = (Element) node;

                                categories.add(getValue("branchName", element2));
                                branchIDs.add(getValue("branchID", element2));
                            }
                        }

                        BufferedReader in=new BufferedReader(new
                                InputStreamReader(
                                is));

                        StringBuffer sb = new StringBuffer("");
                        String line="";

                        while((line = in.readLine()) != null) {

                            sb.append(line);
                            break;
                        }

                        in.close();
                        // return sb.toString();
                        String answer = sb.toString();

                        Log.e(TAG, "Response from url XML: " + answer);

                    } catch (Exception e) {e.printStackTrace();}
                }
                else {
                    //return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                //return new String("Exception: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            dataAdapter = new ArrayAdapter(getBaseContext(), android.R.layout.preference_category, categories);

            // Drop down layout style
            dataAdapter.setDropDownViewResource(android.R.layout.preference_category);

            // attaching data adapter to spinner
            spinnerFrom.setAdapter(dataAdapter);
            }
    }

    private class GetTimesForBranch extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Toast.makeText(IPContainer.this,"XML Data is downloading",Toast.LENGTH_LONG).show();
        }


        @Override
        protected Void doInBackground(Void... arg0) {

            try {

                URL url = new URL("http://" + IPContainer.IP + "/jrlu/getTimeIntervals.php"); // here is your URL path

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                //conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));


                Log.e(TAG, "date: " + date);
                writer.write("branchID=" + branchID + "&date=" + date);
                writer.write("");
                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    try {
                        InputStream is = conn.getInputStream();

                        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                        Document doc = dBuilder.parse(is);

                        Element element=doc.getDocumentElement();
                        element.normalize();

                        NodeList nList = doc.getElementsByTagName("time");
                        times.clear();
                        for (int i=0; i<nList.getLength(); i++) {

                            Node node = nList.item(i);
                            if (node.getNodeType() == Node.ELEMENT_NODE) {
                                Element element2 = (Element) node;
                                times.add(getValue("time_interval", element2));
                                intervals.add(getValue("interval_id", element2));
                            }
                        }

                        BufferedReader in=new BufferedReader(new
                                InputStreamReader(
                                is));

                        StringBuffer sb = new StringBuffer("");
                        String line="";

                        while((line = in.readLine()) != null) {

                            sb.append(line);
                            break;
                        }

                        in.close();
                        // return sb.toString();
                        String answer = sb.toString();

                        Log.e(TAG, "Response from url XML: " + answer);

                    } catch (Exception e) {e.printStackTrace();}
                }
                else {
                    //return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                //return new String("Exception: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            timeAdapter = new ArrayAdapter(getBaseContext(), android.R.layout.preference_category, times);

            // Drop down layout style
            timeAdapter.setDropDownViewResource(android.R.layout.preference_category);

            // attaching data adapter to spinner
            spinnerTime.setAdapter(timeAdapter);
        }
    }

    private class MakeAppointment extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Toast.makeText(IPContainer.this,"XML Data is downloading",Toast.LENGTH_LONG).show();
        }


        @Override
        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://" + IPContainer.IP + "/jrlu/makeAppointment.php"); // here is your URL path

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                //conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));


                Log.e(TAG, "date: " + date + " appointmentID: " + appointmentID + " branchID: "
                                + branchID + " interval: " + interval_id);
                writer.write("appointmentID=" + appointmentID + "&branchID=" + branchID
                        + "&appointment_date=" + date + "&interval_id=" + interval_id );
                writer.write("");
                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

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

                }
                else {
                    //return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                //return new String("Exception: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if(sb.toString().equals("true")){
                Toast.makeText(getApplicationContext(), "Appointment has been made", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Appointment.this, DetailsOfAppointment.class);
                intent.putExtra("qrcode", qrcode);
                intent.putExtra("appointmentID", appointmentID);
                intent.putExtra("bankID", bankID);
                intent.putExtra("serviceID", serviceID);
                intent.putExtra("serviceName", serviceName);
                intent.putExtra("intent_psw", password);
                intent.putExtra("intent_username", username);
                startActivity(intent);
                finish();
            }else if(sb.toString().equals("false")){
                Toast.makeText(getApplicationContext(), "Could not create Appointment", Toast.LENGTH_SHORT).show();
            }else{
                Log.e(TAG, "error: " + sb.toString());
                Toast.makeText(getApplicationContext(), "Sorry, something went wrong",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


}
