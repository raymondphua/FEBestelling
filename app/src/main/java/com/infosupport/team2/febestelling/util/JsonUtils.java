package com.infosupport.team2.febestelling.util;

import com.infosupport.team2.febestelling.model.Customer;
import com.infosupport.team2.febestelling.model.Order;
import com.infosupport.team2.febestelling.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.order;

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
                order.setCustomer(parseCustomerObject(result.getJSONObject("customer")));
                order.setDate(result.getString("orderDate"));

                orders.add(order);
            }

            return orders;
        } catch (JSONException e) {
            e.printStackTrace();
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

                products.add(product);
            }

            return products;

        } catch (JSONException e) {
            e.printStackTrace();
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
            e.printStackTrace();
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