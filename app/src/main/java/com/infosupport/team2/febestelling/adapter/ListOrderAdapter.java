package com.infosupport.team2.febestelling.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.infosupport.team2.febestelling.R;
import com.infosupport.team2.febestelling.model.Order;

import java.util.List;

import static android.R.attr.id;

/**
 * Created by paisanrietbroek on 16/01/2017.
 */

public class ListOrderAdapter extends ArrayAdapter<Order> {

    public ListOrderAdapter(Context context, int resource, List<Order> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater inflater;
            inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.order_item, null);
        }

        Order item = getItem(position);

        if (item != null) {
            TextView orderId = (TextView) view.findViewById(R.id.order_id);
            TextView customerName = (TextView) view.findViewById(R.id.customer_name);

            if (orderId != null) {
                orderId.setText(item.getId().toString());
            }
            if (customerName != null) {
                customerName.setText(item.getCustomer().getName());
            }
        }

        return view;
    }
}
