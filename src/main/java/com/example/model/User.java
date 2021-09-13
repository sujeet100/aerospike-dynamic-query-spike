package com.example.model;

import com.aerospike.mapper.annotations.AerospikeKey;
import com.aerospike.mapper.annotations.AerospikeRecord;

@AerospikeRecord(namespace = "test", set = "users")
public class User {
    @AerospikeKey
    private String id;
    private String name;
    private int age;
    private String city;
    private String country;
    private String mobile;

    public User(String id, String name, int age, String city, String country, String mobile) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.city = city;
        this.country = country;
        this.mobile = mobile;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
