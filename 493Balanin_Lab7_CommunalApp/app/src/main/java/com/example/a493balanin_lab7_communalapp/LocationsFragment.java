package com.example.a493balanin_lab7_communalapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.a493balanin_lab7_communalapp.activities.MainActivity;
import com.example.a493balanin_lab7_communalapp.model.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class LocationsFragment extends Fragment {

    private String token;
    View mainView;
    ArrayAdapter<Location> locs;
    List<Location> list_locs;
    EditText et_FindLoc;
    ListView lv_Locs;

    public LocationsFragment(String token)
    {
        this.token = token;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_locations, container, false);
        mainView = view;
        et_FindLoc = view.findViewById(R.id.etLocName);
        InitFindLocByName(view);
        InitLvOptions(view);
        InitLvLocations(view);
        return view;
    }

    private void InitLvOptions(View view)
    {
        ListView lvOptions = view.findViewById(R.id.lvLocationsOptions);

        String[] opts = new String[] { "Add new location"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item, opts);
        lvOptions.setAdapter(adapter);

        FragmentManager mngr = getChildFragmentManager();

        lvOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              switch (i)
              {
                  case 0:
                      showAddLocationDialog();
                      break;
                  case 1:
                      //    mngr.beginTransaction().replace(R.id.action_container,new ListLocationsFragment(token)).commit();
                      break;
                  case 2:

                      break;
              }
          }
      });
    }

    private void InitLvLocations(View v)
    {
        lv_Locs= v.findViewById(R.id.lvLocations);
         list_locs = new ArrayList<Location>();

        locs = new ArrayAdapter<Location>(getContext(),R.layout.support_simple_spinner_dropdown_item, list_locs);
        lv_Locs.setAdapter(locs);
        refillAdapter();
    }

    private void refillAdapter()
    {

        Request r = new Request(getActivity())
        {
            @Override
            public void onSuccess(String res, Context ctx) throws Exception
            {
                list_locs.clear();
                JSONArray arr = new JSONArray(res);
                for(int i =0; i < arr.length();i++)
                {
                    JSONObject obj = arr.getJSONObject(i);
                    int id = obj.getInt("id2");
                    String name = obj.getString("name2");

                    Location loc = new Location(id, name);
                    list_locs.add(loc);

                }
                locs.notifyDataSetChanged();
            }
        };

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("key1", token);
        } catch (JSONException e) { }

        r.sendHttpPost("/rpc/get_locations",requestBody);

    }



    private void showAddLocationDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View v = getActivity().getLayoutInflater().inflate(R.layout.add_location, null);
        builder.setTitle("New location");
        builder.setView(v);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                Request r = new Request(getActivity())
                {
                    @Override
                    public void onSuccess(String res, Context ctx) throws Exception
                    {
                       //result
                        Toast.makeText(ctx,"Created new location", Toast.LENGTH_SHORT).show();
                        locs.notifyDataSetChanged();
                    }
                };

                EditText et = v.findViewById(R.id.etLocationName);
                String loc_name =et.getText().toString();

                JSONObject requestBody = new JSONObject();
                try {
                    requestBody.put("key1", token);
                    requestBody.put("name1", loc_name);
                } catch (JSONException e) { }

                r.sendHttpPost("/rpc/add_location",requestBody);
            }
        });
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    private void InitFindLocByName(View v)
    {
       et_FindLoc.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String loc_name = charSequence.toString();
               for (int j = 0; j < list_locs.size(); j++)
               {
                   Location l = list_locs.get(j);
                   if (l.Name.contains(loc_name)) {
                       lv_Locs.getChildAt(j).setVisibility(View.VISIBLE);
                       lv_Locs.getChildAt(j).setLayoutParams(new AbsListView.LayoutParams(-1,-2));
                   }
                   else {
                       lv_Locs.getChildAt(j).setVisibility(View.GONE);
                       lv_Locs.getChildAt(j).setLayoutParams(new AbsListView.LayoutParams(-1,10));
                   }
               }
               locs.notifyDataSetChanged();
           }

           @Override
           public void afterTextChanged(Editable editable) {

           }
       });
    }
    
    


}