package br.com.clairtonluz.moviemanagerapp.movie;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.clairtonluz.moviemanagerapp.R;
import br.com.clairtonluz.moviemanagerapp.generic.BackButtonActivity;

public class MovieDetailActivity extends BackButtonActivity {

    private ImageView toolbarImage;
    private TextView yearText;
    private TextView descriptionText;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        setupToolbar();
        initFields();
        Movie movie = getExtraSerializable("movie", Movie.class);
        showMovie(movie);
    }

    private void showMovie(Movie movie) {
        collapsingToolbarLayout.setTitle(movie.getName());
        Picasso.with(this)
                .load(movie.getUrlImage())
                .error(R.drawable.default_image)
                .into(toolbarImage);

        descriptionText.setText(movie.getDescription());
        yearText.setText(String.valueOf(movie.getYear()));
    }

    public void editAction(View view) {
        collapsingToolbarLayout.setTitle("teste");
        Snackbar.make(view, "Replace with your own actio2n", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void initFields() {
        toolbarImage = (ImageView) findViewById(R.id.toolbar_image);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        yearText = (TextView) findViewById(R.id.year_text);
        descriptionText = (TextView) findViewById(R.id.description_text);
    }
}
