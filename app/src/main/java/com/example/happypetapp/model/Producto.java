/*
NOMBRE DEL PROYECTO: HAPPYPET
NOMBRE DEL AUTOR: HAT
FECHA DE CRACION: 22 - 03 - 2021
 */

package com.example.happypetapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Producto {
    @SerializedName("idProducto")
    @Expose
    private int idProducto;

    @SerializedName("nombrepr")
    @Expose
    private String nombrepr;

    @SerializedName("descripccion")
    @Expose
    private String descripccion;

    @SerializedName("precio")
    @Expose
    private double precio;

    @SerializedName("proveedor")
    @Expose
    private String proveedor;

    @SerializedName("foto")
    @Expose
    private String foto;

    @SerializedName("estatus")
    @Expose
    private boolean estatus;

    public Producto() {
    }

    public Producto(String nombrepr, String descripccion, double precio, String proveedor,
                    String foto, boolean estatus) {
        this.nombrepr = nombrepr;
        this.descripccion = descripccion;
        this.precio = precio;
        this.proveedor = proveedor;
        this.foto = foto;
        this.estatus = estatus;
    }

    public Producto(int idProducto, String nombrepr, String descripccion, double precio,
                    String proveedor, String foto, boolean estatus) {
        this.idProducto = idProducto;
        this.nombrepr = nombrepr;
        this.descripccion = descripccion;
        this.precio = precio;
        this.proveedor = proveedor;
        this.foto = foto;
        this.estatus = estatus;
    }

    //GETTERS
    public int getIdProducto() {
        return idProducto;
    }

    public String getNombrepr() {
        return nombrepr;
    }

    public String getDescripccion() {
        return descripccion;
    }

    public double getPrecio() {
        return precio;
    }

    public String getProveedor() {
        return proveedor;
    }

    public String getFoto() {
        return foto;
    }

    public boolean isEstatus() {
        return estatus;
    }

    //SETTERS
    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public void setNombrepr(String nombrepr) {
        this.nombrepr = nombrepr;
    }

    public void setDescripccion(String descripccion) {
        this.descripccion = descripccion;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void setEstatus(boolean estatus) {
        this.estatus = estatus;
    }
}
