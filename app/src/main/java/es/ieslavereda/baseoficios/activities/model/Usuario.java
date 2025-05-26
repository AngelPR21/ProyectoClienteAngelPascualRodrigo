package es.ieslavereda.baseoficios.activities.model;

import java.io.Serializable;

//Clase que muestra los datos de los usuarios
public class Usuario implements Serializable {
    private int idUsuario;
    private String nombre;
    private String apellidos;
    private int idOficio;

    public Usuario(String nombre, String apellidos, int idOficio) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.idOficio = idOficio;
    }
    //Constructor del Usuario
    public Usuario(int idUsuario, String nombre, String apellidos, int idOficio) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.idOficio = idOficio;
    }

    //Setters y Getters del Usuario

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public int getOficioIdOficio() {
        return idOficio;
    }

}