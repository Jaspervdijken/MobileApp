package com.example.mobileapp;

import androidx.appcompat.app.AppCompatActivity;

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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ViewOrdersActivity extends AppCompatActivity {

    private TextView outputText;
    private TextView priceField;
    private Spinner spinner;
    private String[] paths;
    private ArrayList<String> tables = new ArrayList<String>();
    private int selectedTable;
    private JSONObject jsonObject;
    private ArrayList<String> products;
    private HashMap<Object, Object> pairs = new HashMap<Object, Object>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);

        //Get products from API, sent by last activity
        products = getIntent().getStringArrayListExtra("PRODUCTS_FROM_API");

        this.getTables();

        outputText = (TextView)findViewById(R.id.outputText);
        priceField = (TextView)findViewById(R.id.priceField);

    }

    private void getTableInfo(int tableNumber) {

        //Creating a jsonBody with the tableNumber
        final JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("table_number", tableNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
                            jsonObject = response;
                            showResponse(jsonObject);
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

    private void getTables() {
        String URL = "http://192.168.2.91:5000/api/get_all_tables";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray resp = response.getJSONArray("tables");
                            for(int i = 0; i < resp.length(); i++) {
                                JSONObject object = resp.getJSONObject(i);
                                tables.add(String.valueOf(object.getInt("tableNumber")));
                            }

                            initSpinner();

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

    private void initSpinner() {
        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(ViewOrdersActivity.this,
                android.R.layout.simple_spinner_item,tables);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                pairs.clear();
                selectedTable = Integer.parseInt(tables.get(position));
                getTableInfo(selectedTable);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    private void showResponse(JSONObject jsonResponse) throws JSONException {
        if (jsonResponse.get("price").equals(0)) {
            outputText.setText("No orders");
            priceField.setText("Total: € 0,-");
        } else {
            //Call method that gets the product names from api
            JSONArray one1 = jsonObject.getJSONArray("all_orders");
            JSONArray two2 = (JSONArray) one1.get(0);
            //System.out.println("One: " + one1);
            for (int i = 0; i < two2.length(); i++) {
                JSONObject three = (JSONObject) two2.get(i);
                Object amount = three.get("amount");
                if (!amount.equals(0)) {
                    pairs.put(products.get((Integer) three.get("itemId")), amount);
                }
            }

            String output = "";
            Iterator it = pairs.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                output += pair.getValue() + " x " + pair.getKey() + "\n";
                it.remove();
            }
            //outputText.setText(pairs.toString());
            outputText.setText(output);
            priceField.setText("€ " + String.valueOf(jsonResponse.get("price")));

            //Working json part
            /*
            JSONArray arr = jsonObject.getJSONArray("all_orders");
            outputText.setText(arr.toString());
            priceField.setText("€ " + String.valueOf(jsonResponse.get("price")));
            */

        }

    }
}

