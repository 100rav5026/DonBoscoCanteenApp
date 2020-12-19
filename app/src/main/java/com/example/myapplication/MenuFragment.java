package com.example.myapplication;

import android.content.Context;
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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MenuFragment extends Fragment {
    RecyclerView recyclerView;
    private adapter CustomAdapter;
    private ArrayList<String> items1;
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback;
    TextView textView2, total1, totalCost;
    Spinner spinner1, spinner3, spinner2;
    Button bton, placedorder;
    Integer totalCostint;
    String menu_url, menu_url1;
    ArrayList<String> itemsSelected;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup)  inflater.inflate(R.layout.menu_frag,container,false);
        menu_url = getString(R.string.url)+"/disp_menu";
        menu_url1 = getString(R.string.url)+"/placeddb";
        spinner1 = viewGroup.findViewById(R.id.spinner1);
        spinner2 = viewGroup.findViewById(R.id.spinner2);
        spinner3 = viewGroup.findViewById(R.id.spinner3);
        bton = viewGroup.findViewById(R.id.addbtn);
        placedorder = viewGroup.findViewById(R.id.placedorder);

        totalCost = viewGroup.findViewById(R.id.totalCost);
        totalCostint = 0;
        String[] time = new String[]{"Select Time", "9", "10", "11", "12", "13", "14", "15", "16", "17"};
        final ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(
                getActivity(),R.layout.spinner,time){
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
        spinnerArrayAdapter1.setDropDownViewResource(R.layout.spinner);
        spinner3.setAdapter(spinnerArrayAdapter1);
        spinnerArrayAdapter1.setDropDownViewResource(R.layout.custom_spinner_dropdown_items);

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
        itemsSelected = new ArrayList<>();
        items1 = new ArrayList<>();
        bton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text1 = spinner1.getSelectedItem().toString().trim();
                String text2 = spinner2.getSelectedItem().toString().trim();
                String text3 = text1+"  Qty"+text2;
                String text4 = text3.replaceAll("\\D+"," ").trim();
                Log.i("text4", text4);
                String text5 = spinner3.getSelectedItem().toString();
                String text6 = text1 +"  "+  text2+  "  "+ text5;
                String[] splited = text4.split("\\s+");
                try {
                    int j = Integer.parseInt(splited[0])*Integer.parseInt(splited[1]);
                    totalCostint = totalCostint + j;
                    totalCost.setText("Total Cost is "+totalCostint.toString());
                }catch (NumberFormatException e){
                    Toast.makeText(getActivity(),"hehehe",Toast.LENGTH_SHORT).show();
                }

//                Log.i("text123456", String.valueOf(j));
                Log.i("text1234567", String.valueOf(totalCostint));

                Toast.makeText(getActivity().getApplicationContext(), text3, Toast.LENGTH_SHORT).show();
                if (text1!="Select Item" && text2!="Select Quantity") {
                    items1.add(text3);
//                    items1.add(String.valueOf(j));
                }
                recyclerView = viewGroup.findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                CustomAdapter = new adapter(getContext(), items1);
                try {
                    new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                        @Override
                        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                            return false;
                        }

                        @Override
                        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                            items1.remove(viewHolder.getAdapterPosition());
                            final View view = viewHolder.itemView;

                            Log.i("hehehehehe", String.valueOf(view));
                            int j = Integer.parseInt(splited[0])*Integer.parseInt(splited[1]);
                            totalCostint = totalCostint - j;
                            totalCost.setText("Total Cost is "+totalCostint.toString());
                            CustomAdapter.notifyDataSetChanged();
                        }
                    }).attachToRecyclerView(recyclerView);
                }catch (NullPointerException n){
                    Toast.makeText(getActivity().getApplicationContext(), text3, Toast.LENGTH_SHORT).show();

                }
                recyclerView.setAdapter((RecyclerView.Adapter) CustomAdapter);


                itemsSelected.add(text6);
                }

        });

    placedorder.setOnClickListener(new View.OnClickListener(){

        @Override
        public void onClick(View v) {
        PlacedOrders();
        }
    } );


        return  viewGroup;
    }

    private void PlacedOrders(){

        for (int counter = 0; counter < itemsSelected.size(); counter++) {
            String[] itemnamedropdown = itemsSelected.get(counter).toString().split("  ");
            final String itemname = itemnamedropdown[0];
            final Integer plates = Integer.valueOf(itemnamedropdown[2]);
            final String time = itemnamedropdown[3];
            RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getApplicationContext());
            final JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("itemname",itemname);
                jsonObject.put("plates",plates);
                jsonObject.put("time",time);
            }catch(JSONException e){
                e.printStackTrace();
            }
            final String stringObject=jsonObject.toString();

            StringRequest stringRequest=new StringRequest(Request.Method.POST, menu_url1, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(getActivity().getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                    Log.i("volley123",response.toString());
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



    }


    private void GetDetails(){
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, menu_url, new Response.Listener<String>() {
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