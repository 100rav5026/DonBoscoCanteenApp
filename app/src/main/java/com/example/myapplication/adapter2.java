package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class adapter2 extends RecyclerView.Adapter<adapter2.adapterViewHolder> {
    private LayoutInflater layoutInflater;
    private List<String> data;

    public  adapter2(Context context, List<String> data){
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }
    public class adapterViewHolder extends RecyclerView.ViewHolder {
        public TextView itemname,plates,time;
        public adapterViewHolder(@NonNull View itemView) {
            super(itemView);
            itemname = (TextView)itemView.findViewById(R.id.itemname);
            plates = (TextView)itemView.findViewById(R.id.plates);
            time = (TextView)itemView.findViewById(R.id.time);
        }
    }

    @NonNull
    @Override
    public adapter2.adapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_details, parent, false);
        return new adapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter2.adapterViewHolder holder, int position) {
        String title = data.get(position);
        Log.i("tittlelel", title);
        String[] splited = title.split("\\s+");
        Log.i("itemname", splited[0]);
        Log.i("plates", splited[1]);
        Log.i("time", splited[2]);

        holder.itemname.setText(splited[0]);
        holder.plates.setText(splited[1]);
        holder.time.setText(splited[2]);
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
