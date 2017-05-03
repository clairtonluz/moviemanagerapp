package br.com.clairtonluz.moviemanagerapp.config.retrofit;

import android.content.Context;
import android.net.http.HttpResponseCache;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import br.com.clairtonluz.moviemanagerapp.R;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RestFactory {
    private static Retrofit.Builder builder;
    private static Cache cache;

    private static Retrofit.Builder getBuilder(Context context) {
        if (builder == null) {
            builder = new Retrofit.Builder().baseUrl(context.getString(R.string.URL))
                    .addConverterFactory(JacksonConverterFactory.create());
        }

        return builder;
    }

    public static <S> S createService(Context context, Class<S> serviceClass) {
        return createService(context, serviceClass, null);
    }

    public static <S> S createService(Context context, Class<S> serviceClass, String authorization) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if (authorization != null) {
            httpClient.addInterceptor(new BasicAuthInterceptor(authorization));
        }

        OkHttpClient client = httpClient.cache(getCache())
                .addNetworkInterceptor(new RewriteResponseInterceptor())
                .addInterceptor(new RewriteResponseOffilineInterceptor(context))
                .addInterceptor(new LogInterceptor())
                .addInterceptor(new CheckAuthInterceptor(context))
                .retryOnConnectionFailure(true)
                .build();

        Retrofit retrofit = getBuilder(context).client(client).build();
        return retrofit.create(serviceClass);
    }

    public static Cache getCache() {
        return cache;
    }

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
}