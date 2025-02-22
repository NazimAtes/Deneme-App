package com.example.kelimehavuzudeep;

public class Kelime {
    private int id;
    private String yabanci;
    private String ceviri;
    private String ornek;
    private long tarih;

    public Kelime() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getYabanci() { return yabanci; }
    public void setYabanci(String yabanci) { this.yabanci = yabanci; }
    public String getCeviri() { return ceviri; }
    public void setCeviri(String ceviri) { this.ceviri = ceviri; }
    public String getOrnek() { return ornek; }
    public void setOrnek(String ornek) { this.ornek = ornek; }
    public long getTarih() { return tarih; }
    public void setTarih(long tarih) { this.tarih = tarih; }
}