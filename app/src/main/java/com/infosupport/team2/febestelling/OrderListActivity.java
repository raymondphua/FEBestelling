package com.infosupport.team2.febestelling;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.infosupport.team2.febestelling.adapter.ListOrderAdapter;
import com.infosupport.team2.febestelling.model.Order;
import com.infosupport.team2.febestelling.util.AppSingleton;
import com.infosupport.team2.febestelling.util.JsonUtils;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by paisanrietbroek on 16/01/2017.
 */

public class OrderListActivity extends ListActivity {

    TextView statusLabel, statusState;

    ProgressDialog progressDialog;
    private static final String TAG = "OrderListActivity";
    ListView listView;
    EditText searchField;
    private String ORDER_URL = "http://10.0.3.2:11130/orderservice/orders?status=";
    ListOrderAdapter listOrderAdapter;
    private String status;
    List<Order> orders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_list);

        searchField = (EditText) findViewById(R.id.search_field);
        progressDialog = new ProgressDialog(this);

        Intent intent = getIntent();
        status = intent.getStringExtra("status");
        System.out.println("Status: " + status);

//        orderRequest(ORDER_URL + status);

        statusLabel = (TextView) findViewById(R.id.statusLabel);
        statusState = (TextView) findViewById(R.id.statusStatus);
        statusState.setText(status);
    }

    @Override
    protected void onResume() {
        super.onResume();

        orderRequest(ORDER_URL + status);
    }

    @Override
    protected void onDestroy() {
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


                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                Intent intent1 = new Intent(view.getContext(), OrderDetailActivity.class);
                                // TODO: geef order nummer mee aan nieuwe activity om de producten te kunnen ophalen
                                Order item = (Order) parent.getAdapter().getItem(position);
                                intent1.putExtra("orderId", item.getId());
                                intent1.putExtra("customerName", item.getCustomer().getName());
                                intent1.putExtra("status", status);
                                startActivity(intent1);
                            }
                        });

                        orders = JsonUtils.parseOrderResponse(response.toString());
                        listOrderAdapter = new ListOrderAdapter(getApplicationContext(), R.layout.order_item, orders);
                        listView.setAdapter(listOrderAdapter);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listOrderAdapter.refreshEvents(orders);
                            }
                        });

                        // filtering
                        listView.setTextFilterEnabled(true);
                        searchField.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                if (count < before) {
                                    listOrderAdapter.resetData();
                                }
                                listOrderAdapter.getFilter().filter(s.toString());
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });

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

