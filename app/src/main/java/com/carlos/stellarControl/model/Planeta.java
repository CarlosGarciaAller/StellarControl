package com.carlos.stellarControl.model;

public class Planeta {
    private String usuario;
    private String nombre;
    private String imagen;
    private int diametro;
    private int posicion;
    private int sistema;
    private Boolean colonia;

    public Planeta() {

    }

    public Planeta(String usuario, String nombre, String imagen, int diametro, int posicion, int sistema, boolean colonia) {
        this.usuario = usuario;
        this.nombre = nombre;
        this.imagen = imagen;
        this.diametro = diametro;
        this.posicion = posicion;
        this.sistema = sistema;
        this.colonia = colonia;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getImagen() {
        return imagen;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public int getDiametro() {
        return diametro;
    }

    public void setDiametro(int diametro) {
        this.diametro = diametro;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public int getSistema() {
        return sistema;
    }

    public void setSistema(int sistema) {
        this.sistema = sistema;
    }

    public Boolean getColonia() {
        return colonia;
    }

    public void setColonia(Boolean colonia) {
        this.colonia = colonia;
    }
}
