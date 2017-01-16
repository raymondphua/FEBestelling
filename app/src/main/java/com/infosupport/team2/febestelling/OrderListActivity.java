package com.infosupport.team2.febestelling;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.infosupport.team2.febestelling.adapter.ListOrderAdapter;
import com.infosupport.team2.febestelling.resource.TestData;

/**
 * Created by paisanrietbroek on 16/01/2017.
 */

public class OrderListActivity extends ListActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_list);

        Intent intent = getIntent();
        String status = intent.getStringExtra("status");
        System.out.println("Status: " + status);

        // TODO: get list based on status


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
    }
}
