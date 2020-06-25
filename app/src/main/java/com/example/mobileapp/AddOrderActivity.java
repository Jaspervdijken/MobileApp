package com.example.mobileapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class AddOrderActivity extends AppCompatActivity {
    Spinner spinner;
    LinearLayout linearLayout;
    Button button;

    ArrayList<Integer> list = new ArrayList<>();
    ArrayList<String> recipes = new ArrayList<>();
    ArrayList<EditText> editTexts =  new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);

        spinner = (Spinner) findViewById(R.id.spinner);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Log.e("onClick", String.valueOf(editTexts.size()));
                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                //jsonObject.put("orders", "")

                for(int i = 0; i < editTexts.size(); i++) {
                    JSONObject json = new JSONObject();
                    String amount = editTexts.get(i).getText().toString();
                    Log.e("onClick", i + " " + amount);

                    if(amount.equals("")) {
                        try {
                            json.put("itemId", i);
                            json.put("amount", 0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            json.put("itemId", i);
                            json.put("amount", Integer.parseInt(amount));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    jsonArray.put(json);
                }
                try {
                    jsonObject.put("orders", (Object) jsonArray);
                    jsonObject.put("table", spinner.getSelectedItem().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String jsonString = jsonObject.toString();
                String jsonFormattedString = jsonString.replaceAll("\\\\", "");

                try {
                    JSONObject json = new JSONObject(jsonFormattedString);
                    sendOrderToServer(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        this.fetchRecipes();
        this.fetchTables();
    }

    private void initTextView() {

        if(recipes.size() == 0) {
            this.fetchRecipes();
        }

        for(String menuItem : recipes) {
            TextView textView = new TextView(this);
            textView.setText(menuItem);
            textView.setTextSize(25);

            EditText editText = new EditText(this);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);

            editTexts.add(editText);

            linearLayout.addView(textView);
            linearLayout.addView(editText);
        }
    }

    private void initSpinner() {

        ArrayAdapter<Integer> adapter =
                new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void fetchTables() {
        String URL = "http://192.168.178.48:5000/api/get_all_tables";

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
                                list.add((Integer) object.getInt("tableNumber"));
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

    private void fetchRecipes() {
        String URL = "http://192.168.178.48:5000/api/get_all_products";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray resp = response.getJSONArray("products");

                            for(int i = 0; i < resp.length(); i++) {
                                JSONObject object = resp.getJSONObject(i);
                                recipes.add(object.getString("name"));
                                //recipes.add("\n");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        initTextView();
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

    private void sendOrderToServer(JSONObject order) {
        Log.e("json ", order.toString());

        String URL = "http://192.168.178.48:5000/api/add_new_order";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                order,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response", response.toString());
                        Toast.makeText(getApplicationContext(), "Order sent", Toast.LENGTH_LONG).show();
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