/*
NOMBRE DEL PROYECTO: HAPPYPET
NOMBRE DEL AUTOR: HAT
FECHA DE CRACION: 22 - 03 - 2021
 */

package com.example.happypetapp.model;

public class Persona {
    private int idPersona;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String genero;
    private String calle;
    private String colonia;
    private int cp;
    private String telefono;

    public Persona() {
    }

    public Persona(String nombre, String apellidoPaterno, String apellidoMaterno, String genero,
                   String calle, String colonia, int cp, String telefono) {
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.genero = genero;
        this.calle = calle;
        this.colonia = colonia;
        this.cp = cp;
        this.telefono = telefono;
    }

    public Persona(int idPersona, String nombre, String apellidoPaterno, String apellidoMaterno,
                   String genero, String calle, String colonia, int cp, String telefono) {
        this.idPersona = idPersona;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.genero = genero;
        this.calle = calle;
        this.colonia = colonia;
        this.cp = cp;
        this.telefono = telefono;
    }

    //GETTERS

    public int getIdPersona() {
        return idPersona;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public String getGenero() {
        return genero;
    }

    public String getCalle() {
        return calle;
    }

    public String getColonia() {
        return colonia;
    }

    public int getCp() {
        return cp;
    }

    public String getTelefono() {
        return telefono;
    }

    //SETTERS
    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public void setCp(int cp) {
        this.cp = cp;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
