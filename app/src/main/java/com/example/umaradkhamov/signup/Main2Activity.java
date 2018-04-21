package com.example.umaradkhamov.signup;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Main2Activity extends AppCompatActivity {

    private Button signUpBtn;
    private EditText usernameET, fNameET, lNameET, passwordET;
    private String username, firstname, lastname, password;
    StringBuffer sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        usernameET = (EditText) findViewById(R.id.unameET);
        fNameET = (EditText) findViewById(R.id.firstnameET);
        lNameET = (EditText) findViewById(R.id.lastnameET);
        passwordET = (EditText) findViewById(R.id.passwordET);
        signUpBtn = (Button) findViewById(R.id.registerBtn);


        //On click method for signUpBtn
        signUpBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                username = usernameET.getText().toString();
                firstname = fNameET.getText().toString();
                lastname = lNameET.getText().toString();
                password = passwordET.getText().toString();
                new Main2Activity.registerAdmin().execute();
            }
        });
        //End of on click method
    }


    //Registering new Admin in the database.
    public class registerAdmin extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://" +  IPContainer.IP + "/jrlu/registerAdmin.php"); // here is your URL path


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));

                writer.write("name=" + username + "&firstname=" + firstname + "&lastname=" + lastname + "&psw=" + password);
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
            if(sb.toString().equals("true")){
                Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "Registered successfully",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Main2Activity.this, IPContainer.class);
                startActivity(intent);
                finish();
            }
        }
    }
    //End of registering

}

