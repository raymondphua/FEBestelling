package com.infosupport.team2.febestelling;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.infosupport.team2.febestelling.util.AppSingleton;
import com.infosupport.team2.febestelling.util.JsonUtils;

import org.json.JSONArray;

import java.util.List;

public class MainActivity extends Activity {

    ProgressDialog progressDialog;
    private static final String TAG = "MainActivity";
    ListView listView;
    private String ORDER_URL = "http://10.0.2.2:11130/orderservice/statuses";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(this);
        statusRequest(ORDER_URL);
    }


    public void statusRequest(String url) {
        String REQUEST_TAG = "com.infosupport.team2.statusRequestList";
        progressDialog.setMessage("Laden...");
        progressDialog.show();

        JsonArrayRequest orderRequestList = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        LayoutInflater li = LayoutInflater.from(MainActivity.this);

                        listView = (ListView) findViewById(android.R.id.list);

                        List<String> statuses = JsonUtils.parseStatusResponse(response.toString());

                        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.main_view);

                        for (final String i: statuses) {

                            Button button = new Button(MainActivity.this);
                            button.setText(i);

                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(v.getContext(), OrderListActivity.class);
                                    intent.putExtra("status", i);
                                    startActivity(intent);
                                }
                            });

                            linearLayout.addView(button);
                        }

                        progressDialog.hide();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                progressDialog.hide();
            }
        });

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(orderRequestList, REQUEST_TAG);
    }
}
