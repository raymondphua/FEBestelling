package com.infosupport.team2.febestelling;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.infosupport.team2.febestelling.adapter.ListProductAdapter;
import com.infosupport.team2.febestelling.model.Product;
import com.infosupport.team2.febestelling.util.AppSingleton;
import com.infosupport.team2.febestelling.util.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by paisanrietbroek on 16/01/2017.
 */

public class OrderDetailActivity extends Activity {

    private static final String TAG = "OrderDetailActivity";
    private static final String ORDER_URL =   "http://10.0.3.2:11130/orderservice/orders/";

    ProgressDialog progressDialog;

    private static final String PACK_URL = "";

    private ListView listView;
    private Button packBtn;
    private TextView orderId, customerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details);

        progressDialog = new ProgressDialog(this);

        listView = (ListView)findViewById(R.id.order_details_listview_products);
        orderId = (TextView) findViewById(R.id.order_details_order_id);
        customerName = (TextView) findViewById(R.id.order_details_customer_name);
        packBtn = (Button) findViewById(R.id.order_details_btn_ingepakt);


        // TODO: order nummer opvangen en op basis hiervan de producten ervan ophalen
        Intent intent = getIntent();
        orderId.setText(intent.getStringExtra("orderId"));
        customerName.setText(intent.getStringExtra("customerName"));
        setProductList(ORDER_URL + orderId.getText() + "/products");

        String status = getIntent().getStringExtra("status");

        if (status.equals("AFGELEVERD")) {
            packBtn.setVisibility(View.GONE);
        }
            packBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        changeStatus((String) orderId.getText());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }


    public void setProductList(String url) {
        String REQUEST_TAG = "com.infosupport.team2.productlistRequest";

        JsonArrayRequest productListRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<Product> products = JsonUtils.parseProductsResponse(response.toString());

                ListProductAdapter listProductAdapter =
                        new ListProductAdapter(getApplicationContext(), R.layout.product_item, products);
                listView.setAdapter(listProductAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    if (error.networkResponse == null || error.networkResponse.statusCode == 500) {
                        toastMessage("Het systeem is momenteel onbereikbaar.");
                        progressDialog.hide();
                    } else if (error.networkResponse.statusCode == 401){
                        toastMessage("U dient opnieuw in te loggen.");
                        progressDialog.hide();
                    }
                    goToLogin();
                    Log.d("Error.response", error.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
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

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(productListRequest, REQUEST_TAG);
    }

    public void changeStatus(final String orderId) throws JSONException {
        String url = ORDER_URL + orderId;
        String REQUEST_TAG = "com.infosupport.team2.putRequest";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", "INGEPAKT");
        final String requestBody = jsonObject.toString();

        StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        Toast.makeText(OrderDetailActivity.this, "ORDER " + orderId + " ingepakt", Toast.LENGTH_LONG).show();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            if (error.networkResponse == null || error.networkResponse.statusCode == 500) {
                                toastMessage("Het systeem is momenteel onbereikbaar.");
                                progressDialog.hide();
                            } else if (error.networkResponse.statusCode == 401){
                                toastMessage("U dient opnieuw in te loggen.");
                                progressDialog.hide();
                            }
                            goToLogin();
                            Log.d("Error.response", error.getMessage());
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressDialog.hide();
                        }
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

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization","Bearer " + AppSingleton.getInstance(getApplicationContext()).token);

                return map;
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(putRequest, REQUEST_TAG);
    }
    public void toastMessage(String txt) {
        Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();
    }

    public void goToLogin() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
}
