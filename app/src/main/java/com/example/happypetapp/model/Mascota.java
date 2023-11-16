/*
NOMBRE DEL PROYECTO: HAPPYPET
NOMBRE DEL AUTOR: HAT
FECHA DE CRACION: 22 - 03 - 2021
 */

package com.example.happypetapp.model;

public class Mascota {
    private int idMascota;
    private String nombre;
    private String edad;
    private String raza;
    private String especie;
    private String sexo;
    private String descripcion;
    private int estatus;
    private String foto;
    private Cliente cliente;

    public Mascota() {
    }

    public Mascota(String nombre, String edad, String raza, String especie,
                   String sexo, String descripcion, int estatus, String foto,Cliente cliente) {
        this.nombre = nombre;
        this.edad = edad;
        this.raza = raza;
        this.especie = especie;
        this.sexo = sexo;
        this.descripcion = descripcion;
        this.estatus = estatus;
        this.foto = foto;
        this.cliente = cliente;
    }

    public Mascota(int idMascota, String nombre, String edad, String raza, String especie,
                   String sexo, String descripcion, int estatus, String foto, Cliente cliente) {
        this.idMascota = idMascota;
        this.nombre = nombre;
        this.edad = edad;
        this.raza = raza;
        this.especie = especie;
        this.sexo = sexo;
        this.descripcion = descripcion;
        this.estatus = estatus;
        this.foto = foto;
        this.cliente = cliente;
    }

    //GETTERS
    public int getIdMascota() {
        return idMascota;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEdad() {
        return edad;
    }

    public String getRaza() {
        return raza;
    }

    public String getEspecie() {
        return especie;
    }

    public String getSexo(){
        return sexo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getEstatus() {
        return estatus;
    }

    public String getFoto() {
        return foto;
    }

    public Cliente getCliente() {
        return cliente;
    }

    //SETTERS
    public void setIdMascota(int idMascota) {
        this.idMascota = idMascota;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public void setSexo(String sexo){ this.sexo = sexo; }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public void setFoto(String foto) { this.foto = foto; }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
