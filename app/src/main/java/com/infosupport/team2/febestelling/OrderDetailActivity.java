package com.infosupport.team2.febestelling;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.infosupport.team2.febestelling.adapter.ListProductAdapter;
import com.infosupport.team2.febestelling.model.Product;
import com.infosupport.team2.febestelling.util.AppSingleton;
import com.infosupport.team2.febestelling.util.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;


/**
 * Created by paisanrietbroek on 16/01/2017.
 */

public class OrderDetailActivity extends Activity {

    private static final String TAG = "OrderDetailActivity";
    private static final String PRODUCT_URL =   "http://10.0.3.2:11130/orderservice/orders/";
    private static final String ORDER_URL =     "http://10.0.3.2:11130/orderprocessservice/orders/";

    private static final String PACK_URL = "";

    private ListView listView;
    private Button packBtn;
    private TextView orderId, customerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details);

        listView = (ListView)findViewById(R.id.order_details_listview_products);
        packBtn = (Button) findViewById(R.id.order_details_btn_ingepakt);
        orderId = (TextView) findViewById(R.id.order_details_order_id);
        customerName = (TextView) findViewById(R.id.order_details_customer_name);

        // TODO: order nummer opvangen en op basis hiervan de producten ervan ophalen
        Intent intent = getIntent();
        orderId.setText(intent.getStringExtra("orderId"));
        customerName.setText(intent.getStringExtra("customerName"));
        setProductList(PRODUCT_URL + orderId.getText() + "/products");

        packBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStatus((String) orderId.getText());
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
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
    });

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(productListRequest, REQUEST_TAG);
    }

    public void changeStatus(String orderId) {
        String url = ORDER_URL + orderId;
        String REQUEST_TAG = "com.infosupport.team2.putRequest";

        JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.response", error.getMessage());
                    }
                })
        {
            @Override
            public byte[] getBody() {
                return super.getBody();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(putRequest, REQUEST_TAG);
    }

}
