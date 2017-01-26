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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public void setData(List<Order> objects) {
        this.orderList = new ArrayList<Order>(objects);
        this.origOrderList = new ArrayList<Order>(objects);
    }

    public ListOrderAdapter(Context context, int resource) {
        super(context, resource);
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
            TextView orderKey = (TextView) view.findViewById(R.id.order_key);
            TextView customerName = (TextView) view.findViewById(R.id.customer_name);
            TextView orderDate = (TextView) view.findViewById(R.id.orderDatum);

            if (orderKey != null) {
                orderKey.setText(item.getOrderKey());
            }
            if (customerName != null) {
                customerName.setText(item.getCustomer().getName());
            }

            if (orderDate != null) {
                orderDate.setText((CharSequence) item.getDate());
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

                        if (isMatch(o, constraint)) {
                            System.out.println("key: " + o.getOrderKey());
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
                    orderList = (ArrayList<Order>)results.values;
                } else {
                    orderList = (ArrayList<Order>)results.values;
                    notifyDataSetChanged();
                }
            }
        };

        return filter;
    }

    public void refreshEvents(List<Order> orders) {
        this.orderList.clear();
        this.orderList.addAll(orders);
        File cacheDir = getContext().getCacheDir();
        cacheDir.delete();
        notifyDataSetChanged();
    }

    public Boolean isMatch(Order order, CharSequence constraint) {
        Pattern p = Pattern.compile(".*" + constraint.toString()  + ".*");
        Matcher m = p.matcher(order.getOrderKey());
        return m.matches();
    }
}
