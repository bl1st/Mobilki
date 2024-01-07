package com.example.practice2023_pccomponents.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.RequiresApi;

import android.util.Base64;

import com.example.practice2023_pccomponents.R;

public class Product {
    public int Id;
    public String Name;
    public String CategoryName;
    public double Cost;
    public double CostWithDiscount;
    public Bitmap ProductImage;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Product(Context ctx, int id, String name, String categoryName, double cost, double costWithDiscount, String base64Image)
    {
        this.Id = id;
        this.Name = name;
        this.CategoryName = categoryName;
        this.Cost = cost;
        this.CostWithDiscount = costWithDiscount;
        if (base64Image.equals("null"))
        {
            ProductImage = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.no_image);
        }
        else
        {
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            ProductImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }

    }
}