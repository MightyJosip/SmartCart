package com.example.smartcart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    LayoutInflater inflater;
    List<Trgovina> trgovinas;
    Context context;
    Integer tmp_pk;

    public Adapter(Context ctx, List<Trgovina> trgovinas, Context homeScreenActivityContext){
        this.inflater = LayoutInflater.from(ctx);
        this.trgovinas = trgovinas;
        this.context = homeScreenActivityContext;
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
        holder.storeName.setText(trgovinas.get(position).getNaz_trgovina() + " " +trgovinas.get(position).getPk());
        holder.storeLocation.setText(trgovinas.get(position).getAdresa_trgovina());
        holder.storeTimeOfOpening.setText(trgovinas.get(position).getRadno_vrijeme_pocetaka().toString());
        holder.storeTimeOfCloseing.setText(trgovinas.get(position).getRadno_vrijeme_kraj().toString());
        Picasso.get().load(R.drawable.map_marker).into(holder.mapIcon);

    }

    @Override
    public int getItemCount() {
        return trgovinas.size();
    }

    public  class ViewHolder extends  RecyclerView.ViewHolder{
        TextView storeName,storeLocation, storeTimeOfOpening, storeTimeOfCloseing;
        ImageView mapIcon;
        TextView primaryKey;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            storeName = itemView.findViewById(R.id.storeName);
            storeLocation = itemView.findViewById(R.id.storeLocation);
            storeTimeOfOpening = itemView.findViewById(R.id.storeTimeOfOpening);
            storeTimeOfCloseing = itemView.findViewById(R.id.storeTimeOfCloseing);
            mapIcon = itemView.findViewById(R.id.mapLogo);

            // handle onClick
            // check state, if state == ulazno_stanje

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PrikazTrgovine.class);
                    intent.putExtra("name", (Serializable) storeName.getText());
                    context.startActivity(intent);
                }
            });

            mapIcon.setOnClickListener(v -> {
                Uri location = Uri.parse("geo:0,0?q=" + storeLocation.getText().toString());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(mapIntent);
            });
        }
    }
}