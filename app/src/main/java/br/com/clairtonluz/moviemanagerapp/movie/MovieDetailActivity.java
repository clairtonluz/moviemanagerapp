package br.com.clairtonluz.moviemanagerapp.movie;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import br.com.clairtonluz.moviemanagerapp.R;
import br.com.clairtonluz.moviemanagerapp.generic.BackButtonActivity;

public class MovieDetailActivity extends BackButtonActivity {

    private ImageView toolbarImage;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        toolbarImage = (ImageView) findViewById(R.id.toolbar_image);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        setupToolbar();
        Movie movie = getExtraSerializable("movie", Movie.class);
        showMovie(movie);
    }

    private void showMovie(Movie movie) {
        collapsingToolbarLayout.setTitle(movie.getName());
        Picasso.with(this)
                .load(movie.getUrlImage())
                .placeholder(R.drawable.loading_spinner)
                .error(R.drawable.default_image)
                .into(toolbarImage);
    }

    public void editAction(View view) {
        collapsingToolbarLayout.setTitle("teste");
        Snackbar.make(view, "Replace with your own actio2n", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
