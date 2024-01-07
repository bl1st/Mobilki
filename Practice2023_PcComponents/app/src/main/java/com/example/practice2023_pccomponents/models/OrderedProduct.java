package com.example.practice2023_pccomponents.models;

public class OrderedProduct {
    public Product Product;
    public int Quantity;

    public OrderedProduct(Product p)
    {
        this.Product = p;
        this.Quantity = 1;
    }
}
