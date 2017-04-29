package br.com.clairtonluz.moviemanagerapp.generic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import br.com.clairtonluz.moviemanagerapp.R;

/**
 * Created by clairton on 29/04/17.
 */

public abstract class BackButtonActivity extends AppCompatActivity {

    private Bundle extras;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extras = getIntent().getExtras();
    }

    protected void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    protected Integer getExtraInt(String key) {
        Integer value = null;
        if (extras != null) {
            int anInt = extras.getInt(key, -1);
            if (anInt != -1)
                value = anInt;
        }
        return value;
    }

    protected String getExtraString(String key) {
        String value = null;
        if (extras != null) {
            value = extras.getString(key);
        }
        return value;
    }

    protected <T> T getExtraSerializable(String key, Class<T> tClass) {
        T value = null;
        if (extras != null) {
            value = (T) extras.getSerializable(key);
        }
        return value;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                // ProjectsActivity is my 'home' activity
                super.onBackPressed();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}
