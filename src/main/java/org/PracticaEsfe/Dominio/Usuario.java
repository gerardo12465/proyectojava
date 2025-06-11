package org.PracticaEsfe.Dominio;


public class Usuario {
    private int id;
    private String Nombre;
    private String Email;
    private String Contraseña;

    public Usuario() {

    }

    public Usuario(int id, String Nombre, String Email, String Contraseña) {
        this.id = id;
        this.Nombre = Nombre;
        this.Email = Email;
        this.Contraseña = Contraseña;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getContraseña() {
        return Contraseña;
    }

    public void setContraseña(String Contraseña) {
        this.Contraseña = Contraseña;
    }
}
