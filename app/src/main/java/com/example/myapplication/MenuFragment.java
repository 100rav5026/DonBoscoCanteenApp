package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenuFragment extends Fragment {
    RecyclerView recyclerView;
    private adapter CustomAdapter;
    private ArrayList<String> items1;

    TextView textView2, total1, totalCost;
    Spinner spinner1;
    Spinner spinner2;
    Button bton;
    Integer totalCostint;
    private static String menu_url = "http://192.168.1.7:8080/disp_menu";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup)  inflater.inflate(R.layout.menu_frag,container,false);
        spinner1 = viewGroup.findViewById(R.id.spinner1);
        spinner2 = viewGroup.findViewById(R.id.spinner2);
        bton = viewGroup.findViewById(R.id.addbtn);
        totalCost = viewGroup.findViewById(R.id.totalCost);
        totalCostint = 0;

        String[] items = new String[]{"Select Quantity", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(),R.layout.spinner,items){
            @Override
            public boolean isEnabled(int position){
                if(position == 0) { return false; }
                else { return true;}
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){ tv.setTextColor(Color.GRAY); }
                else { tv.setTextColor(Color.BLACK); }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner);
        spinner2.setAdapter(spinnerArrayAdapter);
        GetDetails();

        items1 = new ArrayList<>();
        bton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text1 = spinner1.getSelectedItem().toString();

                String text2 = spinner2.getSelectedItem().toString();

                String text3 = text1+"  Qty"+text2;
                String text4 = text3.replaceAll("\\D+"," ").trim();
                String[] splited = text4.split("\\s+");
                int j = Integer.parseInt(splited[0])*Integer.parseInt(splited[1]);
                totalCostint = totalCostint + j;
                totalCost.setText("Total Cost is "+totalCostint.toString());
                Log.i("text123456", String.valueOf(j));
                Log.i("text1234567", String.valueOf(totalCostint));

                Toast.makeText(getActivity().getApplicationContext(), text3, Toast.LENGTH_SHORT).show();
                if (text1!="Select Item" && text2!="Select Quantity") {
                    items1.add(text3);
//                    items1.add(String.valueOf(j));
                }
                recyclerView = viewGroup.findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                CustomAdapter = new adapter(getContext(), items1);
                recyclerView.setAdapter((RecyclerView.Adapter) CustomAdapter);
                }

        });

        return  viewGroup;
    }

    private void GetDetails(){
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, menu_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ArrayList<String> itemsList =new ArrayList<>();
                    itemsList.add("Select Item");
                    final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                            getActivity(),R.layout.spinner,itemsList){
                        @Override
                        public boolean isEnabled(int position){
                            if(position == 0) { return false;}
                            else { return true;} }
                        @Override
                        public View getDropDownView(int position, View convertView,
                                                    ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            TextView tv = (TextView) view;
                            if(position == 0){ tv.setTextColor(Color.GRAY); }
                            else { tv.setTextColor(Color.BLACK); }
                            return view; }};
                    spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner);
                    spinner1.setAdapter(spinnerArrayAdapter);

                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonobject = jsonArray.getJSONObject(i);
                        String item = (jsonobject.getString("item_name"));
                        String price =(jsonobject.getString("price"));
                        String itemAndPrice = item+"   Rs "+price;
                        itemsList.add(itemAndPrice);
                    }
                    Log.i("response123", response.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("name123", e.toString());
                }

                Toast.makeText(getActivity().getApplicationContext(),response,Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                Log.i("volleyabc", error.toString());
            }
        })
        {
            @Override
            public String getBodyContentType(){ return "application/json; charset=utf-8";}

        };
        requestQueue.add(stringRequest);
    }}