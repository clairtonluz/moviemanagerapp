package br.com.clairtonluz.moviemanagerapp.config.retrofit;

import android.util.Log;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RetryInterceptor implements Interceptor {
    
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Response response = null;
        int tryCount = 0;
        while (response == null && tryCount < 3) {
            try {
                response = chain.proceed(request);
            } catch (SocketTimeoutException e) {
                tryCount++;
                Log.e(getClass().getName(), String.format("%s Timeout - %dº tentativa", request.url().toString(), tryCount));
            }
        }

        return response;
    }
}
