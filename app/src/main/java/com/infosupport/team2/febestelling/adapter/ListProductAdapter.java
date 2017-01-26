package com.infosupport.team2.febestelling.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.infosupport.team2.febestelling.R;
import com.infosupport.team2.febestelling.model.Product;
import com.infosupport.team2.febestelling.util.AppSingleton;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by paisanrietbroek on 16/01/2017.
 */

public class ListProductAdapter extends ArrayAdapter<Product> {

    private int countCheckbox;
    private CheckBox checkBox;
    List<Product> productList;
    String status;

    public ListProductAdapter(Context context, int resource, List<Product> objects, String listStatus) {
        super(context, resource, objects);
        this.productList = objects;
        this.status = listStatus;
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
            final ImageView productImage = (ImageView) view.findViewById(R.id.product_details_image);
            TextView productName = (TextView) view.findViewById(R.id.product_details_product_name);
            TextView productQuantity = (TextView) view.findViewById(R.id.product_details_amount);
            TextView productKey = (TextView) view.findViewById(R.id.product_details_product_key);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);

            if (!status.equals("BESTELD")) {
                checkBox.setVisibility(View.INVISIBLE);
            }


            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        countCheckbox++;
                        System.out.println("counter: " + countCheckbox);
                    } else {
                        countCheckbox--;
                        System.out.println("counter: " + countCheckbox);
                    }
                }
            });

            if (productImage != null) {
                String REQUEST_TAG = "com.infosupport.team2.imageRequest";
                ImageRequest imageRequest = new ImageRequest(product.getImgUrl(), new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        productImage.setImageBitmap(response);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        productImage.setImageResource(R.mipmap.ic_launcher);
                    }
                });
                AppSingleton.getInstance(view.getContext()).addToRequestQueue(imageRequest, REQUEST_TAG);
            }

            if (productQuantity != null) {
                String quantity = String.valueOf(product.getQuantity());
                productQuantity.setText(quantity);
            }

            if (productName != null) {
                productName.setText(product.getName());
            }
            if (productKey != null) {
                productKey.setText(product.getProductKey());
            }
        }

        return view;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    public int getCountCheckbox() {
        return countCheckbox;
    }

    public void setCountCheckbox(int countCheckbox) {
        this.countCheckbox = countCheckbox;
    }
}
