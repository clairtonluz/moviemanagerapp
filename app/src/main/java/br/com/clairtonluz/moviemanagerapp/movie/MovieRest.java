package br.com.clairtonluz.moviemanagerapp.movie;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

interface MovieRest {

    @GET("movies")
    Call<List<Movie>> list();

    @GET("movies/{id}")
    Call<Movie> get(@Path("id") Integer id);

    @GET("movies")
    Call<List<Movie>> query(@QueryMap Map<String, String> options);

    @POST("movies")
    Call<Movie> insert(@Body Movie movie);

    @POST("movies/{id}")
    Call<Movie> update(@Path("id") Integer id, @Body Movie movie);

    @DELETE("movies/{id}")
    Call<ResponseBody> delete(@Path("id") Integer id);
}
