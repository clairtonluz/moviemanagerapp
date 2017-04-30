package br.com.clairtonluz.moviemanagerapp.generic;

import java.io.Serializable;
import java.util.Date;

public class GenericModel implements Serializable {

    protected Date createdAt;
    protected Date updatedAt;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
