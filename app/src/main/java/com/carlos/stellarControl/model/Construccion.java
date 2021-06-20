package com.carlos.stellarControl.model;

public class Construccion {
    private String nombre, imagen, descripcion;
    private int costeMetal, costeCristal, costeDeuterio, costeEnergia, cantidad;

    public Construccion() {

    }

    public Construccion(String nombre, String imagen, String descripcion,
                        int costeMetal, int costeCristal, int costeDeuterio, int costeEnergia, int cantidad) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.descripcion = descripcion;
        this.costeMetal = costeMetal;
        this.costeCristal = costeCristal;
        this.costeDeuterio = costeDeuterio;
        this.costeEnergia = costeEnergia;
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCosteMetal() {
        return costeMetal;
    }

    public void setCosteMetal(int costeMetal) {
        this.costeMetal = costeMetal;
    }

    public int getCosteCristal() {
        return costeCristal;
    }

    public void setCosteCristal(int costeCristal) {
        this.costeCristal = costeCristal;
    }

    public int getCosteDeuterio() {
        return costeDeuterio;
    }

    public void setCosteDeuterio(int costeDeuterio) {
        this.costeDeuterio = costeDeuterio;
    }

    public int getCosteEnergia() {
        return costeEnergia;
    }

    public void setCosteEnergia(int costeEnergia) {
        this.costeEnergia = costeEnergia;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
