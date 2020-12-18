package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.graphics.Color;
import android.widget.AdapterView;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddItems extends Fragment{
    SharedPreferences pref;
    Button add,remove;
    EditText itemname,itemprice;
    String item,price;
    String server_url_add = "http://192.168.1.7:8080/add_menu";
    String server_url_remove = "http://192.168.1.7:8080/remove_menu";
    String server_url_display = "http://192.168.1.7:8080/disp_menu";
    public Spinner spinner;
    public String removing_this_item;
    int[] integerflag={0};
    String[] itemnamedropdown;
    ArrayList items;
    ArrayAdapter<String> adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup)  inflater.inflate(R.layout.additems,container,false);
        pref = getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);
        Toast.makeText(getActivity().getApplicationContext(), pref.getString("MoodleID",null), Toast.LENGTH_LONG).show();
        Toast.makeText(getActivity().getApplicationContext(), "Welcome", Toast.LENGTH_SHORT).show();
        add = viewGroup.findViewById(R.id.addbutton);
        remove = viewGroup.findViewById(R.id.removebutton);
        itemname = viewGroup.findViewById(R.id.itemname);
        itemprice = viewGroup.findViewById(R.id.itemprice);
        spinner = (Spinner) viewGroup.findViewById(R.id.dropdown);
        items = new ArrayList<>();
        fetchitemdb();
        adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,items);
        spinner.setOnItemSelectedListener(listener);
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                item=itemname.getText().toString().trim();
                price=itemprice.getText().toString().trim();
                if (item.length()==0 || itemprice.length()==0){
                    Toast.makeText(getActivity().getApplicationContext(),"Please Fill All Details",Toast.LENGTH_SHORT).show();
                }
                else{
                    additemdb();
                }
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(items.size()==0){
                    Toast.makeText(getActivity().getApplicationContext(),"No items added",Toast.LENGTH_SHORT).show();
                }
                else {
                    removing_this_item = (String) spinner.getSelectedItem();
                    removeitemdb();
                }
            }
        });
        return viewGroup;
    }

    private void additemdb()
    {
        boolean flag=false;
        final String itemname = this.itemname.getText().toString().trim().toUpperCase();
        final int itemprice = Integer.parseInt(this.itemprice.getText().toString().trim());
        String item_and_price = itemname+" "+itemprice+" RS";
        for (int i = 0;i<items.size();i++){
            itemnamedropdown = items.get(i).toString().split(" ");
            if (itemname.equals(itemnamedropdown[0])){
                Log.i("itemnamearray",itemnamedropdown[0]);
                Log.i("itemname123",itemname);
                Toast.makeText(getActivity().getApplicationContext(),"Item already added",Toast.LENGTH_SHORT).show();
                flag=true;
                break;
            }
        }

        if (flag==false) {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
            final JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("item_name", itemname);
                jsonObject.put("price", itemprice);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String stringObject = jsonObject.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url_add, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    fetchitemdb();
                    Toast.makeText(getActivity().getApplicationContext(), "item added", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("volleyerror123", error.toString());
                    Toast.makeText(getActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return stringObject.getBytes("utf-8");
                    } catch (UnsupportedEncodingException em) {
                        return null;
                    }
                }
            };
            requestQueue.add(stringRequest);
        }
    }

    private void removeitemdb()
    {
        final String[] itemparts=removing_this_item.split(" ");
        final String item_name = itemparts[0];

        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getApplicationContext());
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("item_name",item_name);
        }catch(JSONException e){
            e.printStackTrace();
        }
        final String stringObject=jsonObject.toString();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, server_url_remove, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                items.remove(removing_this_item);
                spinner.setAdapter(adapter);
                Toast.makeText(getActivity().getApplicationContext(),"item removed",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("volleyerror123",error.toString());
                Toast.makeText(getActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        })

        {
            @Override
            public String getBodyContentType(){ return "application/json; charset=utf-8";}

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return stringObject.getBytes("utf-8");
                }catch (UnsupportedEncodingException em){
                    return null;
                }
            }
        };
        requestQueue.add(stringRequest);
    }

    private void fetchitemdb()
    {
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getApplicationContext());
        final JSONObject jsonObject = new JSONObject();
        final String stringObject=jsonObject.toString();
        final boolean[] flag = {false};

        StringRequest stringRequest=new StringRequest(Request.Method.POST, server_url_display, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    if (integerflag[0] ==0){
                        for(int k = 0;k<jsonArray.length();k++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(k);
                            String item = jsonObject1.getString("item_name").toUpperCase();
                            String price = jsonObject1.getString("price");
                            String item_and_price = item+" "+price+" RS";
                            items.add(item_and_price);
                            spinner.setAdapter(adapter);
                        }
                        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_items);
                        integerflag[0] =1;
                    }
                    else{

                        for(int i = 0;i<jsonArray.length();i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String item = jsonObject1.getString("item_name").toUpperCase();
                            String price = jsonObject1.getString("price");
                            String item_and_price = item+" "+price+" RS";
                            for (int j = 0;j<items.size();j++){
                                if (item_and_price.equals(items.get(j).toString())){
                                    flag[0] =true;
                                    break;
                                }
                            }

                            if (flag[0]==false) {
                                items.add(item_and_price);
                                spinner.setAdapter(adapter);
                            }
                            adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_items);
                            flag[0]=false;
                        }
                    }
                } catch (JSONException e){
                    Log.i("error123",e.toString());
                    e.printStackTrace();
                }
//                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("volleyerror123",error.toString());
//                Toast.makeText(custom_spinner_dropdown_items.xml(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        })

        {
            @Override
            public String getBodyContentType(){ return "application/json; charset=utf-8";}

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return stringObject.getBytes("utf-8");
                }catch (UnsupportedEncodingException em){
                    return null;
                }
            }
        };
        requestQueue.add(stringRequest);
    }

    private AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);

            spinner.setOnItemSelectedListener(listener);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
}
