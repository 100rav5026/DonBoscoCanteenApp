package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class PlacedOrders extends Fragment{
    SharedPreferences pref;
    ConstraintLayout constraintLayout;
    RecyclerView recyclerView;
    private adapter Customadapter;
    private ArrayList<String> item_details;
    String server_url_display = "http://192.168.1.7:8080/placed_orders";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        pref = getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);
        ViewGroup viewGroup = (ViewGroup)  inflater.inflate(R.layout.placedorders,container,false);
        Toast.makeText(getActivity().getApplicationContext(), pref.getString("MoodleID",null), Toast.LENGTH_LONG).show();
        constraintLayout = (ConstraintLayout) viewGroup.findViewById(R.id.constraintlayout);
        ConstraintSet set = new ConstraintSet();
        fetchplacedorders();
        return viewGroup;
    }
    private void fetchplacedorders()
    {
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getApplicationContext());
        final JSONObject jsonObject = new JSONObject();
        final String stringObject=jsonObject.toString();
        final boolean[] flag = {false};
        item_details = new ArrayList<>();

        StringRequest stringRequest=new StringRequest(Request.Method.GET, server_url_display, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    for(int k = 0;k<jsonArray.length();k++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(k);
                        String itemname = jsonObject1.getString("Itemname").toUpperCase();
                        String plates = jsonObject1.getString("Plates");
                        String time = jsonObject1.getString("Time");

                        String All = itemname+" "+plates+" "+time;
                        Log.i("All_details",All);
                        item_details.add(All);
                        Log.i("itemdetail123",item_details.toString());
                        recyclerView = recyclerView.findViewById(R.id.recyclerview);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                        Customadapter = new adapter(getActivity().getApplicationContext(), item_details);
                    }
                    recyclerView.setAdapter(Customadapter);


                } catch (JSONException e){
                    Log.i("error123",e.toString());
                    e.printStackTrace();
                }
                Toast.makeText(getActivity().getApplicationContext(),response,Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("volleyerror123",error.toString());
                Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
