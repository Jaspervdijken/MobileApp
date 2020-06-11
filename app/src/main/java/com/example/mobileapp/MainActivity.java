package com.example.mobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    private TextView errorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // associate variables with the corresponding elements in the activity
        name = (EditText)findViewById(R.id.etName);
        password = (EditText)findViewById(R.id.etPassword);
        errorMsg = (TextView)findViewById(R.id.errorMsg);
        login = (Button)findViewById(R.id.btnLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputName = name.getText().toString();
                String inputPassword = password.getText().toString();
                if (inputName.isEmpty() || inputPassword.isEmpty()) {
                    errorMsg.setText("Please fill in all fields...");
                } else {
                    validate(inputName, inputPassword);
                }
            }
        });
    }

    private void validate(String userName, String userPassword) {
        //hardcoded for now
        if (userName.equals("serveerder") && userPassword.equals("serveerder")) {
            errorMsg.setText("");
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
        } else {
            errorMsg.setText("Invalid credentials...");
        }
    }

}