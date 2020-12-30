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
import android.util.Base64;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import net.gotev.uploadservice.MultipartUploadRequest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.UUID;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import retrofit2.Call;
import retrofit2.Callback;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private RequestQueue requestQueue;
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    String profile_url, p_url ;
    private Bitmap bitmap;
    SharedPreferences sharedPreferences;

    TextView name, email, mnumber;
    ImageButton btn_edt, btn_save;
    Button edit_photo, upload_photo;
    SharedPreferences pref;
    Uri image_uri;
    ImageView profile_image, square_image;
    String Path, user_id;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        profile_url = getString(R.string.url)+"/profile" ;
        p_url = getString(R.string.url)+"/disp_profile" ;

        ViewGroup viewGroup = (ViewGroup)  inflater.inflate(R.layout.profile_frag,container,false);
        GetDetails();

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
        sharedPreferences = getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("MoodleID", null);


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
                    Toast.makeText(getActivity(), "PIC Clicked", Toast.LENGTH_SHORT).show();
                checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);}
            });
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode ==100){
            try {
                bitmap = (Bitmap) data.getExtras().get("data");
                profile_image.setImageBitmap(bitmap);
                uploadImage();
            } catch (Exception e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
            }


        }
    }
    private void uploadImage() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
        byte[] imageInByte = byteArrayOutputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageInByte,Base64.DEFAULT);
        Call<Response_POJO> call = RetroClient.getInstance().getApi().uploadImage(encodedImage, user_id);
        call.enqueue(new retrofit2.Callback<Response_POJO>() {
            @Override
            public void onResponse(Call<Response_POJO> call, retrofit2.Response<Response_POJO> response) {
                Toast.makeText(getContext(),response.body().getRemarks(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Response_POJO> call, Throwable t) {
                Toast.makeText(getContext(),"Network Failure", Toast.LENGTH_SHORT).show();

            }
        });
    }
       public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(this.getContext(), permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(this.getActivity(), new String[] { permission }, requestCode);
        }
        else {
            Toast.makeText(this.getContext(),"Permission already granted", Toast.LENGTH_SHORT).show();
            if(requestCode == STORAGE_PERMISSION_CODE) {
                checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
            } else if (requestCode == CAMERA_PERMISSION_CODE) {
                Intent intent = new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
                startActivityForResult(intent, 100);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this.getContext(), "Camera Permission Granted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
                startActivityForResult(intent, 100);
            }
            else {
                Toast.makeText(this.getContext(), "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this.getContext(), "Storage Permission Granted", Toast.LENGTH_SHORT).show();
                checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
            }
            else {
                Toast.makeText(this.getContext(), "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }



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
                    String array = jsonObject1.getString("image");

                    name.setText(name1);
                    email.setText(email1);
                    mnumber.setText(mnumber1);


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

            StringRequest stringRequest=new StringRequest(Request.Method.POST, profile_url, new Response.Listener<String>() {
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
