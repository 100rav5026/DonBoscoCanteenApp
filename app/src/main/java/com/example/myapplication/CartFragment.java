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

        return inflater.inflate(R.layout.cart_frag,container,false);
    }
}
