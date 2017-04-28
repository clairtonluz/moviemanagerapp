package br.com.clairtonluz.moviemanagerapp.config.retrofit;

import android.content.Context;
import android.net.http.HttpResponseCache;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestFactory {
    public static final String API_BASE_URL = "http://192.168.2.7:8080/api/";

    private static Cache cache;

    // Esse metodo deve ser chamado no onCreate da sua MainActivity
    public static void carregarCache(Context context) {
        try {
            File httpCacheDir = new File(context.getCacheDir(), "http");
            long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
            cache = new Cache(httpCacheDir, httpCacheSize);
            HttpResponseCache.install(httpCacheDir, httpCacheSize);
        } catch (IOException e) {
            Log.i("App", "HTTP response cache installation failed:", e);
        }
    }

    // Esse metodo deve ser chamado no onStop da sua MainActivity
    public static void armazenarCache() {
        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if (cache != null) {
            cache.flush();
        }
    }

    private static Retrofit.Builder builder = new Retrofit.Builder().baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());


    public static Cache getCache() {
        return cache;
    }

    public static Retrofit builder() {
        return builder.build();
    }

    public static <S> S createService(Context context, Class<S> serviceClass) {
        return createService(context, serviceClass, null, null);
    }

    public static <S> S createService(Context context, Class<S> serviceClass, String username, String password) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if (username != null && password != null) {
            httpClient.addInterceptor(new BasicAuthInterceptor(username, password));
        }

        OkHttpClient client = httpClient.cache(getCache())
                .addNetworkInterceptor(new RewriteResponseInterceptor())
//                .addNetworkInterceptor(new RetryInterceptor())
                .addInterceptor(new RewriteResponseOffilineInterceptor(context))
                .addInterceptor(new LogInterceptor())
                .retryOnConnectionFailure(true)
                .build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }
}