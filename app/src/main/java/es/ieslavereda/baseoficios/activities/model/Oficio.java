package es.ieslavereda.baseoficios.activities.model;

import java.io.Serializable;

public class Oficio implements Serializable {
    private int idOficio;
    private String descripcion;
    private String image;

    public int getIdOficio() {
        return idOficio;
    }

    public void setIdOficio(int idOficio) {
        this.idOficio = idOficio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImage() {
        return image;
    }



    public Oficio(int idOficio, String descripcion, String image) {
        this.idOficio = idOficio;
        this.descripcion = descripcion;
        this.image = image;
    }
}