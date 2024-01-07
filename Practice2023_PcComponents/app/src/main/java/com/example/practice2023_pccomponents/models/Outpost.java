package com.example.practice2023_pccomponents.models;

public class Outpost {
    public int Id;
    public String Address;
    public Outpost(int id, String address)
    {
        this.Id = id;
        this.Address = address;
    }

    @Override
    public String toString() {
        return Address;
    }
}
