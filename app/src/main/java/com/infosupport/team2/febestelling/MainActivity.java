package com.infosupport.team2.febestelling;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.infosupport.team2.febestelling.adapter.ListOrderAdapter;
import com.infosupport.team2.febestelling.resource.TestData;

public class MainActivity extends ListActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(android.R.id.list);

        ListOrderAdapter listOrderAdapter = new ListOrderAdapter(this, R.layout.order_item, TestData.getData());

        listView.setAdapter(listOrderAdapter);
    }
}
