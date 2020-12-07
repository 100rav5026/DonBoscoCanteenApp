package com.example.myapplication;

import android.app.LauncherActivity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class adapter extends RecyclerView.Adapter<adapter.adapterViewHolder> {
    private LayoutInflater layoutInflater;
    private List<String> data;

    public  adapter(Context context, List<String> data){
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }
    public class adapterViewHolder extends RecyclerView.ViewHolder {
        public TextView itemtext, total;
        public Spinner spinner1, spinner2;
        public adapterViewHolder(@NonNull View itemView) {
            super(itemView);

            itemtext = itemView.findViewById(R.id.textView);
            total = itemView.findViewById(R.id.total1);
        }
    }

    @NonNull
    @Override
    public adapter.adapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.card_view, parent, false);
        return new adapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter.adapterViewHolder holder, int position) {
        String title = data.get(position);
        Log.i("qqqq", title);
        holder.itemtext.setText(title);
        String text4 = title.replaceAll("\\D+"," ").trim();
        String[] splited = text4.split("\\s+");
        int j = Integer.parseInt(splited[0])*Integer.parseInt(splited[1]);
        Log.i("qqq", String.valueOf(j));
        holder.total.setText(String.valueOf(j));
    }

    @Override
    public int getItemCount() {
        if(data == null){
            return  0;
        }
        else {
            return data.size();
        }}


}
