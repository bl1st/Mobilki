package com.example.practice2023_pccomponents;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.practice2023_pccomponents.activities.MainActivity;
import com.example.practice2023_pccomponents.models.OrderedProduct;
import com.example.practice2023_pccomponents.models.Outpost;
import com.example.practice2023_pccomponents.models.ProductCharacteristic;
import com.example.practice2023_pccomponents.services.OrderContainer;
import com.example.practice2023_pccomponents.services.OrderedProductAdapter;
import com.example.practice2023_pccomponents.services.ProductsAdapter;
import com.example.practice2023_pccomponents.services.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CartFragment extends Fragment {

   OrderContainer orderContainer;
   TextView tv_ProductCount, tv_TotalCost;
   Spinner sp;
   ArrayAdapter<Outpost> adp_outposts;
    OrderedProductAdapter adp_products;
    ListView orderedProducts;
    public CartFragment() {

    }

   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity parent = (MainActivity)getActivity();
        parent.setTitle("Корзина");
        this.orderContainer =  parent.orderContainter;
        adp_products = new OrderedProductAdapter(getContext(), 0,this,orderContainer);

        View v = inflater.inflate(R.layout.fragment_cart, container, false);
        orderedProducts = v.findViewById(R.id.lv_OrderedProducts);
        orderedProducts.setAdapter(adp_products);
        tv_TotalCost = v.findViewById(R.id.tv_TotalCost);
        tv_ProductCount = v.findViewById(R.id.tv_ProductCount);
        sp = v.findViewById(R.id.sp_Outposts);
        Button btn = v.findViewById(R.id.btn_FormOrder);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonFormOrder_Click(view);
            }
        });
        LoadOutposts();
        InvalidateView();
        
        return v;
    }

    private void LoadOutposts() {

        Request r = new Request (this.getActivity()){
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(String res, Context ctx) throws Exception
            {
                adp_outposts = new ArrayAdapter<Outpost>(getContext(), R.layout.support_simple_spinner_dropdown_item);
                JSONArray arr = new JSONArray(res);
                for(int i = 0; i < arr.length(); i++)
                {
                    JSONObject obj = arr.getJSONObject(i);
                    int id = obj.getInt("id");
                    String address = obj.getString("address");
                    adp_outposts.add(new Outpost(id, address));
                }
                sp.setAdapter(adp_outposts);
                sp.setSelection(0);
            }

            @Override
            public void onFail(Activity ctx)
            {

            }
        };
        String link = "/api/Outposts";
        r.sendHttpGet(link);
    }

    public void onButtonFormOrder_Click(View v)
    {
        JSONObject obj = new JSONObject();
        try {
            obj.put("outpostId", ((Outpost)sp.getSelectedItem()).Id);
        } catch (JSONException e) { e.printStackTrace(); }

        JSONArray arr = new JSONArray();
        for(int i =0; i < orderContainer.Count(); i++)
        {
            OrderedProduct op = orderContainer.Get(i);
            JSONObject arr_item = new JSONObject();
            try {
                arr_item.put("productId", op.Product.Id);
                arr_item.put("quantity",op.Quantity);
                arr.put(arr_item);
            } catch (JSONException e) { e.printStackTrace(); }
        }
        try {
            obj.put("orderedProducts",arr);
        } catch (JSONException e) { e.printStackTrace(); }


        Request r = new Request (getActivity()){
            @Override
            public void onSuccess(String res, Context ctx) throws Exception
            {
                JSONObject obj = new JSONObject(res);
                int recieveCode = obj.getInt("recieveCode");

                Toast.makeText(ctx, "Заказ сформирован\nКод получения заказа: " + recieveCode, Toast.LENGTH_SHORT).show();
                //Возвращает код получения заказа
            }
            @Override
            public void onFail(Activity ctx)
            {
                Log.e("Fail create_order","Well, gg");
                Toast.makeText((Context)ctx,"Что-то пошло не так, проверьте подключение к интернету",Toast.LENGTH_SHORT).show();
            }
        };
          r.sendHttpPost("/api/Orders/create_order",obj);
    }

    public void InvalidateView()
    {
        tv_ProductCount.setText("Кол-во позиций: " + orderContainer.Count() + " шт.");
        tv_TotalCost.setText("Общая сумма заказа: " + orderContainer.GetTotalCost() + " руб.");

    }
}