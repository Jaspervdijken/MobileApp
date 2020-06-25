package com.example.mobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    // create variables
    private Button btnLogout;
    private Button btnAddOrder;
    private Button btnViewOrders;

    //Get products from api
    private ArrayList<String> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        getProductNames();

        //UI-configurations
        // associate variables with the corresponding elements in the activity
        btnLogout = (Button)findViewById(R.id.btnLogout);
        btnAddOrder = (Button)findViewById(R.id.btnAddOrder);
        btnViewOrders = (Button)findViewById(R.id.btnViewOrders);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.password.setText("");
                MainActivity.password.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
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
                intent.putExtra("PRODUCTS_FROM_API", products);
                startActivity(intent);
            }
        });
    }

    private void getProductNames() {
        //API connection
        String URL = "http://192.168.2.91:5000/api/get_all_products";

        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Rest Response: ", response.toString());
                        try {
                            //Get the product names from api, store in ArrayList 'products'
                            products = new ArrayList<String>();
                            JSONObject res = response;
                            JSONArray arr1 = res.getJSONArray("products");
                            for (int i = 0; i < arr1.length(); i++) {
                                //For every product
                                JSONObject arr2 = (JSONObject) arr1.get(i);
                                //Extract product name
                                String productName = (String) arr2.get("name");
                                //Add to ArrayList
                                products.add(productName);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest Response: ", error.toString());
                    }
                }
        );

        requestQueue.add(objectRequest);

    }
}