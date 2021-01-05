package com.example.smartcart;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class ArtiklAdapter extends RecyclerView.Adapter<ArtiklAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<JSONObject> artikli;
    Context context;

    public ArtiklAdapter(Context ctx, List<JSONObject> artikli, Context prikazTrgovineActivityContext) {
        this.inflater = LayoutInflater.from(ctx);
        this.artikli = artikli;
        this.context = prikazTrgovineActivityContext;
    }

    @NonNull
    @Override
    public ArtiklAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.artikl, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ArtiklAdapter.ViewHolder holder, int position) {
        try {
            holder.artikl_barkod.setText("Barkod: " + artikli.get(position).get("artikl").toString());
            holder.artikl_cijena.setText("Cijena: " + artikli.get(position).get("cijena").toString());

            if (Boolean.parseBoolean(artikli.get(position).get("dostupan").toString())) {
                holder.artikl_dostupan.setText("Dostupan");
            } else {
                holder.artikl_dostupan.setText("Nedostupan");
            }

            if (Boolean.parseBoolean(artikli.get(position).get("akcija").toString())) {
                holder.artikl_akcija.setText("Na akciji");
            } else {
                holder.artikl_akcija.setText("Nije na akciji");
            }

            holder.artikl_sifTrg.setText(artikli.get(position).get("trgovina").toString());
            //holder.artikl_dostupan.setText(artikli.get(position).get("dostupan").toString());
            //holder.artikl_akcija.setText(artikli.get(position).get("akcija").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return this.artikli.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView artikl_barkod, artikl_cijena, artikl_dostupan, artikl_akcija, artikl_sifTrg;
        Button artikl_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            artikl_barkod = itemView.findViewById(R.id.artikl_barkod);
            artikl_cijena = itemView.findViewById(R.id.artikl_cijena);
            artikl_dostupan = itemView.findViewById(R.id.artikl_dostupan);
            artikl_akcija = itemView.findViewById(R.id.artikl_akcija);
            artikl_sifTrg = itemView.findViewById(R.id.artikl_sifTrg);
            artikl_btn = itemView.findViewById(R.id.artikl_btn);

            artikl_btn.setOnClickListener(v -> {
                Intent intent = new Intent(context, PrikazArtikla.class);
                //Log.d("ovi", fields.getString("trgovina") + " " + fields.getString("artikl"));

                String[] tmp;
                tmp = ((String) artikl_sifTrg.getText()).split(" ");
                intent.putExtra("sif_trgovina", (Serializable) tmp[tmp.length - 1]);

                tmp = ((String) artikl_barkod.getText()).split(" ");
                intent.putExtra("barkod", (Serializable) tmp[tmp.length - 1]);
                context.startActivity(intent);
            });
        }
    }
}
