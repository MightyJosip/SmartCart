package com.example.smartcart;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.smartcart.database.Popis;
import com.example.smartcart.database.PopisDao;
import com.example.smartcart.database.SmartCartDatabase;
import com.example.smartcart.database.Stavka;
import com.example.smartcart.database.StavkaDao;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

public class ListManager extends RecyclerView.Adapter<ViewHolder> {
    private List<Stavka> stavke;
    private Popis popis;
    private static final int ADD_ITEM_VIEW = 1;
    private static final int REGULAR_ITEM_VIEW = 0;
    private final Context context;


    public ListManager(Context context) {
        // Dovati popis koji se prikazuje
        this.context = context;
        SharedPreferences sp = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        int sifPopis = sp.getInt("sifPopis", 0);

        SmartCartDatabase db = SmartCartDatabase.getInstance(context);
        StavkaDao dao = db.stavkaDao();
        stavke = dao.dohvatiStavkeZaPopis(sifPopis);

        PopisDao popisDao = db.popisDao();
        popis = popisDao.dohvatiPoSifri(sifPopis).get(0);
    }

    public void addUsingBarcode(String barcode) {
        Connector.getInstance(context).fetchItemByBarcode(
                barcode, popis.getNacinIzracuna(), popis.getSifTrgovina(),
                response -> {
                    // TODO: staviti ovdje da se response pretvori u Stavku, spremi u db i stavke te
                    // pozvati notifyDataSetChanged()
                    Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT);
                },
                error -> {
                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT);
                }
        );
    }

    public void refreshPriceSum() {
        double listPrice = 0;
        double cartPrice = 0;
        for (Stavka it : stavke) {
            if (it.getUKosarici()) {
                cartPrice += it.getCijena();
            }
            listPrice += it.getCijena();
        }
        // TODO: refresh views
    }

    @Override
    public int getItemViewType(int position) {
        return (position == stavke.size()) ? ADD_ITEM_VIEW : REGULAR_ITEM_VIEW;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        if (viewType == REGULAR_ITEM_VIEW) {
            return new ItemViewHolder(li.inflate(R.layout.view_item, parent, false));
        } else {
            return new LastViewHolder(li.inflate(R.layout.view_add_item, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder.getItemViewType() == REGULAR_ITEM_VIEW) {
            ((ItemViewHolder) holder).populate(stavke.get(position));
        } else {
            return; // Ovaj je samo jedan i sve je definirano u XML-u
        }
    }

    @Override
    public int getItemCount() {
        return stavke.size() + 1;
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private final CheckBox checkBox;
        private final EditText editText;
        private final TextView textViewName;
        private final TextView textViewPrice;
        private static final DecimalFormat priceFormat = new DecimalFormat("##,##0.00\u00A4");
        private static final DecimalFormat quantityFormat = new DecimalFormat("##,##0.000");


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.item_checkbox);
            editText = itemView.findViewById(R.id.item_quantity);
            textViewName = itemView.findViewById(R.id.item_name);
            textViewPrice = itemView.findViewById(R.id.item_price);
        }

        public void populate(Stavka stavka) {
            checkBox.setChecked(stavka.getUKosarici());
            editText.setText(quantityFormat.format(stavka.getKolicina() / 1000.0));
            textViewName.setText(stavka.getNaziv());
            textViewPrice.setText(priceFormat.format(stavka.getCijena() * stavka.getKolicina() / 1000));

        }
    }

    static class LastViewHolder extends RecyclerView.ViewHolder {

        public LastViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
