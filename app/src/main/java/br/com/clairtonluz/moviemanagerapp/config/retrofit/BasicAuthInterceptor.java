package br.com.clairtonluz.moviemanagerapp.config.retrofit;

import android.util.Base64;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by clairton on 28/04/17.
 */

public class BasicAuthInterceptor implements Interceptor {
    private String basicAuthToken;

    public BasicAuthInterceptor(String username, String password) {
        String credentials = username + ":" + password;
        this.basicAuthToken = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder()
                .header("Authorization", basicAuthToken)
                .header("Accept", "application/json")
                .method(original.method(), original.body());

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
