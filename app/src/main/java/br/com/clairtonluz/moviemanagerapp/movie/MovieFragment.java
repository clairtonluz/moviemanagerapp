package br.com.clairtonluz.moviemanagerapp.movie;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import br.com.clairtonluz.moviemanagerapp.util.ConverterUtil;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class MovieFragment extends Fragment {
    private RecyclerView recyclerView;
    private MoviesAdapter adapter;
    private List<Movie> movieList;
    private List<Favorite> favoriteList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View noContent;

    private MovieService movieService;
    private FavoriteService favoriteService;
    private MainActivity.OnTabChangeListener onTabChangeListener;

    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieService = new MovieService(getContext());
        favoriteService = new FavoriteService(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);

        initFields(view);
        setListeners();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        prepareMovies();
        prepareFavorites();
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
        favoriteService.list().enqueue(new CallbackRest<List<Favorite>>(getContext(), adapter, mSwipeRefreshLayout) {
            @Override
            protected void onSuccess(Call<List<Favorite>> call, Response<List<Favorite>> response) {
                favoriteList.addAll(response.body());
            }
        });
    }

    private void prepareMovies() {
        mSwipeRefreshLayout.setRefreshing(true);
        movieList.clear();
        movieService.list().enqueue(new CallbackRest<List<Movie>>(getContext(), adapter, mSwipeRefreshLayout) {
            @Override
            protected void onSuccess(Call<List<Movie>> call, Response<List<Movie>> response) {
                movieList.addAll(response.body());
            }

            @Override
            protected void onComplete(Call<List<Movie>> call, boolean success) {
                super.onComplete(call, success);
                checkContent();
            }
        });
    }

    private void checkContent() {
        if (movieList.isEmpty()) {
            noContent.setVisibility(View.VISIBLE);
        } else {
            noContent.setVisibility(View.GONE);
        }
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
                prepareMovies();
                prepareFavorites();
            }
        });

        onTabChangeListener = new MainActivity.OnTabChangeListener() {
            @Override
            public void onTabSelected(int position) {
                if (position == MainActivity.TAB_HOME)
                    refresh();
            }
        };
        MainActivity.addTabListener(onTabChangeListener);
    }


}