package com.example.lihao.a04homework;

/**
 * Created by lihao on 2016/1/17.
 */
public class Person {



    public Person(String name,String phone,int id){
        setName(name);
        setPhone(phone);
        this.id = id;
    }

    private String name;

    private  String phone;

    private int id;

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

    public int getId() {
        return id;
    }
}
