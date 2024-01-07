package com.example.practice2023_pccomponents.services;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.practice2023_pccomponents.R;
import com.example.practice2023_pccomponents.models.Product;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder>{
    private LayoutInflater inflater;
    public List<Product> products;

    public ProductsAdapter(Context context, List<Product> states) {
        this.products = states;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public ProductsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
////////////////////////////////////////////////////////////////////////////////////////////////
        View view = inflater.inflate(R.layout.product_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductsAdapter.ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.productImageView.setImageBitmap(product.ProductImage);
        holder.productNameView.setText(product.Name);
        holder.productCategoryView.setText("Категория: " + product.CategoryName);

        holder.costView.setText(product.Cost + "руб.");
        if (product.Cost != product.CostWithDiscount)
        {
            holder.costView.setPaintFlags(holder.costView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.costWithDiscountView.setText(product.CostWithDiscount + " руб.");
        }
        else
        {
            holder.costWithDiscountView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView productImageView;
        final TextView productNameView, productCategoryView, costView, costWithDiscountView;
        ViewHolder(View view){
            super(view);
            productImageView = view.findViewById(R.id.op_Image);
            productNameView = view.findViewById(R.id.product_Name);
            productCategoryView = view.findViewById(R.id.product_Category);
            costView = view.findViewById(R.id.product_Cost);
            costWithDiscountView = view.findViewById(R.id.product_CostWithDisount);
        }
    }
}
