package com.infosupport.team2.febestelling;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.infosupport.team2.febestelling.adapter.ListOrderAdapter;
import com.infosupport.team2.febestelling.model.Order;
import com.infosupport.team2.febestelling.util.AppSingleton;
import com.infosupport.team2.febestelling.util.JsonUtils;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by paisanrietbroek on 16/01/2017.
 */

public class OrderListActivity extends ListActivity {

    ProgressDialog progressDialog;
    private static final String TAG = "OrderListActivity";
    ListView listView;
    private String ORDER_URL = "http://10.0.2.2:11130/orderservice/orders?status=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_list);
        progressDialog = new ProgressDialog(this);

        Intent intent = getIntent();
        String status = intent.getStringExtra("status");
        System.out.println("Status: " + status);

        orderRequest(ORDER_URL + status);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        progressDialog.dismiss();
    }

    public void orderRequest(String url) {
        String REQUEST_TAG = "com.infosupport.team2.orderlistRequest";
        progressDialog.setMessage("Laden...");
        progressDialog.show();

        JsonArrayRequest orderRequestList = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        LayoutInflater li = LayoutInflater.from(OrderListActivity.this);

                        listView = (ListView) findViewById(android.R.id.list);

        ListOrderAdapter listOrderAdapter = new ListOrderAdapter(this, R.layout.order_item, TestData.getData());
        listView.setAdapter(listOrderAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent1 = new Intent(view.getContext(), OrderDetailActivity.class);
                startActivity(intent1);
            }
        });
                        List<Order> orders = JsonUtils.parseOrderResponse(response.toString());
                        ListOrderAdapter listOrderAdapter = new ListOrderAdapter(getApplicationContext(), R.layout.order_item, orders);
                        listView.setAdapter(listOrderAdapter);

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
