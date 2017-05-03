package br.com.clairtonluz.moviemanagerapp.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public abstract class Constants {
    public static final int HTTP_STATUS_UNAUTHORIZED = 401;
    public static final String AUTHORIZATION = "Authorization";
    public static final String PREFS_NAME = "MyPrefsFile";
    private static final List<Integer> YEARS = new ArrayList<>();
    public static final String USER = "movie.manager.user";

    static {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (; currentYear >= 1900; currentYear--) {
            YEARS.add(currentYear);
        }
    }

    public static List<Integer> getYEARS() {
        return Collections.unmodifiableList(YEARS);
    }
}
