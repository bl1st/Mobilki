package com.example.practice2023_pccomponents;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.practice2023_pccomponents.activities.MainActivity;
import com.example.practice2023_pccomponents.models.Product;
import com.example.practice2023_pccomponents.models.ProductCharacteristic;
import com.example.practice2023_pccomponents.services.OrderContainer;
import com.example.practice2023_pccomponents.services.Request;

import org.json.JSONArray;
import org.json.JSONObject;


public class ProductFragment extends Fragment {
    Product product;
    ArrayAdapter<ProductCharacteristic> characteristics;
    ListView lv_chars;
    CatalogueFragment parent;
    OrderContainer orderContainer;
    ProductFragment self;

    public ProductFragment(CatalogueFragment parent, Product p) {
        this.parent = parent;
        this.product = p;
        this.self = this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MainActivity act = (MainActivity)getActivity();
        act.setTitle("Информация о товаре");
        this.orderContainer =  act.orderContainter;
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_product, container, false);
        lv_chars = v.findViewById(R.id.lv_characteristics);
        LoadProductCharacteristics();

        ImageView iv = v.findViewById(R.id.op_Image);
        iv.setImageBitmap(product.ProductImage);
        TextView p_name = v.findViewById(R.id.product_Name);
        p_name.setText(product.Name);
        TextView p_cat = v.findViewById(R.id.product_Category);
        p_cat.setText("Категория:" + product.CategoryName);
        TextView p_cost = v.findViewById(R.id.product_Cost);
        p_cost.setText(String.valueOf(product.Cost) + " руб.");
        TextView p_costWithDiscount = v.findViewById(R.id.product_CostWithDisount);

        if (product.Cost != product.CostWithDiscount) {
            p_cost.setPaintFlags(p_cost.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            p_costWithDiscount.setText(product.CostWithDiscount + " руб.");
        }
        else {
            p_costWithDiscount.setVisibility(View.INVISIBLE);
        }
        Button btn_back = v.findViewById(R.id.btn_goBack);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.hostFragment,parent)
                        .addToBackStack("tag").commit();
                Activity parent = getActivity();

            }
        });

        Button btn_addToCart = v.findViewById(R.id.btn_AddToCart);
        btn_addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderContainer.AddProduct(product);
                Toast.makeText(self.getContext(), "Добавлено в корзину", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    private void LoadProductCharacteristics() {
        Request r = new Request (this.getActivity()){
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(String res, Context ctx) throws Exception
            {
                characteristics = new ArrayAdapter<ProductCharacteristic>(ctx, R.layout.support_simple_spinner_dropdown_item);
                JSONArray arr = new JSONArray(res);
                for(int i = 0; i < arr.length(); i++)
                {
                    JSONObject obj = arr.getJSONObject(i);
                    int id = obj.getInt("id");
                    int charId = obj.getInt("characteristicId");
                    String charName = obj.getString("characteristicName");
                    charName = charName.trim();
                    String charVal = obj.getString("characteristicValue");
                    String unit = obj.getString("unit");
                    characteristics.add(new ProductCharacteristic(id, charId, charName, charVal, unit));
                }
                lv_chars.setAdapter(characteristics);
            }

            @Override
            public void onFail(Activity ctx)
            {

            }
        };
        String link = "/api/Products/get_product_characteristics?productId=" + String.valueOf(product.Id);
        r.sendHttpGet(link);


    }
}