package com.example.mobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.VoiceInteractor;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // create variables
    private EditText password;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //UI-configurations
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

        //Creating a jsonBody with the login credentials
        JSONObject jsonBody = new JSONObject();

        try {
            //The role for this mobile app is 'serveerder' by default
            jsonBody.put("role", "serveerder");
            jsonBody.put("password", userPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //API connection
        //Replace the ip address with ip address of your device
        //Replace "app.run" in app.py with "app.run(host='0.0.0.0', port=5000)"

        String URL = "http://192.168.2.91:5000/api/auth";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Rest Response: ", response.toString());
                        try {
                            //When the response contains a token
                            response.getString("token");
                            //Reset underline to white, create and start new intent
                            password.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest Response: ", error.toString());
                        //When login request returns negative, display error (red line)
                        password.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                    }
                }
        );

        requestQueue.add(objectRequest);

    }

}