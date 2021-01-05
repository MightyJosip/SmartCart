package com.example.smartcart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

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
            holder.artikl_barkod.setText(artikli.get(position).get("artikl").toString());
            holder.artikl_cijena.setText(artikli.get(position).get("cijena").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return this.artikli.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView artikl_barkod, artikl_cijena;
        TextView primaryKey;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            artikl_barkod = itemView.findViewById(R.id.artikl_barkod);
            artikl_cijena = itemView.findViewById(R.id.artikl_cijena);

        }
    }
}
