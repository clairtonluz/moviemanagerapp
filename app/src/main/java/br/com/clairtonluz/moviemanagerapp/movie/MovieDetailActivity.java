package br.com.clairtonluz.moviemanagerapp.movie;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.com.clairtonluz.moviemanagerapp.R;
import br.com.clairtonluz.moviemanagerapp.config.retrofit.CallbackRest;
import br.com.clairtonluz.moviemanagerapp.favorite.Favorite;
import br.com.clairtonluz.moviemanagerapp.favorite.FavoriteService;
import br.com.clairtonluz.moviemanagerapp.generic.BackButtonActivity;
import br.com.clairtonluz.moviemanagerapp.util.ExtraUtil;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class MovieDetailActivity extends BackButtonActivity {

    public static final int REQUEST_CODE = 10;
    private ImageView toolbarImage;
    private TextView yearText;
    private TextView descriptionText;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private FavoriteService favoriteService;
    private Movie movie;
    private List<Favorite> favoriteList;
    private MenuItem favoriteMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        setupToolbar();

        initFields();
        favoriteService = new FavoriteService(this);
        movie = ExtraUtil.getExtraSerializable(extras, "movie", Movie.class);

        prepareFavorites();
        showMovie(movie);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
        favoriteMenu = menu.findItem(R.id.action_favorite);
        checkFavorite();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                changeFavorite();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            movie = (Movie) data.getSerializableExtra("movie");
            showMovie(movie);
            Snackbar.make(toolbarImage, R.string.message_sucesso, Snackbar.LENGTH_LONG).show();
        }
    }

    private void changeFavorite() {
        Favorite favorite = FavoriteService.getFavorite(favoriteList, movie);
        if (favorite == null) {
            favorite = new Favorite(movie);
            favoriteService.save(favorite).enqueue(new CallbackRest<Favorite>(this) {
                @Override
                protected void onSuccess(Call<Favorite> call, Response<Favorite> response) {
                    prepareFavorites();
                }
            });
        } else {
            favoriteService.delete(favorite).enqueue(new CallbackRest<ResponseBody>(this) {
                @Override
                protected void onSuccess(Call<ResponseBody> call, Response<ResponseBody> response) {
                    prepareFavorites();
                }
            });
        }
    }

    private void prepareFavorites() {
        favoriteList.clear();
        favoriteService.list().enqueue(new CallbackRest<List<Favorite>>(this) {
            @Override
            protected void onSuccess(Call<List<Favorite>> call, Response<List<Favorite>> response) {
                favoriteList.addAll(response.body());
                checkFavorite();
            }
        });
    }

    private void checkFavorite() {
        if (favoriteMenu != null) {
            if (FavoriteService.isFavorite(favoriteList, movie)) {
                favoriteMenu.setIcon(R.drawable.ic_favorite_black_24dp);
            } else {
                favoriteMenu.setIcon(R.drawable.ic_favorite_border_black_24dp);
            }
        }
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


    private void initFields() {
        favoriteList = new ArrayList<>();
        toolbarImage = (ImageView) findViewById(R.id.toolbar_image);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        yearText = (TextView) findViewById(R.id.year_text);
        descriptionText = (TextView) findViewById(R.id.description_text);
    }
}
