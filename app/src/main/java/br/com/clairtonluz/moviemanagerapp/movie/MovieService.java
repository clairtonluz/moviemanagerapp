package br.com.clairtonluz.moviemanagerapp.movie;

import android.content.Context;

import java.util.List;

import br.com.clairtonluz.moviemanagerapp.config.retrofit.RestFactory;
import retrofit2.Call;

public class MovieService {
    private MovieRest movieRest;

    public MovieService(Context context) {
        this.movieRest = RestFactory.createService(context, MovieRest.class, "admin", "admin");
    }

    public Call<List<Movie>> list() {
        return movieRest.list();
    }

    public Call<Movie> get(Integer id) {
        return movieRest.get(id);
    }

}
