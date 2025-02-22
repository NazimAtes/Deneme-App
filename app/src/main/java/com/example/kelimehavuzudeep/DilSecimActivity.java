package com.example.kelimehavuzudeep;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class DilSecimActivity extends BaseActivity { // BaseActivity'den türetildi!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dil_secim);

        String[] diller = {"Türkçe", "English", "Deutsch", "Français"};
        String[] dilKodlari = {"tr", "en", "de", "fr"}; // Dil kodları eklendi

        ListView listView = findViewById(R.id.lvDiller);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, diller);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String secilenDilKodu = dilKodlari[position];
            LocaleHelper.setLocale(this, secilenDilKodu); // Dil değiştirme işlemi

            // Tüm aktiviteleri kapatıp MainActivity'yi başlat
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}