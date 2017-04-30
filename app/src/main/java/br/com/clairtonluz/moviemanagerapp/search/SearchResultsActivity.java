package br.com.clairtonluz.moviemanagerapp.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.clairtonluz.moviemanagerapp.R;
import br.com.clairtonluz.moviemanagerapp.config.GridSpacingItemDecoration;
import br.com.clairtonluz.moviemanagerapp.config.retrofit.CallbackRest;
import br.com.clairtonluz.moviemanagerapp.generic.BackButtonActivity;
import br.com.clairtonluz.moviemanagerapp.movie.Movie;
import br.com.clairtonluz.moviemanagerapp.movie.MovieService;
import br.com.clairtonluz.moviemanagerapp.movie.MoviesAdapter;
import br.com.clairtonluz.moviemanagerapp.util.ConverterUtil;
import retrofit2.Call;
import retrofit2.Response;

public class SearchResultsActivity extends BackButtonActivity {

    private RecyclerView recyclerView;
    private MoviesAdapter adapter;
    private List<Movie> movieList;
    private TextView titleText;
    private View noContent;
    private View loading;
    private MovieService movieService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        setupToolbar();
        initFields();

        movieService = new MovieService(this);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);

        return true;
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            search(query);
        }
    }

    private void search(String query) {
        titleText.setText(String.format("Resultados para: %s", query));
        if (TextUtils.isEmpty(query)) {
            movieList.clear();
            adapter.notifyDataSetChanged();
            checkContent();
        } else {
            loading.setVisibility(View.VISIBLE);
            movieService.findByName(query).enqueue(new CallbackRest<List<Movie>>(this, adapter, loading) {
                @Override
                protected void onSuccess(Call<List<Movie>> call, Response<List<Movie>> response) {
                    movieList.clear();
                    movieList.addAll(response.body());
                }

                @Override
                protected void onComplete(Call<List<Movie>> call, boolean success) {
                    super.onComplete(call, success);
                    checkContent();
                }
            });
        }
    }

    private void checkContent() {
        if (movieList.isEmpty()) {
            noContent.setVisibility(View.VISIBLE);
        } else {
            noContent.setVisibility(View.GONE);
        }
    }

    private void initFields() {
        movieList = new ArrayList<>();

        noContent = findViewById(R.id.no_content);
        loading = findViewById(R.id.loading);
        titleText = (TextView) findViewById(R.id.title_text);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        adapter = new MoviesAdapter(movieList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, getResources().getConfiguration().orientation);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, ConverterUtil.dpToPx(this, 10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}
