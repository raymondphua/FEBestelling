package com.infosupport.team2.febestelling.model;

import android.location.Address;

/**
 * Created by paisanrietbroek on 16/01/2017.
 */

public class Customer {
    private String id;
    private String name;
    private String email;
    private String phone;
    private Address address;

    public Customer() {
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
