package br.com.clairtonluz.moviemanagerapp.config.retrofit;

import android.util.Log;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class LogInterceptor implements Interceptor {

    public static final String TAG = "REST";
    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response;
        String message;
        try {
            long startNs = System.nanoTime();
            response = chain.proceed(request);
            long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
            message = String.format("status:%d %s %s - %dms", response.code(), request.method(), request.url().toString(), tookMs);
            if (response.isSuccessful()) {
                Log.d(TAG, message);
            } else {
                Log.e(TAG, message);
                Log.e(TAG, response.message());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, String.format("%s - %s", request.method(), request.url().toString()));
            throw e;
        }

        return response;
    }
}
