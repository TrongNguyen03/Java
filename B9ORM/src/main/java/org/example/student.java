package org.example;


public class student {
    private int id;
    private String name;

    public student() {}

    public student(String name) {
        this.name = name;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

