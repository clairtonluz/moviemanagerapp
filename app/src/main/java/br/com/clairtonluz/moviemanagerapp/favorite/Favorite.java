package br.com.clairtonluz.moviemanagerapp.favorite;


import br.com.clairtonluz.moviemanagerapp.movie.Movie;

public class Favorite {
    private Integer id;
    private Integer userId;
    private Movie movie;

    public Favorite(Movie movie) {
        this.movie = movie;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Favorite favorite = (Favorite) o;

        return id != null ? id.equals(favorite.id) : favorite.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
