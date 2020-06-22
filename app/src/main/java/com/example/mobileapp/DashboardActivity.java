package com.example.mobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DashboardActivity extends AppCompatActivity {

    // create variables
    private Button btnLogout;
    private Button btnAddOrder;
    private Button btnViewOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //UI-configurations
        // associate variables with the corresponding elements in the activity
        btnLogout = (Button)findViewById(R.id.btnLogout);
        btnAddOrder = (Button)findViewById(R.id.btnAddOrder);
        btnViewOrders = (Button)findViewById(R.id.btnViewOrders);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.password.setText("");
                finish();
            }
        });

        btnAddOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        AddOrderActivity.class);
                startActivity(intent);
            }
        });

        btnViewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        ViewOrdersActivity.class);
                startActivity(intent);
            }
        });
    }
}