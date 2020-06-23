package com.example.mobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ViewOrdersActivity extends AppCompatActivity {

    private TextView outputText;
    private Spinner spinner;
    private static final String[] paths = {"1", "2", "3","4", "5", "6", "7", "8", "9","10"};
    private int selectedTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);

        outputText = (TextView)findViewById(R.id.outputText);
        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(ViewOrdersActivity.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                selectedTable = Integer.parseInt(paths[position]);
                getTableInfo(selectedTable);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

    }

    private void getTableInfo(int tableNumber) {

        //Creating a jsonBody with the tableNumber
        final JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("table_number", tableNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //API connection
        //Replace the ip address with ip address of your device
        //Replace "app.run" in app.py with "app.run(host='0.0.0.0', port=5000)"

        String URL = "http://192.168.2.91:5000/api/get_table_info";

        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Rest Response: ", response.toString());
                        try {
                            showResponse(response);
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

    private void showResponse(JSONObject jsonResponse) throws JSONException {
        System.out.println("JSONRESPONSE: " + jsonResponse);
        String x = jsonResponse.toString();
        outputText.setText(x);

    }
    /*
    private void getTables() {


        //API connection
        //Replace the ip address with ip address of your device
        //Replace "app.run" in app.py with "app.run(host='0.0.0.0', port=5000)"

        String URL = "http://192.168.2.91:5000/api/get_current_orders";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Rest Response: ", response.toString());
                        try {
                            //response.getString("orders");
                            JSONObject responseObject = new JSONObject(response.getString("orders"));
                            JSONArray resultsArray = responseObject.getJSONArray("");
                            System.out.println("ResultsArray: " + resultsArray);
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
    */

}

