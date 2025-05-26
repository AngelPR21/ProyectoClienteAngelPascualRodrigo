package es.ieslavereda.baseoficios.activities.model;

import java.io.Serializable;

//Clase que muestra los datos de los oficios

public class Oficio implements Serializable {

    private int idOficio;
    private String descripcion;
    private String image;

    //Constructor del Oficio
    public Oficio(int idOficio, String descripcion, String image) {
        this.idOficio = idOficio;
        this.descripcion = descripcion;
        this.image = image;
    }

    //Setters y Getters del Oficio
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


}