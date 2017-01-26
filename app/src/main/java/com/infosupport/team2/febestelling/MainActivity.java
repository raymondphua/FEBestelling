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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.infosupport.team2.febestelling.util.AppSingleton;
import com.infosupport.team2.febestelling.util.JsonUtils;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    ProgressDialog progressDialog;
    private static final String TAG = "MainActivity";
    ListView listView;
    private String ORDER_URL = "http://10.0.3.2:11130/orderservice/statuses";
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(this);
        statusRequest(ORDER_URL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }

    public void statusRequest(String url) {
        String REQUEST_TAG = "com.infosupport.team2.statusRequestList";

        JsonArrayRequest orderRequestList = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        toastMessage("Inloggen gelukt!");

                        LayoutInflater li = LayoutInflater.from(MainActivity.this);

                        listView = (ListView) findViewById(android.R.id.list);

                        List<String> statuses = JsonUtils.parseStatusResponse(response.toString());

                        linearLayout = (LinearLayout) findViewById(R.id.main_view);

                        addButtons(statuses);

                        progressDialog.hide();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    if (error.networkResponse == null || error.networkResponse.statusCode == 500) {
                        toastMessage("Het systeem is momenteel onbereikbaar.");
                        progressDialog.hide();
                    } else if (error.networkResponse.statusCode == 403) {
                        toastMessage("U heeft niet de juiste rechten.");
                        progressDialog.hide();
                    } else if (error.networkResponse.statusCode == 401) {
                        toastMessage("U dient opnieuw in te loggen.");
                        progressDialog.hide();
                    }
                    goToLogin();
                } catch (Exception e) {
                    Log.w(e.getMessage(), e);
                    progressDialog.hide();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization", "Bearer " + AppSingleton.getInstance(getApplicationContext()).token);

                return map;
            }

        };

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(orderRequestList, REQUEST_TAG);
    }

    public void addButtons(List<String> stringArrayList) {
        for (final String i : stringArrayList) {

            switch (i) {
                case "BESTELD":
                    addButton("Bestelling geplaatst", i);
                    break;
                case "INGEPAKT":
                    addButton("Bestelling ingepakt", i);
                    break;
                case "IN_BEHANDELING":
                    addButton("Bestelling in behandeling", i);
                    break;
                case "VERZONDEN":
                    addButton("Bestelling verzonden", i);
                    break;
                default:
                    break;
            }
        }
    }

    public void toastMessage(String txt) {
        Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();
    }

    public void goToLogin() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }


    public void addButton(String viewStatus, final String serverStatus) {
        Button button = new Button(MainActivity.this);
        button.setHeight(300);
        button.setText(viewStatus);
        button.setTextSize(24);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), OrderListActivity.class);
                intent.putExtra("status", serverStatus);
                startActivity(intent);
            }
        });
        linearLayout.addView(button);
    }

}
