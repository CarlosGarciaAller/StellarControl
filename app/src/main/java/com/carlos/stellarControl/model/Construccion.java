package com.carlos.stellarControl.model;

import java.util.Map;

public class Construccion {
    private String nombre, imagen, descripcion;
    private int costeMetal, costeCristal, costeDeuterio, costeEnergia, cantidad;
    //private Map requisitosInvestigaciones, requisitosInstalaciones;

    public Construccion() {

    }

    public Construccion(String nombre, String imagen, String descripcion, int costeMetal, int costeCristal, int costeDeuterio, int costeEnergia, int cantidad, Map requisitosInvestigaciones, Map requisitosInstalaciones) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.descripcion = descripcion;
        this.costeMetal = costeMetal;
        this.costeCristal = costeCristal;
        this.costeDeuterio = costeDeuterio;
        this.costeEnergia = costeEnergia;
        this.cantidad = cantidad;
        /*this.requisitosInvestigaciones = requisitosInvestigaciones;
        this.requisitosInstalaciones = requisitosInstalaciones;*/
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

    /*public Map getRequisitosInvestigaciones() {
        return requisitosInvestigaciones;
    }

    public void setRequisitosInvestigaciones(Map requisitosInvestigaciones) {
        this.requisitosInvestigaciones = requisitosInvestigaciones;
    }

    public Map getRequisitosInstalaciones() {
        return requisitosInstalaciones;
    }

    public void setRequisitosInstalaciones(Map requisitosInstalaciones) {
        this.requisitosInstalaciones = requisitosInstalaciones;
    }*/
}
