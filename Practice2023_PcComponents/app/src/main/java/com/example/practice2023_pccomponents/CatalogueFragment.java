package com.example.practice2023_pccomponents;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.practice2023_pccomponents.models.Category;
import com.example.practice2023_pccomponents.models.Product;
import com.example.practice2023_pccomponents.services.ProductsAdapter;
import com.example.practice2023_pccomponents.services.RecyclerItemClickListener;
import com.example.practice2023_pccomponents.services.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class CatalogueFragment extends Fragment {

    RecyclerView rv_products;
    int page = 1;
    List<Product> productList = new ArrayList<Product>();
    ProductsAdapter adp;
    List<Category> categories = new ArrayList<Category>();
    Spinner sp;
    ArrayAdapter sp_adp;
    CatalogueFragment myself;

    public CatalogueFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        categories.add(new Category(-1, "Все"));
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_catalogue, container, false);
        getActivity().setTitle("Каталог");
        this.rv_products = v.findViewById(R.id.rv_Products);
        rv_products.setLayoutManager(new GridLayoutManager(v.getContext(),2));
        LoadCategories();

        v.findViewById(R.id.btn_PreviousPage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPreviousPage_Click(v);
            }
        });
        v.findViewById(R.id.btn_NextPage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonNextPage_Click(v);
            }
        });
        sp = v.findViewById(R.id.spinner_Categories);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                LoadProducts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        myself = this;
      rv_products.addOnItemTouchListener(
              new RecyclerItemClickListener(getContext(), rv_products,
                      new RecyclerItemClickListener.OnItemClickListener() {
                  @Override public void onItemClick(View view, int position) {
                      Product p = adp.products.get(position);

                      FragmentManager manager = getFragmentManager();
                      FragmentTransaction transaction = manager.beginTransaction();
                      transaction.replace(R.id.hostFragment, new ProductFragment(myself, p));
                      transaction.commit();


                  }

                  @Override public void onLongItemClick(View view, int position) {
                      // do whatever
                  }
              })
      );
        return v;
    }



    public void LoadProducts()
    {
        Request r = new Request (this.getActivity()){
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(String res, Context ctx) throws Exception
            {
                JSONArray arr = new JSONArray(res);
                if (arr.length() == 0)
                {
                    Toast.makeText(ctx,"Следующей страницы нет",Toast.LENGTH_SHORT).show();
                    return;
                }
                productList.clear();
                for(int i=0; i < arr.length(); i++)
                {
                    JSONObject obj = arr.getJSONObject(i);
                    int id = obj.getInt("id");
                    String name = obj.getString("name");
                    String categoryName = obj.getString("categoryName");
                    Double cost = obj.getDouble("cost");
                    Double costWithDiscount = obj.getDouble("costWithDiscount");
                    String base64Image = obj.getString("base64Image");
                    Product p = new Product(ctx, id, name, categoryName, cost, costWithDiscount,base64Image);
                    productList.add(p);
                }

                adp = new ProductsAdapter(ctx, productList);
                rv_products.setAdapter(adp);

            }

            @Override
            public void onFail(Activity ctx)
            {
                Toast.makeText(ctx,"Товары не найдены", Toast.LENGTH_SHORT).show();
            }
        };
        String link = "/api/products?page=" + String.valueOf(page);
        int categoryId = ((Category)sp.getSelectedItem()).Id;
        if (categoryId != -1)
        {
            link += "&categoryId=" + String.valueOf(categoryId);
        }
        r.sendHttpGet(link);
    }


    private void LoadCategories()
    {
        Request r = new Request (this.getActivity()){
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(String res, Context ctx) throws Exception
            {
                categories.clear();
                categories.add(new Category(-1, "Все"));
                JSONArray arr = new JSONArray(res);
                for(int i=0; i < arr.length(); i++)
                {
                    JSONObject obj = arr.getJSONObject(i);
                    int id = obj.getInt("id");
                    String name = obj.getString("categoryName");
                    categories.add(new Category(id,name));
                }
                sp_adp = new ArrayAdapter(ctx, R.layout.support_simple_spinner_dropdown_item, categories);
                sp.setAdapter(sp_adp);
                sp.setSelection(0);
                LoadProducts();
            }

            @Override
            public void onFail(Activity ctx)
            {

            }
        };
        String link = "/api/Categories";
        r.sendHttpGet(link);
    }



    public void onButtonNextPage_Click(View v) {

        if (productList.size() == 0) {
            Toast.makeText(getActivity(), "Товаров не найдено",Toast.LENGTH_SHORT).show();
            return;
        }
        page++;
        LoadProducts();
    }
    public void onButtonPreviousPage_Click(View v) {

       if (page == 1)
       {
           Toast.makeText(getActivity(), "Вы уже на странице 1",Toast.LENGTH_SHORT).show();
           return;
       }
       page--;
       LoadProducts();
    }
}