package com.example.umaradkhamov.signup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

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

import javax.net.ssl.HttpsURLConnection;

public class DetailsOfAppointment extends AppCompatActivity {

    private String TAG = DetailsOfAppointment.class.getSimpleName();
    private String qrcode, appointmentID, bankName, branchName, staffName, serviceName,
            appointment_date, time_interval, username, password, hardcopies;
    private ImageView qrImage;
    private TextView qrBankName, qrServiceName, qrBranchName, qrStaffName, qrDate, qrTime, qrHardcopy;
    private Button goHome;
    private StringBuffer sb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_of_appointment);

        qrImage = (ImageView) findViewById(R.id.qrImage);
        goHome = (Button) findViewById((R.id.goToHome)) ;

        //Getting intents
        qrcode = getIntent().getStringExtra("qrcode");
        appointmentID = getIntent().getStringExtra("appointmentID");
        bankName = getIntent().getStringExtra("bankName");
        branchName = getIntent().getStringExtra("branchName");
        serviceName = getIntent().getStringExtra("serviceName");
        staffName = getIntent().getStringExtra("staffName");
        appointment_date = getIntent().getStringExtra("appointment_date");
        time_interval = getIntent().getStringExtra("time_interval");
        username = getIntent().getStringExtra("intent_username");
        password = getIntent().getStringExtra("intent_psw");

        qrBankName = (TextView) findViewById(R.id.qrBankName);
        qrBankName.setText(bankName);
        qrBranchName = (TextView) findViewById(R.id.qrBranchName);
        qrBranchName.setText(branchName);
        qrServiceName = (TextView) findViewById(R.id.qr_serviceName);
        qrServiceName.setText(serviceName);
        qrStaffName = (TextView) findViewById(R.id.qrStaffName);
        qrStaffName.setText(staffName);
        qrDate = (TextView) findViewById(R.id.qrDate);
        qrDate.setText(appointment_date);
        qrTime = (TextView) findViewById(R.id.qrTime);
        qrTime.setText(time_interval);
        qrHardcopy = (TextView) findViewById(R.id.qrHardcopy);

        //Execute get hardcopies
        new DetailsOfAppointment.getHardcopies().execute();





        //Coming from Appointment page
        if (qrcode != null) {
            SharedPreference2 sharedPreference = new SharedPreference2();
            sharedPreference.save(appointmentID, getApplicationContext(), qrcode);

            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(qrcode, BarcodeFormat.QR_CODE, 200, 200);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                qrImage.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }//Coming from AppointmentList page
        else{
            SharedPreference2 sharedPreference = new SharedPreference2();
            String storedQRCode = sharedPreference.getValue(appointmentID, getApplicationContext());
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(storedQRCode, BarcodeFormat.QR_CODE, 200, 200);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                qrImage.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }

        goHome.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                new DetailsOfAppointment.checkStatus().execute();
            }
        });
    }

    public class checkStatus extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://" + IPContainer.IP + "/jrlu/checkStatus.php"); // here is your URL path


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));

                writer.write("appointmentID=" + appointmentID);
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
            if (sb.toString().equals("true")) {
                Toast.makeText(getApplicationContext(), "You gained additional points for being on time. Thanks!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(DetailsOfAppointment.this, BankSelect.class);
                intent.putExtra("intent_username", username);
                intent.putExtra("intent_psw", password);
                startActivity(intent);
                finish();
            } else {
                //Toast.makeText(getApplicationContext(), "Not yet", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DetailsOfAppointment.this, BankSelect.class);
                intent.putExtra("intent_username", username);
                intent.putExtra("intent_psw", password);
                startActivity(intent);
                finish();
            }
        }
    }

    public class getHardcopies extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://" + IPContainer.IP + "/jrlu/getHardcopies.php"); // here is your URL path


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));

                writer.write("appointmentID=" + appointmentID);
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
            if (!sb.toString().equals("false")) {
                qrHardcopy.setText(sb.toString());
            } else {
                Toast.makeText(getApplicationContext(), "Failed to get hardcopies", Toast.LENGTH_SHORT).show();
            }
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
