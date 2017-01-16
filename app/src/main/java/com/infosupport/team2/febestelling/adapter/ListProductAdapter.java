package com.infosupport.team2.febestelling.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.infosupport.team2.febestelling.R;
import com.infosupport.team2.febestelling.model.Order;
import com.infosupport.team2.febestelling.model.Product;

import java.util.List;

/**
 * Created by paisanrietbroek on 16/01/2017.
 */

public class ListProductAdapter extends ArrayAdapter<Product> {

    public ListProductAdapter(Context context, int resource, List<Product> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater inflater;
            inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.product_item, null);
        }

        Product product = getItem(position);

        if (product != null) {
            ImageView productImage = (ImageView) view.findViewById(R.id.product_details_image);
            TextView productName = (TextView) view.findViewById(R.id.product_details_product_name);
            TextView productAmount = (TextView) view.findViewById(R.id.product_details_amount);

            // TODO: productImage needs to map here too

            // TODO: productAmount needs to map

            if (productName != null) {
                productName.setText(product.getName());
            }
        }

        return view;
    }
}
