package com.example.umaradkhamov.signup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ApplicationDescription extends AppCompatActivity {


    private TextView header, body;
    private Button applyBtn;
    private String bankID, serviceName, description, serviceID, password, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.application_description);

        password = getIntent().getStringExtra("intent_psw");
        username = getIntent().getStringExtra("intent_username");
        bankID = getIntent().getStringExtra("bankID");


        serviceName = SelectApplication.intent_serviceName;
        description = SelectApplication.intent_description;
        serviceID = SelectApplication.intent_serviceID;



        header = (TextView) findViewById(R.id.header);
        body = (TextView) findViewById(R.id.description);
        applyBtn = (Button) findViewById(R.id.applyBtn);

        //linking button to the next page
        applyBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(ApplicationDescription.this, ManualDesign.class);
                intent.putExtra("intent_serviceID", serviceID);
                intent.putExtra("intent_serviceName", serviceName);
                intent.putExtra("bankID", bankID);
                intent.putExtra("intent_psw", password);
                intent.putExtra("intent_username", username);
                startActivity(intent);
            }
        });

        //Setting intent strings into text views
        header.setText(serviceName);
        body.setText(description);

    }


}
