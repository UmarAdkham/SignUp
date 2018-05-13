package com.example.umaradkhamov.signup;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class LogInCustomer extends AppCompatActivity {

    private Button loginBtn, signUpBtn;
    private EditText usernameET, passwordET;
    private String username, password;
    private TextView warning;
    StringBuffer sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_customer);


        loginBtn = (Button) findViewById(R.id.CustLoginBtn);
        signUpBtn = (Button) findViewById(R.id.CustSignUpBtn);
        usernameET = (EditText) findViewById(R.id.CustUnameET);
        passwordET = (EditText) findViewById(R.id.CustPswET);
        warning = (TextView) findViewById(R.id.CustWarning);

        //On click method for Log in
        loginBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                username = usernameET.getText().toString();
                password = passwordET.getText().toString();
                new LogInCustomer.checkCustomer().execute();
            }
        });


        //On click method for sign up
        signUpBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(LogInCustomer.this, SignUpCustomer.class);
                startActivity(intent);
            }
        });

    }


    public class checkCustomer extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }
        protected String doInBackground(String... arg0) {


            try {

                URL url = new URL("http://" +  IPContainer.IP + "/jrlu/checkCustomer.php"); // here is your URL path


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
                //Going to the next page
                Intent intent = new Intent(LogInCustomer.this, SelectApplication.class);
                intent.putExtra("intent_psw", password);
                intent.putExtra("intent_username", username);
                startActivity(intent);
                finish();
            }else{
                warning.setText("Wrong username or password");
            }
        }
    }
}
