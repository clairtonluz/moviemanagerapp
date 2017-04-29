package br.com.clairtonluz.moviemanagerapp.movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

interface MovieRest {

    @GET("movies")
    Call<List<Movie>> list();

    @GET("movies/{id}")
    Call<Movie> get(@Path("id") Integer id);
}
