package com.example.kelimehavuzudeep;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import java.util.Locale;

public class LocaleHelper {
    private static final String SELECTED_LANGUAGE = "Dil";

    public static void setLocale(Context context, String lang) {
        SharedPreferences preferences = context.getSharedPreferences("Ayarlar", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SELECTED_LANGUAGE, lang);
        editor.apply();
        updateResources(context, lang);
    }

    public static Context updateResources(Context context, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration config = new Configuration(resources.getConfiguration());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        return context; // Bu satır kritik!
    }

    public static String getLanguage(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("Ayarlar", Context.MODE_PRIVATE);
        return preferences.getString(SELECTED_LANGUAGE, "tr");
    }
}