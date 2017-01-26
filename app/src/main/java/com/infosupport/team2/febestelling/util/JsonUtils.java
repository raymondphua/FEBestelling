package com.infosupport.team2.febestelling.util;

import android.util.Log;

import com.infosupport.team2.febestelling.model.Customer;
import com.infosupport.team2.febestelling.model.Order;
import com.infosupport.team2.febestelling.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robin on 16-1-2017.
 */

public class JsonUtils {
    public static List<Order> parseOrderResponse(String jsonStr) {
        try {
            JSONArray jsonArray = new JSONArray(jsonStr);

            ArrayList<Order> orders = new ArrayList<Order>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject result = jsonArray.getJSONObject(i);
                Order order = new Order();
                order.setId(result.getString("id"));

                if (!result.isNull("customer")) {
                    order.setCustomer(parseCustomerObject(result.getJSONObject("customer")));
                    order.setDate(result.getString("orderDate"));
                    order.setOrderKey(result.getString("orderKey"));
                    orders.add(order);
                }
            }

            return orders;
        } catch (JSONException e) {
            Log.w(e.getMessage(), e);
        }
        return null;
    }


    public static List<Product> parseProductsResponse(String jsonStr) {
        try {
            JSONArray jsonArray = new JSONArray(jsonStr);

            ArrayList<Product> products = new ArrayList<Product>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject result = jsonArray.getJSONObject(i);
                Product product = new Product();

                product.setImgUrl(result.getString("imgUrl"));
                product.setName(result.getString("name"));
                product.setQuantity(result.getInt("quantity"));
                product.setProductKey(result.getString("productKey"));

                products.add(product);
            }

            return products;

        } catch (JSONException e) {
            Log.w(e.getMessage(), e);
        }
        return null;
    }

    public static List<String> parseStatusResponse(String jsonstr) {
        List<String> statuses = new ArrayList<String>();
        try {
            JSONArray jsonArray = new JSONArray(jsonstr);
            for (int i = 0; i < jsonArray.length(); i++) {
                statuses.add(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            Log.w(e.getMessage(), e);
        }

        return statuses;
    }

    private static Customer parseCustomerObject(JSONObject customerObject) throws JSONException {
        Customer customer = new Customer();

        customer.setId(customerObject.getString("id"));
        customer.setName(customerObject.getString("name"));


        return customer;
    }
}
