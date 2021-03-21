package com.example.recyclerview;

public class People {
    private String name;
    private int age ;

    public String getNome() {
        return name;
    }
    public int getAge() {
        return age;
    }

    public People(){}
    public People(String name, int age){
        this.age = age;
        this.name = name;
    };
}
