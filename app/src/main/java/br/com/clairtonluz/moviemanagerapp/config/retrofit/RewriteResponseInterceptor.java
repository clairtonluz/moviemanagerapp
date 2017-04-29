package br.com.clairtonluz.moviemanagerapp.config.retrofit;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class RewriteResponseInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        okhttp3.Response response = chain.proceed(chain.request());
        if (response.request().method().equals("GET")) {
            String cacheControl = response.header("Cache-Control");
            if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                    cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")) {
                int maxAge = 1;
                response = response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .removeHeader("Pragma")
                        .build();
            }
        }

        return response;
    }
}