package br.com.clairtonluz.moviemanagerapp.config.retrofit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RewriteResponseOffilineInterceptor implements Interceptor {
    private Context context;

    public RewriteResponseOffilineInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (request.method().equals("GET")) {
            String headerCache = request.header("Cache-Control"); // isso vai servir mais na frente para forçar um serviço não utilizar cache
            if (!isNetworkAvailable() && (headerCache == null || !headerCache.contains("no-cache"))) {
                int maxStale = 60 * 60 * 24 * 28; // 4 semanas
                request = request.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        }
        return chain.proceed(request);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}