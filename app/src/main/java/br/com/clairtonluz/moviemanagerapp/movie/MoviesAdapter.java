package br.com.clairtonluz.moviemanagerapp.movie;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.clairtonluz.moviemanagerapp.R;
import br.com.clairtonluz.moviemanagerapp.favorite.Favorite;
import br.com.clairtonluz.moviemanagerapp.favorite.FavoriteService;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private final List<Movie> movieList;
    private final List<Favorite> favoriteList;
    private final OnFavoriteListener onFavoriteListener;
    private Animation heartbeatAnimation;

    public MoviesAdapter(List<Movie> movieList) {
        this(null, movieList, null, null);
    }

    public MoviesAdapter(Context context, List<Movie> movieList, List<Favorite> favoriteList, OnFavoriteListener onFavoriteListener) {
        this.movieList = movieList;
        this.favoriteList = favoriteList;
        this.onFavoriteListener = onFavoriteListener;
        if (context != null)
            this.heartbeatAnimation = AnimationUtils.loadAnimation(context, R.anim.heartbeat);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, description;
        public ImageView thumbnail, favorite;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            favorite = (ImageView) view.findViewById(R.id.favorite);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Movie movie = movieList.get(position);
        final Context context = holder.title.getContext();
        holder.title.setText(movie.getName());
        holder.description.setText(movie.getDescription());
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MovieDetailActivity.class);
                intent.putExtra("movie", movie);
                context.startActivity(intent);
            }
        });

        if (movie.getUrlImage() != null) {
            Uri uri = Uri.parse(movie.getUrlImage());
            Picasso.with(context)
                    .load(uri)
                    .error(R.drawable.default_image)
                    .into(holder.thumbnail);
        } else {
            Picasso.with(context)
                    .load(R.drawable.default_image)
                    .into(holder.thumbnail);
        }


        if (favoriteList == null || onFavoriteListener == null) {
            holder.favorite.setVisibility(View.GONE);
        } else {
            if (FavoriteService.isFavorite(favoriteList, movie)) {
                holder.favorite.setImageResource(R.drawable.ic_favorite_white_24dp);
                holder.favorite.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary));
            } else {
                holder.favorite.setColorFilter(null);
                holder.favorite.setImageResource(R.drawable.ic_favorite_border_white_24dp);
            }

            holder.favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.favorite.startAnimation(heartbeatAnimation);
                    Favorite favorite = FavoriteService.getFavorite(favoriteList, movie);
                    onFavoriteListener.onFavorite(movie, favorite);
                }
            });
        }

    }

    public static interface OnFavoriteListener {
        void onFavorite(Movie movie, Favorite favorite);
    }

}
