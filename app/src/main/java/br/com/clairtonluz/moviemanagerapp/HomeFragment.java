package br.com.clairtonluz.moviemanagerapp;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.clairtonluz.moviemanagerapp.movie.Movie;
import br.com.clairtonluz.moviemanagerapp.movie.MovieService;
import br.com.clairtonluz.moviemanagerapp.movie.MoviesAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private MoviesAdapter adapter;
    private List<Movie> movieList;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private MovieService movieService;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieService = new MovieService(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);

        movieList = new ArrayList<>();
        adapter = new MoviesAdapter(getContext(), movieList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), getResources().getConfiguration().orientation);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                prepareMovies();
            }
        });

        prepareMovies();
        return view;
    }

    private void prepareMovies() {
        movieList.clear();
        movieService.list().enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                Log.e("teste", "response");
                if (response.isSuccessful()) {
                    movieList.addAll(response.body());
                } else {
                    Toast.makeText(getContext(), "Ocorreu uma falha!", Toast.LENGTH_LONG);
                }

                adapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG);
                mSwipeRefreshLayout.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }

        });
//        int[] covers = new int[]{
//                R.drawable.suicide_squad,
//                R.drawable.thor_ragnarok
//        };
//
//        String description1 = "Um time dos mais perigosos e encarcerados supervilões são contratados por uma agência secreta do governo, para combater uma poderosa entidade. No entanto, quando eles percebem que não foram escolhidos apenas para ter sucesso, mas também por sua óbvia culpa quando inevitavelmente falharem, terão que decidir se vale a pena ou não continuar correndo risco de morte.";
//        String description2 = "Thor: Ragnarok é um futuro filme americano de ação, fantasia e aventura, baseado no herói homônimo da Marvel Comics criado por Jack Kirby, Stan Lee e Larry Lieber. O filme é dirigido por Taika Waititi, com base no roteiro de Stephany Folsom.";
//        Movie a = new Movie("Esquadrão Suicida", description1, covers[0]);
//        Movie b = new Movie("Thor Ragnarok", description2, covers[1]);
//        movieList.add(a);
//        movieList.add(b);
//        movieList.add(a);
//        movieList.add(b);
//        movieList.add(a);
//        movieList.add(b);
//        movieList.add(a);
//        movieList.add(b);
//        movieList.add(a);
//        movieList.add(b);
//        movieList.add(a);
//        movieList.add(b);


    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}