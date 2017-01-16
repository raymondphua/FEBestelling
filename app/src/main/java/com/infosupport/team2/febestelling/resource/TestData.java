package com.infosupport.team2.febestelling.resource;

import com.infosupport.team2.febestelling.model.Customer;
import com.infosupport.team2.febestelling.model.Order;

import java.util.ArrayList;

/**
 * Created by paisanrietbroek on 16/01/2017.
 */

public class TestData {

    public static ArrayList<Order> getData() {
        ArrayList<Order> orderArrayList = new ArrayList<>();

        Order order = new Order();
        order.setId("135");
        Customer customer = new Customer();
        customer.setName("Jan");
        order.setCustomer(customer);

        Order order2 = new Order();
        order2.setId("246");
        Customer customer2 = new Customer();
        customer2.setName("Klaas");
        order2.setCustomer(customer2);

        Order order3 = new Order();
        order3.setId("531");
        Customer customer3 = new Customer();
        customer3.setName("Piet");
        order3.setCustomer(customer3);

        orderArrayList.add(order);
        orderArrayList.add(order2);
        orderArrayList.add(order3);

        return orderArrayList;
    }
}
