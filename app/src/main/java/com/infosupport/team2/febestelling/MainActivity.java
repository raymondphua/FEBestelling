package com.infosupport.team2.febestelling;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.infosupport.team2.febestelling.adapter.ListOrderAdapter;
import com.infosupport.team2.febestelling.resource.TestData;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.infosupport.team2.febestelling.util.AppSingleton;
import com.infosupport.team2.febestelling.util.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    ProgressDialog progressDialog;
    private static final String TAG = "MainActivity";
    ListView listView;
    private String ORDER_URL = "http://10.0.3.2:11130/orderservice/statuses";
    private String AUTHENTICATION_URL = "http://10.0.3.2:11150/oauth/token?grant_type=password&username=pieter@hotmail.com&password=henkie&client_id=kantilever&client_secret=kantiSecret";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(this);

        authenticateApp(AUTHENTICATION_URL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }

    public void authenticateApp(String url){
        String REQUEST_TAG = "com.infosupport.team2.authenticationReq";
        progressDialog.setMessage("Laden...");
        progressDialog.show();

        JSONObject jsonObject = new JSONObject();
        final String requestBody = jsonObject.toString();
        String result = "";

        StringRequest putRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            AppSingleton.getInstance(getApplicationContext()).token = responseObject.getString("access_token");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        statusRequest(ORDER_URL);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.response", error.getMessage());
                        progressDialog.hide();
                    }
                })
        {
            @Override
            public byte[] getBody() {
                return requestBody.getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(putRequest, REQUEST_TAG);
    }

    public void statusRequest(String url) {
        String REQUEST_TAG = "com.infosupport.team2.statusRequestList";

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
                            button.setHeight(300);
                            button.setText(i);
                            button.setTextSize(50);

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
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization","Bearer " + AppSingleton.getInstance(getApplicationContext()).token);

                return map;
            }

        };

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(orderRequestList, REQUEST_TAG);
    }
}
