package com.example.practice2023_pccomponents.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.practice2023_pccomponents.CartFragment;
import com.example.practice2023_pccomponents.R;
import com.example.practice2023_pccomponents.models.OrderedProduct;
import java.util.ArrayList;
import java.util.List;

public class OrderedProductAdapter extends ArrayAdapter<OrderedProduct> {
    private static final String TAG = "GraphArrayAdapter";
    private List<OrderedProduct> productsList;
    private String token = "";
    private OrderedProductAdapter self;
    private CartFragment fragment;


    static class ProductViewHolder {
        ImageView productImg;
        TextView productName;
        TextView productCategory;
        TextView costWithDiscount;
        Button btn_increase, btn_decrease;
        TextView productCount;
    }

    public OrderedProductAdapter(Context context, int textViewResourceId, CartFragment fragment, OrderContainer oc) {
        super(context, textViewResourceId);
        this.fragment = fragment;
        this.self = this;
       this.productsList = oc.GetOrderedProducts();
    }

    @Override
    public void add(OrderedProduct object) {
        productsList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.productsList.size();
    }

    @Override
    public OrderedProduct getItem(int index) {
        return this.productsList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ProductViewHolder viewHolder;
        OrderedProduct orderedProduct = getItem(position);
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.ordered_product, parent, false);
            viewHolder = new ProductViewHolder();
            viewHolder.productImg = row.findViewById(R.id.op_Image);
            viewHolder.productName = row.findViewById(R.id.op_product_Name);
            viewHolder.productCategory = row.findViewById(R.id.op_product_Category);
            viewHolder.costWithDiscount = row.findViewById(R.id.op_product_CostWithDisount);
            viewHolder.btn_decrease = row.findViewById(R.id.btn_Minus);
            viewHolder.btn_increase = row.findViewById(R.id.btn_Plus);
            viewHolder.productCount = row.findViewById(R.id.tv_Product_ProductCount);

            row.setTag(viewHolder);
        } else {
            viewHolder = (ProductViewHolder) row.getTag();
        }

        viewHolder.productImg.setImageBitmap(orderedProduct.Product.ProductImage);
        viewHolder.productName.setText(orderedProduct.Product.Name);
        viewHolder.productCategory.setText("Категория: " + orderedProduct.Product.CategoryName);
        viewHolder.costWithDiscount.setText("Цена: " + orderedProduct.Product.CostWithDiscount + " руб.");
        viewHolder.productCount.setText(orderedProduct.Quantity  + " шт.");

        viewHolder.btn_decrease.setTag(orderedProduct.Product.Id);
        viewHolder.btn_increase.setTag(orderedProduct.Product.Id);
        viewHolder.btn_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int productId = (int) view.getTag();
                OrderedProduct op = GetOrderedProductById(productId);
                op.Quantity++;
                self.notifyDataSetChanged();
                fragment.InvalidateView();
            }
        });
        viewHolder.btn_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int productId = (int) view.getTag();
                OrderedProduct op = GetOrderedProductById(productId);
                op.Quantity--;
                if (op.Quantity == 0)
                    productsList.remove(op);
                self.notifyDataSetChanged();
                fragment.InvalidateView();
            }
        });

        return row;
    }
    private OrderedProduct GetOrderedProductById(int productId) {
        for (int i =0; i < productsList.size(); i++)
        {
            OrderedProduct op = productsList.get(i);
            if (op.Product.Id == productId)
                return op;
        }
        return null;
    }


}