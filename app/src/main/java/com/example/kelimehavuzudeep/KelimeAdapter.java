package com.example.kelimehavuzudeep;




import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class KelimeAdapter extends RecyclerView.Adapter<KelimeAdapter.ViewHolder> {
    private List<Kelime> kelimeList;
    private Context context;
    private DatabaseHelper db;

    public KelimeAdapter(List<Kelime> kelimeList, Context context) {
        this.kelimeList = kelimeList;
        this.context = context;
        db = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kelime, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Kelime kelime = kelimeList.get(position);
        holder.tvYabanci.setText(kelime.getYabanci());
        holder.tvOrnek.setText(kelime.getOrnek());
        holder.tvCeviri.setText(kelime.getCeviri());

        // Göster/Gizle Butonu
        holder.btnGoster.setOnClickListener(v -> {
            if (holder.tvCeviri.getVisibility() == View.VISIBLE) {
                holder.tvCeviri.setVisibility(View.GONE);
                holder.btnGoster.setText("Göster");
            } else {
                holder.tvCeviri.setVisibility(View.VISIBLE);
                holder.btnGoster.setText("Gizle");
            }
        });

        // Sil Butonu
        holder.btnSil.setOnClickListener(v -> {
            db.kelimeSil(kelime.getId());
            kelimeList.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context, "Kelime silindi", Toast.LENGTH_SHORT).show();
        });

        // Düzenle Butonu
        holder.btnDuzenle.setOnClickListener(v -> {
            if (context instanceof MainActivity) {
                MainActivity activity = (MainActivity) context;
                activity.etYabanci.setText(kelime.getYabanci());
                activity.etCeviri.setText(kelime.getCeviri());
                activity.etOrnek.setText(kelime.getOrnek());
                activity.guncellenecekKelimeId = kelime.getId();
            }
        });

        // En Başa Çıkar Butonu
        holder.btnEnBasa.setOnClickListener(v -> {
            kelime.setTarih(System.currentTimeMillis());
            db.kelimeGuncelle(kelime);
            kelimeList = db.tumKelimeler(); // Listeyi veritabanından yeniden çek
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return kelimeList.size();
    }

    public void guncelle(List<Kelime> yeniListe) {
        kelimeList.clear();
        kelimeList.addAll(yeniListe);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvYabanci, tvOrnek, tvCeviri;
        TextView btnGoster, btnDuzenle, btnEnBasa, btnSil;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvYabanci = itemView.findViewById(R.id.tvYabanci);
            tvOrnek = itemView.findViewById(R.id.tvOrnek);
            tvCeviri = itemView.findViewById(R.id.tvCeviri);
            btnGoster = itemView.findViewById(R.id.btnGoster);
            btnDuzenle = itemView.findViewById(R.id.btnDuzenle);
            btnEnBasa = itemView.findViewById(R.id.btnEnBasa);
            btnSil = itemView.findViewById(R.id.btnSil);
        }
    }
}