package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LogoutFragment extends Fragment {
    SharedPreferences prf;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.logout_frag,container,false);
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(getActivity(), LogoutFragment.class);
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getActivity());
        builder
                .setTitle("LOGOUT")
                .setMessage("Are you sure?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "Yes button pressed", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = prf.edit();
                        editor.clear();
                        editor.commit();
//                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();

        return view;
    }
}
