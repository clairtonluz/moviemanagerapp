package br.com.clairtonluz.moviemanagerapp.util;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;

public class ExtraUtil {

    private ExtraUtil() {
    }

    public static int dpToPx(Context context, int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    public static Integer getExtraInt(Bundle extras, String key) {
        Integer value = null;
        if (extras != null) {
            int anInt = extras.getInt(key, -1);
            if (anInt != -1)
                value = anInt;
        }
        return value;
    }

    public static String getExtraString(Bundle extras, String key) {
        String value = null;
        if (extras != null) {
            value = extras.getString(key);
        }
        return value;
    }

    public static <T> T getExtraSerializable(Bundle extras, String key, Class<T> tClass) {
        T value = null;
        if (extras != null) {
            value = (T) extras.getSerializable(key);
        }
        return value;
    }

}
