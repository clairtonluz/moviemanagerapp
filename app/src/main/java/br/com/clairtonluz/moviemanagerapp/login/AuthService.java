package br.com.clairtonluz.moviemanagerapp.login;

import android.content.Context;
import android.content.Intent;
import android.util.Base64;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.clairtonluz.moviemanagerapp.MainActivity;
import br.com.clairtonluz.moviemanagerapp.config.retrofit.RestFactory;
import br.com.clairtonluz.moviemanagerapp.util.Constants;
import br.com.clairtonluz.moviemanagerapp.util.StoreUtil;
import retrofit2.Call;

public class AuthService {

    private final Context context;
    private LoginRest loginRest;

    public AuthService(Context context) {
        this.context = context;
        loginRest = RestFactory.createService(context, LoginRest.class);
    }

    public Call<User> login(String username, String password) {
        String basicAuthToken = createBasicAuthToken(username, password);
        return loginRest.login(basicAuthToken);
    }

    public static String createBasicAuthToken(String username, String password) {
        String credentials = username + ":" + password;
        return "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
    }

    public void storeCredentials(User user, String username, String password) {
        try {
            String json = new ObjectMapper().writeValueAsString(user);
            String token = AuthService.createBasicAuthToken(username, password);
            StoreUtil.put(context, Constants.AUTHORIZATION, token);
            StoreUtil.put(context, Constants.USER, json);
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void logout() {
        StoreUtil.put(context, Constants.AUTHORIZATION, null);
        StoreUtil.put(context, Constants.USER, null);
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}
