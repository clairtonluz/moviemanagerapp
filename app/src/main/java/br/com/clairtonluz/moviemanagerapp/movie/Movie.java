package br.com.clairtonluz.moviemanagerapp.movie;

import java.util.List;

public class Movie {
    private Integer id;
    private String name;
    private String description;
    private List<Type> types;
    private Integer year;
    private int thumbnail;

    public Movie() {
    }

    public Movie(String name, String description, int thumbnail) {
        this.name = name;
        this.thumbnail = thumbnail;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
