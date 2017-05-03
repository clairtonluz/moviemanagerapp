package br.com.clairtonluz.moviemanagerapp.favorite;

import android.content.Context;

import java.util.List;

import br.com.clairtonluz.moviemanagerapp.config.retrofit.RestFactory;
import br.com.clairtonluz.moviemanagerapp.movie.Movie;
import br.com.clairtonluz.moviemanagerapp.util.Constants;
import br.com.clairtonluz.moviemanagerapp.util.StoreUtil;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class FavoriteService {
    private FavoriteRest favoriteRest;

    public FavoriteService(Context context) {
        String token = StoreUtil.get(context, Constants.AUTHORIZATION);
        this.favoriteRest = RestFactory.createService(context, FavoriteRest.class, token);
    }

    public Call<List<Favorite>> list() {
        return favoriteRest.list();
    }

    public Call<Favorite> save(Favorite favorite) {
        Call<Favorite> call;
        if (favorite.getId() == null) {
            call = favoriteRest.insert(favorite);
        } else {
            call = favoriteRest.update(favorite.getId(), favorite);
        }

        return call;
    }

    public Call<ResponseBody> delete(Favorite favorite) {
        return favoriteRest.delete(favorite.getId());
    }


    public static boolean isFavorite(List<Favorite> favoriteList, Movie movie) {
        return getFavorite(favoriteList, movie) != null;
    }

    public static Favorite getFavorite(List<Favorite> favoriteList, Movie movie) {
        for (Favorite favorite : favoriteList) {
            if (favorite.getMovie().equals(movie)) {
                return favorite;
            }
        }
        return null;
    }

}
