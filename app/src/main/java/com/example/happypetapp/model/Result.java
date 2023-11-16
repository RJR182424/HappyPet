/*
NOMBRE DEL PROYECTO: HAPPYPET
NOMBRE DEL AUTOR: HAT
FECHA DE CRACION: 22 - 03 - 2021
DESCRIPCION: Modelo especial para las respuestas de los
servivicios POST de la aplicacion.
 */
package com.example.happypetapp.model;

public class Result {
    private String result;
    private String error;

    //GETTERS
    public String getResult() {
        return result;
    }

    public String getError() {
        return error;
    }

    //SETTERS
    public void setResult(String result) {
        this.result = result;
    }

    public void setError(String error) {
        this.error = error;
    }
}
