package com.infosupport.team2.febestelling.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import com.infosupport.team2.febestelling.OrderListActivity;
import com.infosupport.team2.febestelling.R;
import com.infosupport.team2.febestelling.model.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.R.attr.filter;
import static android.R.attr.focusable;
import static android.R.attr.id;

/**
 * Created by paisanrietbroek on 16/01/2017.
 */

public class ListOrderAdapter extends ArrayAdapter<Order> implements Filterable{

    private ArrayList<Order> orderList;
    private ArrayList<Order> origOrderList;

    public ListOrderAdapter(Context context, int resource, List<Order> objects) {
        super(context, resource, objects);
        this.orderList = new ArrayList<Order>(objects);
        this.origOrderList = new ArrayList<Order>(objects);
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

    @Override
    public int getCount() {
        return this.orderList.size();
    }

    @Override
    public Order getItem(int position) {
        return this.orderList.get(position);
    }

    public void resetData() {
        orderList = origOrderList;
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();

                if (constraint != null || constraint.toString().length() > 0) {
                    ArrayList<Order> filteredOrderList = new ArrayList<>();

                    for (Order o : origOrderList) {
                        boolean equals = o.getId().equals(constraint.toString());
                        if (equals) {
                            System.out.println("id: " + o.getId());
                            filteredOrderList.add(o);
                        }
                        results.values = filteredOrderList;
                        results.count = filteredOrderList.size();
                    }
                } else {
                        synchronized (this) {
                            results.values = origOrderList;
                            results.count = origOrderList.size();
                        }
                    }
                return results;
                }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (results.count == 0) {
                    notifyDataSetInvalidated();
                } else {
                    orderList = (ArrayList<Order>)results.values;
                    notifyDataSetChanged();
                }
            }
        };

        return filter;
    }
}
