package com.infosupport.team2.febestelling.model;

import android.location.Address;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by paisanrietbroek on 16/01/2017.
 */

public class Order implements Serializable{

    private String id;
    private double shoppingFee;
    private double totalPrice;
    private Customer customer;
    private List<Product> orderedProducts;
    private Address deliveryAddress;
    private String status;
    private String date;

    public Order() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Product> getOrderedProducts() {
        return orderedProducts;
    }

    public void setOrderedProducts(List<Product> orderedProducts) {
        this.orderedProducts = orderedProducts;
    }

    public double getShoppingFee() {
        return shoppingFee;
    }

    public void setShoppingFee(double shoppingFee) {
        this.shoppingFee = shoppingFee;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
