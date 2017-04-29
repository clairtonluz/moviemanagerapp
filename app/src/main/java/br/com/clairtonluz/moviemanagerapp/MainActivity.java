package br.com.clairtonluz.moviemanagerapp;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import br.com.clairtonluz.moviemanagerapp.config.retrofit.RestFactory;
import br.com.clairtonluz.moviemanagerapp.favorite.FavoriteFragment;
import br.com.clairtonluz.moviemanagerapp.movie.MovieFragment;

public class MainActivity extends AppCompatActivity {

    public static final int TAB_HOME = 0;
    public static final int TAB_FAVORITE = 1;
    private static final List<OnTabChangeListener> LISTENERS = new ArrayList<>();
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private int[] tabIcons = {
            R.drawable.ic_home_black_24dp,
            R.drawable.ic_favorite_black_24dp
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        RestFactory.carregarCache(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        RestFactory.armazenarCache();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(TAB_HOME).setIcon(tabIcons[TAB_HOME]);
        tabLayout.getTabAt(TAB_FAVORITE).setIcon(tabIcons[TAB_FAVORITE]);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                for (OnTabChangeListener it : LISTENERS) {
                    it.onTabSelected(tab.getPosition());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MovieFragment(), getString(R.string.title_home));
        adapter.addFragment(new FavoriteFragment(), getString(R.string.title_favorite));
        viewPager.setAdapter(adapter);

    }


    public static void addTabListener(OnTabChangeListener onTabChangeListener) {
        LISTENERS.add(onTabChangeListener);
    }

    public static void removeTabListener(OnTabChangeListener onTabChangeListener) {
        LISTENERS.remove(onTabChangeListener);
    }

    public interface OnTabChangeListener {
        void onTabSelected(int position);
    }
}
