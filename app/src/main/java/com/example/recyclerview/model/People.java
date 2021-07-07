package com.example.recyclerview.model;

public class People {

    private int id;
    private String name;
    private int age;

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getAge() {
        return age;
    }

    public People(int id,String name, int age){
        this.id = id;
        this.age = age;
        this.name = name;
    }

}
