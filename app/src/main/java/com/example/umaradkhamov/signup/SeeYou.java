package com.example.umaradkhamov.signup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SeeYou extends AppCompatActivity {
    private Button okAppBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_you_activity);
        okAppBtn = (Button) findViewById(R.id.okAppBtn);

        okAppBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(SeeYou.this, BankSelect.class);
                startActivity(intent);
            }
        });
    }
}
