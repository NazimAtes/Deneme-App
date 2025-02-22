 package com.example.kelimehavuzudeep;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "KelimeDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_KELIMELER = "kelimeler";
    private static final String KEY_ID = "id";
    private static final String KEY_YABANCI = "yabanci";
    private static final String KEY_CEVIRI = "ceviri";
    private static final String KEY_ORNEK = "ornek";
    private static final String KEY_TARIH = "tarih";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_KELIMELER + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_YABANCI + " TEXT NOT NULL,"
                + KEY_CEVIRI + " TEXT NOT NULL,"
                + KEY_ORNEK + " TEXT,"
                + KEY_TARIH + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KELIMELER);
        onCreate(db);
    }

    // Kelime Ekleme
    public void kelimeEkle(Kelime kelime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_YABANCI, kelime.getYabanci());
        values.put(KEY_CEVIRI, kelime.getCeviri());
        values.put(KEY_ORNEK, kelime.getOrnek());
        values.put(KEY_TARIH, System.currentTimeMillis());
        db.insert(TABLE_KELIMELER, null, values);
        db.close();
    }

    // Tüm Kelimeleri Getir (Yeniden Eskiye)
    public List<Kelime> tumKelimeler() {
        List<Kelime> kelimeList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_KELIMELER + " ORDER BY " + KEY_TARIH + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Kelime kelime = new Kelime();
                kelime.setId(cursor.getInt(0));
                kelime.setYabanci(cursor.getString(1));
                kelime.setCeviri(cursor.getString(2));
                kelime.setOrnek(cursor.getString(3));
                kelimeList.add(kelime);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return kelimeList;
    }

    // Kelime Silme
    public void kelimeSil(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_KELIMELER, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Kelime Güncelleme
    public void kelimeGuncelle(Kelime kelime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_YABANCI, kelime.getYabanci());
        values.put(KEY_CEVIRI, kelime.getCeviri());
        values.put(KEY_ORNEK, kelime.getOrnek());
        values.put(KEY_TARIH, System.currentTimeMillis());
        db.update(TABLE_KELIMELER, values, KEY_ID + " = ?", new String[]{String.valueOf(kelime.getId())});
        db.close();
    }

    // Arama Yapma
    public List<Kelime> kelimeAra(String aramaKelimesi) {
        List<Kelime> kelimeList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_KELIMELER + " WHERE " + KEY_YABANCI + " LIKE '%" + aramaKelimesi + "%'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        // ... (Cursor işlemleri yukarıdaki gibi)
        return kelimeList;
    }
}