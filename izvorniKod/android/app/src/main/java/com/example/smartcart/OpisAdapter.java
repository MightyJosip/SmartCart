package com.example.smartcart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class OpisAdapter extends RecyclerView.Adapter<OpisAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<JSONObject> opisi;
    Context context;
    String session_id;

    public OpisAdapter(Context ctx, List<JSONObject> opisi, Context prikazTrgovineActivityContext, String session_id) {
        this.inflater = LayoutInflater.from(ctx);
        this.opisi = opisi;
        this.context = prikazTrgovineActivityContext;
        this.session_id = session_id;
    }

    @NonNull
    @Override
    public OpisAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.opisi, parent, false);
        return new OpisAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OpisAdapter.ViewHolder holder, int position) {
        try {
            holder.opis_opis.setText("Opis artikla: " + opisi.get(position).get("opis_artikla").toString());
            holder.opis_glasovi.setText("Broj glasova: " + opisi.get(position).get("broj_glasova").toString());
            //holder.opis_upvote.setText("Ovaj gumb ništa ne radi :(");
            // set sif opis for upvote button
            holder.sif_opis = opisi.get(position).getString("sif_opis");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return this.opisi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView opis_opis, opis_glasovi;
        Button opis_upvote;
        // cheesy but ok
        public String sif_opis;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            opis_opis = itemView.findViewById(R.id.opis_opis);
            opis_glasovi = itemView.findViewById(R.id.opis_glasovi);
            opis_upvote = itemView.findViewById(R.id.opis_upvote);
            
            Connector connector = Connector.getInstance(context);
            opis_upvote.setOnClickListener(l -> {
                connector.upvote(sif_opis, session_id, onSuccess -> {
                }, onFail -> {
                });
            });
        }
    }
}
