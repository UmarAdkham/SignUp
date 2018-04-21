package com.example.umaradkhamov.signup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ApplicationDescription extends AppCompatActivity {


    private TextView header, body;
    private Button applyBtn;
    private String serviceName, description, serviceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.application_description);

        header = (TextView) findViewById(R.id.header);
        body = (TextView) findViewById(R.id.body);
        applyBtn = (Button) findViewById(R.id.applyBtn);

        //linking button to the next page
        applyBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(ApplicationDescription.this, Fields.class);
                intent.putExtra("intent_serviceID", serviceID);
                intent.putExtra("intent_serviceName", serviceName);
                intent.putExtra("intent_description", description);
                startActivity(intent);
            }
        });

        //Getting intents
        serviceName = getIntent().getStringExtra("intent_serviceName");
        serviceID = getIntent().getStringExtra("intent_serviceID");
        description = getIntent().getStringExtra("intent_description");

        //Setting intent strings into text views
        header.setText(serviceName);
        body.setText(description);

    }


}
