package com.example.mobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // create variables
    private EditText name;
    private EditText password;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // associate variables with the corresponding elements in the activity
        //name = (EditText)findViewById(R.id.etName);
        password = (EditText)findViewById(R.id.etPassword);
        login = (Button)findViewById(R.id.btnLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputPassword = password.getText().toString();
                if (inputPassword.isEmpty()) {
                    password.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                } else {
                    validate(inputPassword);
                }
            }
        });
    }

    private void validate(String userPassword) {
        //hardcoded for now
        if (userPassword.equals("serveerder")) {
            password.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
        } else {
            password.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            password.setText("");
        }
    }

}