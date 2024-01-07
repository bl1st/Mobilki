package com.example.practice2023_pccomponents.models;

public class Category {
    public int Id;
    public String Name;


    public Category(int id, String name)
    {
        this.Id = id;
        this.Name = name;
    }
    @Override
    public String toString() {
        return Name;
    }
}
