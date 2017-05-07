package br.com.clairtonluz.moviemanagerapp.movie;

import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.clairtonluz.moviemanagerapp.config.retrofit.RestFactory;
import br.com.clairtonluz.moviemanagerapp.util.Constants;
import br.com.clairtonluz.moviemanagerapp.util.StoreUtil;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class MovieService {
    private MovieRest movieRest;

    public MovieService(Context context) {
        String token = StoreUtil.get(context, Constants.AUTHORIZATION);
        this.movieRest = RestFactory.createService(context, MovieRest.class, token);
    }

    public Call<List<Movie>> list() {
        return movieRest.list();
    }

    public Call<Movie> get(Integer id) {
        return movieRest.get(id);
    }

    public Call<Movie> save(Movie movie) {
        if (movie.getId() == null) {
            return movieRest.insert(movie);
        } else {
            return movieRest.update(movie.getId(), movie);
        }
    }

    public Call<ResponseBody> delete(Integer id) {
        return movieRest.delete(id);
    }

    public Call<List<Movie>> findByName(String name) {
        Map<String, String> data = new HashMap<>();
        data.put("name", name);
        return movieRest.query(data);
    }

}
