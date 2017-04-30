package br.com.clairtonluz.moviemanagerapp.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public abstract class Constants {
    private static final List<Integer> YEARS = new ArrayList<>();

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
