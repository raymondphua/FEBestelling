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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.order_list);

        searchField = (EditText) findViewById(R.id.search_field);
        progressDialog = new ProgressDialog(this);

        Intent intent = getIntent();
        status = intent.getStringExtra("status");
        System.out.println("Status: " + status);

        setTitle("Kantilever - Status: " + status);

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
                        LayoutInflater li = LayoutInflater.from(OrderListActivity.this);

                        listView = (ListView) findViewById(android.R.id.list);


                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                Intent intent1 = new Intent(view.getContext(), OrderDetailActivity.class);

                                Order item = (Order) parent.getAdapter().getItem(position);
                                intent1.putExtra("orderId", item.getId());
                                intent1.putExtra("orderKey", item.getOrderKey());
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
                try {
                    if (error.networkResponse == null || error.networkResponse.statusCode == 500) {
                        toastMessage("Momenteel kan het systeem de bestellingen niet ophalen.");
                        progressDialog.hide();
                    } else if (error.networkResponse.statusCode == 403) {
                        toastMessage("U heeft niet de juiste rechten.");
                        progressDialog.hide();
                    } else if (error.networkResponse.statusCode == 401){
                        toastMessage("uw inlog gegevens kloppen niet.");
                        progressDialog.hide();
                        goToLogin();
                    }
                } catch (Exception e) {
                    Log.w(e.getMessage(), e);
                    progressDialog.hide();
                }
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

    public void toastMessage(String txt) {
        Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();
    }

    public void goToLogin() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
}

