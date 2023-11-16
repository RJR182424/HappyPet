/*
NOMBRE DEL PROYECTO: HAPPYPET
NOMBRE DEL AUTOR: HAT
FECHA DE CRACION: 22 - 03 - 2021
 */

package com.example.happypetapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cliente {
    @SerializedName("idCliente")
    private int idCliente;

    @SerializedName("correo")
    private String correo;

    @SerializedName("estatus")
    private boolean estatus;

    @SerializedName("Persona")
    private Persona persona;

    @SerializedName("Usuario")
    private Usuario usuario;

    public Cliente() {
    }

    public Cliente(String correo, boolean estatus, Persona persona, Usuario usuario) {
        this.correo = correo;
        this.estatus = estatus;
        this.persona = persona;
        this.usuario = usuario;
    }

    public Cliente(int idCliente, String correo, boolean estatus, Persona persona,
                   Usuario usuario) {
        this.idCliente = idCliente;
        this.correo = correo;
        this.estatus = estatus;
        this.persona = persona;
        this.usuario = usuario;
    }


    //GETTERS
    public int getIdCliente() {
        return idCliente;
    }

    public String getCorreo() {
        return correo;
    }

    public boolean isEstatus() {
        return estatus;
    }

    public Persona getPersona() {
        return persona;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    //SETTERS
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setEstatus(boolean estatus) {
        this.estatus = estatus;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
