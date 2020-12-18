package com.example.myapplication;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.database.Cursor;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import net.gotev.uploadservice.MultipartUploadRequest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.UUID;
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

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    private static  String url = "http://192.168.1.7:8080/profile";
    private static  String p_url = "http://192.168.1.7:8080/disp_profile";
    private static  String pic_url = "http://192.168.1.7:8080/pic";
    private Bitmap bitmap;


    SharedPreferences img_url;

    TextView name, email, mnumber;
    ImageButton btn_edt, btn_save;
    Button edit_photo, upload_photo;
    SharedPreferences pref;
    Uri image_uri;
    ImageView profile_image, square_image;
    String Path;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup)  inflater.inflate(R.layout.profile_frag,container,false);
        GetDetails();

        img_url =  getActivity().getSharedPreferences("image_details",getContext().MODE_PRIVATE);
        name = (TextView) viewGroup.findViewById(R.id.name);
        email = (TextView) viewGroup.findViewById(R.id.email);
        mnumber = (TextView) viewGroup.findViewById(R.id.mnumber);
        btn_edt = (ImageButton) viewGroup.findViewById(R.id.btn_edit);
        btn_save = (ImageButton) viewGroup.findViewById(R.id.btn_save);
        edit_photo = (Button) viewGroup.findViewById(R.id.btn_photo);
        profile_image = (ImageView) viewGroup.findViewById(R.id.circle_image);
        name.setEnabled(false);
        email.setEnabled(false);
        mnumber.setEnabled(false);
        btn_save.setVisibility(View.GONE);


        btn_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_save.setVisibility(View.VISIBLE);
                btn_edt.setVisibility(View.GONE);
                name.setEnabled(true);
                email.setEnabled(true);
                mnumber.setEnabled(true);
            }});
            btn_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_save.setVisibility(View.GONE);
                    btn_edt.setVisibility(View.VISIBLE);
                    name.setEnabled(false);
                    email.setEnabled(false);
                    mnumber.setEnabled(false);
                    SaveDetail();
                }

        });

            edit_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                        if (getActivity().checkSelfPermission(Manifest.permission.CAMERA)==
                                PackageManager.PERMISSION_DENIED ||
                                getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        == PackageManager.PERMISSION_DENIED ||
                                getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                                        == PackageManager.PERMISSION_DENIED ){
                            String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE};
                            requestPermissions(permission, PERMISSION_CODE);
                        }
                        else{
                            openCamera();
                        }
                    }else{
                        openCamera();
                    } }
            });
//            upload_photo.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    uploadMultipart();
//                }
//            });
        return viewGroup;

    }
    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "NEW PICTURE");
        values.put(MediaStore.Images.Media.DESCRIPTION, "FROM THE CAMERA");
        image_uri = getActivity().getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Log.i("image123", String.valueOf(image_uri));
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);

    }
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openCamera();
                }else{
                    Toast.makeText(getActivity(), "Permission Denied...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            profile_image.setImageURI(image_uri);
        }
    }
    public void uploadMultipart() {
        //getting name for the image
//        pref = getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);
//        String name = pref.getString("MoodleID", null);
        BitmapDrawable drawable = (BitmapDrawable) profile_image.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();

        final String image_byte=String.valueOf(byteArray);
        Log.i("hii", byteArray.toString());

//            getimage();
        pref = getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);

        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getApplicationContext());
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", pref.getString("MoodleID",null));
            jsonObject.put("array",image_byte);
        }catch(JSONException e){
            e.printStackTrace();
        }
        final String stringObject=jsonObject.toString();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, pic_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity().getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                Log.i("volley123",response.toString());
//                    SharedPreferences.Editor editor = pref.edit();
//                    editor.putString("MoodleID", String.valueOf(pref));
//                    editor.commit();
//                    Intent i=new Intent(getActivity().getApplicationContext(), navigationPage.class);
//                    startActivity(i);
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

//        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//        square_image.setImageBitmap(Bitmap.createScaledBitmap(bmp, square_image.getWidth(), square_image.getHeight(), false));
    }



//    public String getPath(Uri uri) {
//        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
//        cursor.moveToFirst();
//        String document_id = cursor.getString(0);
//        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
//        cursor.close();
//
//        cursor = getActivity().getContentResolver().query(
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
//        cursor.moveToFirst();
//        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//        cursor.close();
//        Log.i("path123", path);
//        return path;
//    }



















    private void GetDetails(){
        pref = getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getApplicationContext());
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", pref.getString("MoodleID",null));
        }catch(JSONException e){
            e.printStackTrace();
        }
        final String stringObject=jsonObject.toString();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, p_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject1 = new JSONObject(response);

                    String name1 = jsonObject1.getString("name");
                    Log.i("name123", response.toString());
                    String email1 = jsonObject1.getString("email");
                    String mnumber1 = jsonObject1.getString("phone");
                    String array = jsonObject1.getString("image1");
                    Log.i("image123", array);

                    name.setText(name1);
                    email.setText(email1);
                    mnumber.setText(mnumber1);

                    byte[] byteArray = array.getBytes();
//                    Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//                    profile_image.setImageBitmap(bmp);
                    InputStream inputStream  = new ByteArrayInputStream(byteArray);
                    Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
                    profile_image.setImageBitmap(bitmap);

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

    private void SaveDetail(){
        final String name = this.name.getText().toString().trim();
        final String email = this.email.getText().toString().trim();
        final String mnumber = this.mnumber.getText().toString().trim();

        pref = getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);

        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getApplicationContext());
            final JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id", pref.getString("MoodleID",null));
                jsonObject.put("name",name);
                jsonObject.put("email",email );
                jsonObject.put("phone",mnumber );
            }catch(JSONException e){
                e.printStackTrace();
            }
            final String stringObject=jsonObject.toString();

            StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(getActivity().getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                    Log.i("volley123",response.toString());
//                    SharedPreferences.Editor editor = pref.edit();
//                    editor.putString("MoodleID", String.valueOf(pref));
//                    editor.commit();
//                    Intent i=new Intent(getActivity().getApplicationContext(), navigationPage.class);
//                    startActivity(i);
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




//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        inflater.inflate(R.menu.profile_edit, menu);
//        action = menu;
//        action.findItem(R.id.profile_save).setVisible(false);
//        super.onCreateOptionsMenu(menu, inflater);
//        return true;
//    }


//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch(item.getItemId()){
//            case R.id.profile_edit:
//                name.setFocusableInTouchMode(true);
//                email.setFocusableInTouchMode(true);
//                mnumber.setFocusableInTouchMode(true);
//                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputMethodManager.showSoftInput(name, InputMethodManager.SHOW_IMPLICIT);
//
//                action.findItem(R.id.profile_edit).setVisible(false);
//                action.findItem(R.id.profile_save).setVisible(true);
//                return true;
//
//            case R.id.profile_save:
//                SaveDetail();
//
//                action.findItem(R.id.profile_edit).setVisible(true);
//                action.findItem(R.id.profile_save).setVisible(false);
//
//                name.setFocusableInTouchMode(false);
//                email.setFocusableInTouchMode(false);
//                mnumber.setFocusableInTouchMode(false);
//                name.setFocusable(false);
//                email.setFocusable(false);
//                mnumber.setFocusable(false);
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//
//        }
//    }
//
//    private void SaveDetail() {
//        final String name = this.name.getText().toString().trim();
//        final String email = this.email.getText().toString().trim();
//        final String mnumber = this.mnumber.getText().toString().trim();
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//            }
//        }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                });
//    }

}
