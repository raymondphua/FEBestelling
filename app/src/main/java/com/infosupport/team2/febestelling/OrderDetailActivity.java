package com.infosupport.team2.febestelling;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by paisanrietbroek on 16/01/2017.
 */

public class OrderDetailActivity extends Activity {

    private ListView listView;
    private Button button;
    private TextView orderId, customerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details);

        listView = (ListView)findViewById(R.id.order_details_listview_products);
        button = (Button) findViewById(R.id.order_details_btn_ingepakt);
        orderId = (TextView) findViewById(R.id.order_details_order_id);
        customerName = (TextView) findViewById(R.id.order_details_customer_name);


    }
}
