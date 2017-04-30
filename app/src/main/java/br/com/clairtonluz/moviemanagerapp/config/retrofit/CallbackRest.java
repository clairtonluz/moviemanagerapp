package br.com.clairtonluz.moviemanagerapp.config.retrofit;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by clairton on 28/04/17.
 */

public abstract class CallbackRest<T> implements Callback<T> {

    private final Context context;
    private final RecyclerView.Adapter adapter;
    private final SwipeRefreshLayout mSwipeRefreshLayout;

    public CallbackRest(Context context) {
        this(context, null, null);
    }

    public CallbackRest(Context context, RecyclerView.Adapter adapter, SwipeRefreshLayout mSwipeRefreshLayout) {
        this.context = context;
        this.adapter = adapter;
        this.mSwipeRefreshLayout = mSwipeRefreshLayout;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            onSuccess(call, response);
            onComplete(call, Boolean.TRUE);
        } else {
            onUnsuccess(call, response);
            onComplete(call, Boolean.FALSE);
        }

    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        t.printStackTrace();
        Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        onComplete(call, Boolean.FALSE);
    }

    protected abstract void onSuccess(Call<T> call, Response<T> response);

    protected void onUnsuccess(Call<T> call, Response<T> response) {
        ResponseBody errorBody = response.errorBody();
        String message;
        if (errorBody != null) {
            try {
                ErrorRest error = new ObjectMapper().readValue(errorBody.string(), ErrorRest.class);
                message = error.message;
            } catch (Exception e) {
                e.printStackTrace();
                message = String.format("%d - %s", response.code(), response.message());
            }

        } else {
            message = String.format("%d - %s", response.code(), response.message());
        }

        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    protected void onComplete(Call<T> call, boolean success) {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
