package br.com.clairtonluz.moviemanagerapp.favorite;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.com.clairtonluz.moviemanagerapp.MainActivity;
import br.com.clairtonluz.moviemanagerapp.R;
import br.com.clairtonluz.moviemanagerapp.config.GridSpacingItemDecoration;
import br.com.clairtonluz.moviemanagerapp.config.retrofit.CallbackRest;
import br.com.clairtonluz.moviemanagerapp.favorite.Favorite;
import br.com.clairtonluz.moviemanagerapp.favorite.FavoriteService;
import br.com.clairtonluz.moviemanagerapp.movie.Movie;
import br.com.clairtonluz.moviemanagerapp.movie.MoviesAdapter;
import br.com.clairtonluz.moviemanagerapp.util.ConverterUtil;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;


public class FavoriteFragment extends Fragment {

    private RecyclerView recyclerView;
    private MoviesAdapter adapter;
    private List<Movie> movieList;
    private List<Favorite> favoriteList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View noContent;

    private FavoriteService favoriteService;
    private MainActivity.OnTabChangeListener onTabChangeListener;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e("teste", "onCreate");
        super.onCreate(savedInstanceState);
        favoriteService = new FavoriteService(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("teste", "onCreateView");
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        initFields(view);
        setListeners();
        prepareFavorites();
        return view;
    }

    @Override
    public void onDestroyView() {
        MainActivity.removeTabListener(onTabChangeListener);
        super.onDestroyView();
    }

    public void refresh() {
        prepareFavorites();
    }

    private void prepareFavorites() {
        mSwipeRefreshLayout.setRefreshing(true);
        favoriteList.clear();
        movieList.clear();
        favoriteService.list().enqueue(new CallbackRest<List<Favorite>>(getContext(), adapter, mSwipeRefreshLayout) {
            @Override
            protected void onSuccess(Call<List<Favorite>> call, Response<List<Favorite>> response) {
                favoriteList.addAll(response.body());
                for (Favorite favorite : favoriteList) {
                    movieList.add(favorite.getMovie());
                }
            }

            @Override
            protected void onComplete(Call<List<Favorite>> call, boolean success) {
                super.onComplete(call, success);
                if (movieList.isEmpty()) {
                    noContent.setVisibility(View.VISIBLE);
                } else {
                    noContent.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initFields(View view) {
        movieList = new ArrayList<>();
        favoriteList = new ArrayList<>();

        noContent = view.findViewById(R.id.no_content);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);

        adapter = new MoviesAdapter(movieList, favoriteList, new MoviesAdapter.OnFavoriteListener() {
            @Override
            public void onFavorite(Movie movie, Favorite favorite) {
                if (favorite == null) {
                    favorite = new Favorite(movie);
                    favoriteService.save(favorite).enqueue(new CallbackRest<Favorite>(getContext()) {
                        @Override
                        protected void onSuccess(Call<Favorite> call, Response<Favorite> response) {
                            prepareFavorites();
                        }
                    });
                } else {
                    favoriteService.delete(favorite).enqueue(new CallbackRest<ResponseBody>(getContext()) {
                        @Override
                        protected void onSuccess(Call<ResponseBody> call, Response<ResponseBody> response) {
                            prepareFavorites();
                        }
                    });
                }

            }
        });

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), getResources().getConfiguration().orientation);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, ConverterUtil.dpToPx(getContext(), 10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setListeners() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                prepareFavorites();
            }
        });

        onTabChangeListener = new MainActivity.OnTabChangeListener() {
            @Override
            public void onTabSelected(int position) {
                if (position == MainActivity.TAB_FAVORITE)
                    refresh();
            }
        };
        MainActivity.addTabListener(onTabChangeListener);
    }


}
