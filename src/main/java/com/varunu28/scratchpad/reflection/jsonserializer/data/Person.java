package com.varunu28.scratchpad.reflection.jsonserializer.data;

public class Person {
    private final String name;
    private final boolean employed;
    private final int age;
    private final float salary;
    private final Address address;
    private final Company company;

    public Person(String name, boolean employed, int age, float salary, Address address, Company company) {
        this.name = name;
        this.employed = employed;
        this.age = age;
        this.salary = salary;
        this.address = address;
        this.company = company;
    }
}
