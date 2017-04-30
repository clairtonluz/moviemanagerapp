package br.com.clairtonluz.moviemanagerapp.movie;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import br.com.clairtonluz.moviemanagerapp.R;
import br.com.clairtonluz.moviemanagerapp.config.retrofit.CallbackRest;
import br.com.clairtonluz.moviemanagerapp.generic.BackButtonActivity;
import br.com.clairtonluz.moviemanagerapp.util.Constants;
import br.com.clairtonluz.moviemanagerapp.util.ExtraUtil;
import fr.ganfra.materialspinner.MaterialSpinner;
import retrofit2.Call;
import retrofit2.Response;

public class MovieEditActivity extends BackButtonActivity {

    private Movie movie;
    private TextInputEditText descriptionText;
    private MaterialSpinner yearSpinner;
    private TextInputEditText nameText;
    private TextInputEditText urlText;

    private MovieService movieService;
    private ImageView movieImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_edit);
        setupToolbar();
        initFields();
        movieService = new MovieService(this);
        movie = ExtraUtil.getExtraSerializable(extras, "movie", Movie.class);
        showMovie(movie);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                save();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void showMovie(Movie movie) {
        Picasso.with(this)
                .load(movie.getUrlImage())
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(movieImage);
        nameText.setText(movie.getName());
        descriptionText.setText(movie.getDescription());
        int position = Constants.getYEARS().indexOf(movie.getYear()) + 1;
        yearSpinner.setSelection(position);
        urlText.setText(movie.getUrlImage());
    }

    public void save() {
        movie.setName(nameText.getText().toString());
        movie.setDescription(descriptionText.getText().toString());
        movie.setYear((Integer) yearSpinner.getSelectedItem());
        movie.setUrlImage(urlText.getText().toString());

        movieService.save(movie).enqueue(new CallbackRest<Movie>(this) {
            @Override
            protected void onSuccess(Call<Movie> call, Response<Movie> response) {
                MovieEditActivity.this.movie = response.body();
                showMovie(MovieEditActivity.this.movie);
                Intent intent = new Intent();
                intent.putExtra("movie", movie);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void initFields() {
        nameText = (TextInputEditText) findViewById(R.id.name_text);
        movieImage = (ImageView) findViewById(R.id.movie_image);
        descriptionText = (TextInputEditText) findViewById(R.id.description_text);
        urlText = (TextInputEditText) findViewById(R.id.url_text);

        yearSpinner = (MaterialSpinner) findViewById(R.id.year_spinner);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Constants.getYEARS());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(adapter);

        urlText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Picasso.with(getApplicationContext())
                            .load(urlText.getText().toString())
                            .placeholder(R.drawable.default_image)
                            .error(R.drawable.default_image)
                            .into(movieImage);
                }
            }
        });


    }
}
