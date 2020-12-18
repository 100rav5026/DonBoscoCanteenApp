package com.example.myapplication;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;

public class LogIn extends AppCompatActivity{

    Button btn;
    EditText id1,pass;
    String user,pas;
    String server_url = "http://192.168.1.7:8080/login";
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();
        btn= findViewById(R.id.LoginButton);
        id1= findViewById(R.id.IdField);
        pass= findViewById(R.id.PassField);
        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user=id1.getText().toString();
                Log.i("user", user);
                pas=pass.getText().toString();
                if(TextUtils.isEmpty(user)){
                    id1.setError("Empty");
                    id1.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(pas)){
                    pass.setError("Empty");
                    pass.requestFocus();
                    return;
                }
                if(user.equals("admin") && pas.equals("admin")){
                    Log.i("ddd", user);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("MoodleID",user);
                    editor.commit();

                    Intent i=new Intent(getApplicationContext(), navigationPage2.class);
                    startActivity(i);
                }else {
                    func();
                }
            }
        });
    }
    private void func()
    {
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id",user);
            jsonObject.put("password",pas );
        }catch(JSONException e){
            e.printStackTrace();
        }
        final String stringObject=jsonObject.toString();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                Log.i("volley123",response.toString());
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("MoodleID",user);
                editor.commit();

                Intent i=new Intent(getApplicationContext(), navigationPage.class);
                startActivity(i);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
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