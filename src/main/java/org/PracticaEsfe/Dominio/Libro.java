package org.PracticaEsfe.Dominio;

import java.sql.Date; // Importa java.sql.Date para el campo FechaPublicacion

public class Libro {
    private int id;
    private String titulo;
    private Date fechaPublicacion; // Nuevo campo para la fecha de publicación
    private int idAutor; // Se mantiene el nombre 'idAutor' por convención Java, pero se mapeará a 'IdAutor' en SQL

    public Libro() {
    }

    public Libro(int id, String titulo, Date fechaPublicacion, int idAutor) {
        this.id = id;
        this.titulo = titulo;
        this.fechaPublicacion = fechaPublicacion;
        this.idAutor = idAutor;
    }

    public Libro(String titulo, Date fechaPublicacion, int idAutor) {
        this.titulo = titulo;
        this.fechaPublicacion = fechaPublicacion;
        this.idAutor = idAutor;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public int getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(int idAutor) {
        this.idAutor = idAutor;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Título: " + titulo + ", Fecha de Publicación: " + fechaPublicacion + ", Autor ID: " + idAutor;
    }
}