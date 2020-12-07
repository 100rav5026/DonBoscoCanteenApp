package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CartFragment extends Fragment {
    SharedPreferences pref;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        pref = getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);
        Toast.makeText(getActivity().getApplicationContext(), pref.getString("MoodleID",null), Toast.LENGTH_LONG).show();


//            View view = inflater.inflate(R.layout.details,container,false);
//            TextView result = (TextView)getView().findViewById(R.id.resultView);
//            Button btnLogOut = (Button)getView().findViewById(R.id.btnLogOut);
//        SharedPreferences prf = getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//
//        builder.setTitle("Erase hard drive");
//        builder.setMessage("Are you sure?");
//        builder.setIcon(android.R.drawable.ic_dialog_alert);
//        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        //Yes button clicked, do something
//                        Toast.makeText(getActivity(), "Yes button pressed", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .setNegativeButton("No", null)                      //Do nothing on no
//                .show();
//
//        Intent intent = new Intent(getActivity(), MainActivity.class);
//            result.setText("Hello, "+prf.getString("MoodleID",null));
//            btnLogOut.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    SharedPreferences.Editor editor = prf.edit();
//                    editor.clear();
//                    editor.commit();
//                    startActivity(intent);
//                }
//            });

        return inflater.inflate(R.layout.cart_frag,container,false);
    }
}
