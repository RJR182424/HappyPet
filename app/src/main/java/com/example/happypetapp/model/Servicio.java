/*
NOMBRE DEL PROYECTO: HAPPYPET
NOMBRE DEL AUTOR: HAT
FECHA DE CRACION: 22 - 03 - 2021
 */

package com.example.happypetapp.model;

public class Servicio {
    private int idServicio;
    private String nombreS;
    private String descrip;
    private double precio;
    private int tipos;
    private int estatus;

    public Servicio() {
    }

    public Servicio(String nombreS, String descrip, double precio, int tipos, int estatus) {
        this.nombreS = nombreS;
        this.descrip = descrip;
        this.precio = precio;
        this.tipos = tipos;
        this.estatus = estatus;
    }

    public Servicio(int idServicio, String nombreS, String descrip, double precio,
                    int tipos, int estatus) {
        this.idServicio = idServicio;
        this.nombreS = nombreS;
        this.descrip = descrip;
        this.precio = precio;
        this.tipos = tipos;
        this.estatus = estatus;
    }


    //GETTERS
    public int getIdServicio() {
        return idServicio;
    }

    public String getNombreS() {
        return nombreS;
    }

    public String getDescrip() {
        return descrip;
    }

    public double getPrecio() {
        return precio;
    }

    public int getTipos() {
        return tipos;
    }

    public int getEstatus() {
        return estatus;
    }

    //SETTERS
    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    public void setNombreS(String nombreS) {
        this.nombreS = nombreS;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setTipos(int tipos) {
        this.tipos = tipos;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }
}
