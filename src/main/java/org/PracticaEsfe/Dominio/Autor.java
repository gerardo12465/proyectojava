package org.PracticaEsfe.Dominio;

public class Autor {
    private int id;
    private String nombreCompleto;
    private String nacionalidad;


    public Autor() {
    }

    public Autor(int id, String nombreCompleto, String nacionalidad) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.nacionalidad = nacionalidad;
    }

    public Autor(String nombreCompleto, String nacionalidad) {
        this.nombreCompleto = nombreCompleto;
        this.nacionalidad = nacionalidad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Nombre: " + nombreCompleto + ", Nacionalidad: " + nacionalidad;
    }
}
