package br.com.clairtonluz.moviemanagerapp.favorite;

import java.util.List;

import br.com.clairtonluz.moviemanagerapp.movie.Movie;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

interface FavoriteRest {

    @GET("favorites")
    Call<List<Favorite>> list();

    @POST("favorites")
    Call<Favorite> insert(@Body Favorite favorite);

    @POST("favorites/{id}")
    Call<Favorite> update(@Path("id") Integer id, @Body Favorite favorite);

    @DELETE("favorites/{id}")
    Call<ResponseBody> delete(@Path("id") Integer id);
}
