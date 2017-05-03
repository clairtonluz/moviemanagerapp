package br.com.clairtonluz.moviemanagerapp.login;

import br.com.clairtonluz.moviemanagerapp.generic.GenericModel;

public class User extends GenericModel {
    private Integer id;
    private String name;
    private String username;
    private Boolean enabled;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
