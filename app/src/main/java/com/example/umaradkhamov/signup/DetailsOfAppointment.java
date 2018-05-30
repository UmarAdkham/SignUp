package com.example.umaradkhamov.signup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class DetailsOfAppointment extends AppCompatActivity {

    private String qrcode, appointmentID, bankName, branchName, staffName, serviceName, appointment_date, time_interval, username, password;
    private ImageView qrImage;
    private TextView qrBankName, qrServiceName, qrBranchName, qrStaffName, qrDate, qrTime;
    private Button goHome;
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
                Intent intent = new Intent(DetailsOfAppointment.this, BankSelect.class);
                intent.putExtra("intent_username", username);
                intent.putExtra("intent_psw", password);
                startActivity(intent);
                finish();
            }
        });
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
