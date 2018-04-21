package com.example.umaradkhamov.signup;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import javax.net.ssl.HttpsURLConnection;

public class SignUpCustomer extends AppCompatActivity {

    private Button signUpBtn, dobSignUp;
    private EditText usernameET, fNameET, lNameET, passwordET, addressET, passportNoET;
    private String username, firstname, lastname, password, address, passportNo, dobString;
    private TextView dob;
    StringBuffer sb;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_customer);

        //buying EditTexts by ID
        usernameET = (EditText) findViewById(R.id.CustUsernameET);
        fNameET = (EditText) findViewById(R.id.CustFirstnameET);
        lNameET = (EditText) findViewById(R.id.CustLastnameET);
        passwordET = (EditText) findViewById(R.id.CustPasswordET);
        addressET = (EditText) findViewById(R.id.address);
        passportNoET = (EditText) findViewById(R.id.myKad);

        //buying buttons
        signUpBtn = (Button) findViewById(R.id.CustRegisterBtn);
        dobSignUp = (Button) findViewById(R.id.dobSignUp);
        dob = (TextView) findViewById(R.id.dobTextSignUp);

        dobSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        SignUpCustomer.this,
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
                String date = month + "/" + day + "/" + year;
                dob.setText(date);


            }
        };

        //On click method for signUpBtn
        signUpBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                username = usernameET.getText().toString();
                firstname = fNameET.getText().toString();
                lastname = lNameET.getText().toString();
                password = passwordET.getText().toString();
                address = addressET.getText().toString();
                passportNo = passportNoET.getText().toString();
                dobString = dob.getText().toString();
                new SignUpCustomer.registerCustomer().execute();
            }
        });
        //End of on click method
    }


    //Registering new Admin in the database.
    public class registerCustomer extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://" +  IPContainer.IP + "/jrlu/registerCustomer.php"); // here is your URL path


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));

                writer.write("name=" + username + "&firstname=" + firstname + "&lastname=" + lastname + "&psw=" + password +
                "&dob=" + dobString + "&address=" + address + "&passportNo=" + passportNo);
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
            if(sb.toString().equals("duplicate")){
                Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_SHORT).show();
            }else if(sb.toString().equals("false")){
                Toast.makeText(getApplicationContext(), "Failed to sign up", Toast.LENGTH_SHORT).show();
            }else if(sb.toString().equals("empty")){
                Toast.makeText(getApplicationContext(), "Please, fill out the fields", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Registered successfully",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpCustomer.this, LogInCustomer.class);
                startActivity(intent);
                finish();
            }
        }
    }
    //End of registering

}