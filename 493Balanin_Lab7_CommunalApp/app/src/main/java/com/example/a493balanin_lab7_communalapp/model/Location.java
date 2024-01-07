package com.example.a493balanin_lab7_communalapp.model;

public class Location {

    public int Id;
    public String Name;

    public Location(int id, String name)
    {
        this.Id = id;
        this.Name = name;
    }

    @Override
    public String toString() { return Name; }

}
