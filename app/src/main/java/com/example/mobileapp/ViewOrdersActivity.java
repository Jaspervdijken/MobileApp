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
import java.util.Map;

public class ViewOrdersActivity extends AppCompatActivity {

    private TextView outputText;
    private TextView priceField;
    private Spinner spinner;
    private static final String[] paths = {"1", "2", "3","4", "5", "6", "7", "8", "9","10"};
    private int selectedTable;
    private JSONArray orderArray;
    private JSONObject jsonObject;
    private Map<String, Integer> orderItems;
    private ArrayList<String> products;

    //ArrayList<Object> list = new ArrayList<Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);

        getProductNames();

        outputText = (TextView)findViewById(R.id.outputText);
        priceField = (TextView)findViewById(R.id.priceField);
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

    private void showResponse(JSONObject jsonResponse) throws JSONException {
        if (jsonResponse.get("price").equals(0)) {
            outputText.setText("No orders");
            priceField.setText("Total: € 0,-");
        } else {
            //Call method that gets the product names from api
            //getProductNames();

            String output = "";
            //System.out.println(products);

            //Ended here, trying to match amount with product name and display it, frames being skipped
            /*
            JSONArray one1 = jsonObject.getJSONArray("all_orders");
            for (int i = 0; i < one1.length(); i++) {
                //System.out.println("One: " + one1);
                JSONArray two2 = (JSONArray) one1.get(i);
                //System.out.println("Two: " + two2);
                JSONObject three = (JSONObject) two2.get(0);
                //System.out.println("Three: " + three);
                Object amount = three.get("amount");
                Object itemId = three.get("itemId");
                output += "Three: " + amount + ":" + itemId;
            }

            outputText.setText(output);

             */

            JSONArray arr = jsonObject.getJSONArray("all_orders");
            outputText.setText(arr.toString());
            priceField.setText("€ " + String.valueOf(jsonResponse.get("price")));

        }


    }
}

