package br.com.clairtonluz.moviemanagerapp.movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

interface MovieRest {

    @GET("movies")
    Call<List<Movie>> list();

    @GET("movies/{id}")
    Call<Movie> get(@Path("id") Integer id);

    @POST("movies")
    Call<Movie> insert(@Body Movie movie);

    @POST("movies/{id}")
    Call<Movie> update(@Path("id") Integer id, @Body Movie movie);
}
