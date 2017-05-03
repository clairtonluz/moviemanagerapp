package br.com.clairtonluz.moviemanagerapp.config.retrofit;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BasicAuthInterceptor implements Interceptor {
    private String authentication;

    public BasicAuthInterceptor(String authentication) {
        this.authentication = authentication;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder()
                .header("Authorization", authentication)
                .header("Accept", "application/json")
                .method(original.method(), original.body());

        Request request = requestBuilder.build();
        Response response = chain.proceed(request);


        return response;
    }
}
