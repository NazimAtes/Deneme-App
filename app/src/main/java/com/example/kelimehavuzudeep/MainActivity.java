package com.example.kelimehavuzudeep;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;
import java.util.List;

public class MainActivity extends BaseActivity { // BaseActivity'den türetildi
    public int guncellenecekKelimeId = -1;
    private DatabaseHelper db;
    private KelimeAdapter adapter;
    private EditText etYabanci, etCeviri, etOrnek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Tema Ayarları
        SharedPreferences temaPrefs = getSharedPreferences("TEMA", MODE_PRIVATE);
        boolean geceModu = temaPrefs.getBoolean("geceModu", false);
        AppCompatDelegate.setDefaultNightMode(geceModu ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        // Bileşenleri Bağla
        db = new DatabaseHelper(this);
        etYabanci = findViewById(R.id.etYabanci);
        etCeviri = findViewById(R.id.etCeviri);
        etOrnek = findViewById(R.id.etOrnek);
        Button btnKaydet = findViewById(R.id.btnKaydet);
        ImageButton btnTema = findViewById(R.id.btnTema);
        ImageButton btnDil = findViewById(R.id.btnDil);
        RecyclerView rvKelimeler = findViewById(R.id.rvKelimeler);
        SearchView searchView = findViewById(R.id.searchView);

        // RecyclerView Ayarla
        rvKelimeler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new KelimeAdapter(db.tumKelimeler(), this);
        rvKelimeler.setAdapter(adapter);

        // Kaydet/Düzenle Butonu
        btnKaydet.setOnClickListener(v -> kaydetGuncelle());

        // Tema Değiştir
        btnTema.setOnClickListener(v -> {
            boolean yeniMod = !geceModu;
            SharedPreferences.Editor editor = temaPrefs.edit();
            editor.putBoolean("geceModu", yeniMod);
            editor.apply();
            recreate(); // Aktiviteyi yeniden başlat
        });

        // Dil Değiştir
        btnDil.setOnClickListener(v -> {
            // Dil seçim ekranını başlat
            startActivity(new Intent(this, DilSecimActivity.class));
        });

        // Arama İşlevi
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.guncelle(db.kelimeAra(newText));
                return true;
            }
        });
    }

    private void kaydetGuncelle() {
        String yabanci = etYabanci.getText().toString().trim();
        String ceviri = etCeviri.getText().toString().trim();
        String ornek = etOrnek.getText().toString().trim();

        if (yabanci.isEmpty() || ceviri.isEmpty()) {
            Toast.makeText(this, getString(R.string.hata_bos_alan), Toast.LENGTH_SHORT).show();
            return;
        }

        Kelime kelime = new Kelime();
        kelime.setYabanci(yabanci);
        kelime.setCeviri(ceviri);
        kelime.setOrnek(ornek);

        if (guncellenecekKelimeId == -1) {
            db.kelimeEkle(kelime);
            Toast.makeText(this, getString(R.string.kaydedildi), Toast.LENGTH_SHORT).show();
        } else {
            kelime.setId(guncellenecekKelimeId);
            kelime.setTarih(System.currentTimeMillis());
            db.kelimeGuncelle(kelime);
            Toast.makeText(this, getString(R.string.guncellendi), Toast.LENGTH_SHORT).show();
            guncellenecekKelimeId = -1;
        }

        adapter.guncelle(db.tumKelimeler());
        temizleInputlar();
    }

    private void temizleInputlar() {
        etYabanci.getText().clear();
        etCeviri.getText().clear();
        etOrnek.getText().clear();
    }
}