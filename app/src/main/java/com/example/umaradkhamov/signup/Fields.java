package com.example.umaradkhamov.signup;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class Fields extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private static final String TAG = "Fields";
    private String serviceName, serviceID;
    private TextView fields_header, dobTV;
    private EditText firstname, lastname, passprtNo, email, address;
    private Button submitForm, dobBtn;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fields_activity);

        fields_header = (TextView) findViewById(R.id.fields_header);
        firstname = (EditText) findViewById(R.id.firstname);
        lastname = (EditText) findViewById(R.id.lastname);
        passprtNo = (EditText) findViewById(R.id.passportNo);
        email = (EditText) findViewById(R.id.email);
        dobBtn = (Button) findViewById(R.id.dobBtn);
        dobTV = (TextView) findViewById(R.id.dobTV);
        address = (EditText) findViewById(R.id.adress);
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

                String date = month + "/" + day + "/" + year;
                dobTV.setText(date);
            }
        };


        //Getting intents
        serviceName = getIntent().getStringExtra("intent_serviceName");
        serviceID = getIntent().getStringExtra("intent_serviceID");

        //Setting intent strings into text views
        fields_header.setText(serviceName);


        //hard coded onclick method for button
        submitForm.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),
                        "Submitted successfully",
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Fields.this, Appointment.class);
                startActivity(intent);

            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }
}



  /* dobBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View dob_builder_view) {
                AlertDialog.Builder dob_builder = new AlertDialog.Builder(Fields.this);
                dob_builder_view = getLayoutInflater().inflate(R.layout.dob_builder_dialog,null);
                dob_builder.setView(dob_builder_view);
                dob_builder.setMessage(getResources().getString(R.string.update_time) +" " + time_value +"?")
                        .setCancelable(true)
                        .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                newTimePT = dob_builder_view.findViewById(R.id.timeForUpd);
                                newTime = newTimePT.getHour() + ":" + newTimePT.getMinute() + ":00";
                                new UpdateTime().execute();
                                Intent intent = new Intent(TimesOfStation.this, TimesOfStation.class);
                                finish();
                                startActivity(intent);

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                dob_builder.create().show();

            }
        });*/
