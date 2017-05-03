package br.com.clairtonluz.moviemanagerapp.login;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

interface LoginRest {

    @GET("login")
    Call<User> login(@Header("Authorization") String authorization);
}
