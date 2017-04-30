package br.com.clairtonluz.moviemanagerapp.movie;


import br.com.clairtonluz.moviemanagerapp.generic.GenericModel;

public class Type extends GenericModel {
    private Integer id;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
