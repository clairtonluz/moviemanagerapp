package br.com.clairtonluz.moviemanagerapp.config.retrofit;

import android.content.Context;
import android.content.Intent;

import java.io.IOException;

import br.com.clairtonluz.moviemanagerapp.login.LoginActivity;
import br.com.clairtonluz.moviemanagerapp.util.Constants;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CheckAuthInterceptor implements Interceptor {
    private Context context;

    public CheckAuthInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

        if (response.code() == Constants.HTTP_STATUS_UNAUTHORIZED && !request.url().toString().contains("/api/login")) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        }

        return response;
    }
}
