package br.com.clairtonluz.moviemanagerapp.movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

interface MovieRest {

    @GET("movies")
    Call<List<Movie>> list();
}
