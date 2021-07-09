package com.example.recyclerview.model;

import java.util.Random;

public class People {

    private int id;
    private String name;
    private int age;

    private final Random randomPosition = new Random();
    private final String[] names = {
            "Alan", "Arthur", "Nicolas", "Angela", "Brenda", "Liz",
            "Roberto", "Juliano", "Roberta", "Juliana", "Marisa", "Maria"
    };
    private final int[] ages = {32, 54, 65, 84, 41, 6, 15, 22, 9, 50, 98, 103};

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getAge() {
        return age;
    }

    // Construtor que recebe todos os Dados
    public People(int id, String name, int age){
        this.id = id;
        this.age = age;
        this.name = name;
    }

    // Contrutor que recebe ID e gera Dados Aleatorios
    public People(int id){
        this.id = id;
        this.name = names[randomPosition.nextInt(11)];
        this.age = ages[randomPosition.nextInt(11)];
    }

}
