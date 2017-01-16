package com.infosupport.team2.febestelling.util;

import com.infosupport.team2.febestelling.model.Customer;
import com.infosupport.team2.febestelling.model.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robin on 16-1-2017.
 */

public class JsonUtils {
    public static List<Order> parseOrderResponse(String jsonStr){
        try
        {
            JSONArray jsonArray = new JSONArray(jsonStr);

            ArrayList<Order> orders = new ArrayList<Order>();

            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject result = jsonArray.getJSONObject(i);
                Order order = new Order();
                order.setId(result.getString("id"));
                order.setCustomer(parseCustomerObject(result.getJSONObject("customer")));

                orders.add(order);
            }

            return orders;
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private static Customer parseCustomerObject(JSONObject customerObject) throws JSONException {
        Customer customer = new Customer();

        customer.setId(customerObject.getString("id"));
        customer.setName(customerObject.getString("name"));

        return customer;
    }
}
