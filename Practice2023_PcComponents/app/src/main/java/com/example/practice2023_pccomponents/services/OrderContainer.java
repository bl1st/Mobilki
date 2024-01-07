package com.example.practice2023_pccomponents.services;

import com.example.practice2023_pccomponents.models.OrderedProduct;
import com.example.practice2023_pccomponents.models.Product;

import java.util.ArrayList;
import java.util.List;

public class OrderContainer {
    private List<OrderedProduct> orderedProducts;

    public OrderContainer()
    {
        orderedProducts = new ArrayList<OrderedProduct>();
    }

    public List<OrderedProduct> GetOrderedProducts() {
        return orderedProducts;
    }

    public void AddProduct(Product p)
    {
        for (int i=0; i < orderedProducts.size(); i++)
        {
            OrderedProduct op = orderedProducts.get(i);
            if (op.Product.Id == p.Id)
            {
                op.Quantity++;
                return;
            }
        }
        OrderedProduct op = new OrderedProduct(p);
        orderedProducts.add(op);
    }

    public void AddQuantityOfProduct(int id)
    {
        for (int i = 0; i < orderedProducts.size(); i++)
        {
            OrderedProduct op = orderedProducts.get(i);
            if (op.Product.Id == id)
            {
                op.Quantity++;
                return;
            }
        }
    }

    public void RemoveProduct(int id)
    {
        for (int i = 0; i < orderedProducts.size(); i++)
        {
            OrderedProduct op = orderedProducts.get(i);
            if (op.Product.Id == id)
            {
               orderedProducts.remove(op);
               return;
            }
        }
    }

    public int Count() {
        return orderedProducts.size();
    }

    public OrderedProduct Get(int position) {
        return orderedProducts.get(position);
    }

    public double GetTotalCost()
    {
        double totalCost = 0;
        for (int i = 0; i < orderedProducts.size(); i++)
        {
            OrderedProduct op = orderedProducts.get(i);
            totalCost+= op.Product.CostWithDiscount * op.Quantity;
        }
        return totalCost;
    }

    public void IncreaseQuantity(int productId)
    {
        OrderedProduct op = GetOrderedProductById(productId);
        op.Quantity++;

    }
    public void DecreaseQuantity(int productId)
    {
        OrderedProduct op = GetOrderedProductById(productId);
        op.Quantity--;
        if (op.Quantity == 0)
            orderedProducts.remove(op);
    }


    private OrderedProduct GetOrderedProductById(int productId) {
        for (int i =0; i < orderedProducts.size(); i++)
        {
            OrderedProduct op = orderedProducts.get(i);
            if (op.Product.Id == productId)
                return op;
        }
        return null;
    }
}
