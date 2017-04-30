package br.com.clairtonluz.moviemanagerapp.movie;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.clairtonluz.moviemanagerapp.R;
import br.com.clairtonluz.moviemanagerapp.generic.BackButtonActivity;

public class MovieDetailActivity extends BackButtonActivity {

    public static final int REQUEST_CODE = 10;
    private ImageView toolbarImage;
    private TextView yearText;
    private TextView descriptionText;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Movie movie;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        setupToolbar();

        initFields();
        movie = getExtraSerializable("movie", Movie.class);
        showMovie(movie);
    }

    private void showMovie(Movie movie) {
        collapsingToolbarLayout.setTitle(movie.getName());
        Picasso.with(this)
                .load(movie.getUrlImage())
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(toolbarImage);

        descriptionText.setText(movie.getDescription());
        yearText.setText(String.valueOf(movie.getYear()));
    }

    public void editAction(View view) {
        Intent intent = new Intent(this, MovieEditActivity.class);
        intent.putExtra("movie", movie);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            movie = getExtraSerializable("movie", Movie.class);
            showMovie(movie);
        }
    }

    private void initFields() {
        toolbarImage = (ImageView) findViewById(R.id.toolbar_image);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        yearText = (TextView) findViewById(R.id.year_text);
        descriptionText = (TextView) findViewById(R.id.description_text);
    }
}
