package br.com.clairtonluz.moviemanagerapp.util;

import android.content.Context;
import android.content.SharedPreferences;

public class StoreUtil {
    public static void put(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String get(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        return settings.getString(key, null);
    }


}
