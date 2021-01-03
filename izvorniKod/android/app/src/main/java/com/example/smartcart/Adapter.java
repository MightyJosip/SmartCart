package com.example.smartcart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    LayoutInflater inflater;
    List<Trgovina> trgovinas;

    public Adapter(Context ctx, List<Trgovina> trgovinas){
        this.inflater = LayoutInflater.from(ctx);
        this.trgovinas = trgovinas;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.store,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // bind the data
        holder.storeName.setText(trgovinas.get(position).getNaz_trgovina());
        holder.storeLocation.setText(trgovinas.get(position).getAdresa_trgovina());
        holder.storeTimeOfOpening.setText(trgovinas.get(position).getRadno_vrijeme_pocetaka().toString());
        holder.storeTimeOfCloseing.setText(trgovinas.get(position).getRadno_vrijeme_kraj().toString());

    }

    @Override
    public int getItemCount() {
        return trgovinas.size();
    }

    public  class ViewHolder extends  RecyclerView.ViewHolder{
        TextView storeName,storeLocation, storeTimeOfOpening, storeTimeOfCloseing;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            storeName = itemView.findViewById(R.id.storeName);
            storeLocation = itemView.findViewById(R.id.storeLocation);
            storeTimeOfOpening = itemView.findViewById(R.id.storeTimeOfOpening);
            storeTimeOfCloseing = itemView.findViewById(R.id.storeTimeOfCloseing);

            // handle onClick

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Do Something With this Click", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}