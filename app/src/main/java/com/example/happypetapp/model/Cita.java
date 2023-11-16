package com.example.happypetapp.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

public class Cita {

    @SerializedName("idCita")
    private int idCita;

    private String fechaIni;
    private String fechafin;

    @SerializedName("fechaHoraIniAsString")
    private String fechaHoraIniAsString;
    private String fechaHoraFinAsString;

    @SerializedName("estatus")
    private int estatus;

    @SerializedName("mascota")
    private Mascota mascota;

    @SerializedName("cliente")
    private Cliente cliente;

    @SerializedName("servicio")
    private Servicio servicio;

    public int getIdCita() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }

    public String getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(String fechaIni) {
        this.fechaIni = fechaIni;
    }

    public String getFechafin() {
        return fechafin;
    }

    public void setFechafin(String fechafin) {
        this.fechafin = fechafin;
    }

    public String getFechaHoraIniAsString() {
        return fechaHoraIniAsString;
    }

    public void setFechaHoraIniAsString(String fechaHoraIniAsString) {
        this.fechaHoraIniAsString = fechaHoraIniAsString;
    }

    public String getFechaHoraFinAsString() {
        return fechaHoraFinAsString;
    }

    public void setFechaHoraFinAsString(String fechaHoraFinAsString) {
        this.fechaHoraFinAsString = fechaHoraFinAsString;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }



    public Cita() {
    }

    public Cita(int idCita, String fechaIni, String fechafin, String fechaHoraIniAsString, String fechaHoraFinAsString, int estatus, Mascota mascota, Cliente cliente, Servicio servicio) {
        this.idCita = idCita;
        this.fechaIni = fechaIni;
        this.fechafin = fechafin;
        this.fechaHoraIniAsString = fechaHoraIniAsString;
        this.fechaHoraFinAsString = fechaHoraFinAsString;
        this.estatus = estatus;
        this.mascota = mascota;
        this.cliente = cliente;
        this.servicio = servicio;
    }

    public Cita(String fechaIni, String fechafin, String fechaHoraIniAsString, String fechaHoraFinAsString, int estatus, Mascota mascota, Cliente cliente, Servicio servicio) {
        this.fechaIni = fechaIni;
        this.fechafin = fechafin;
        this.fechaHoraIniAsString = fechaHoraIniAsString;
        this.fechaHoraFinAsString = fechaHoraFinAsString;
        this.estatus = estatus;
        this.mascota = mascota;
        this.cliente = cliente;
        this.servicio = servicio;
    }

    @Override
    public String toString() {
        return "Cita{" + "idCita=" + idCita + ", fechaIni=" + fechaIni + ", fechafin=" + fechafin + ", fechaHoraIniAsString=" + fechaHoraIniAsString + ", fechaHoraFinAsString=" + fechaHoraFinAsString + ", estatus=" + estatus + ", mascota=" + mascota + ", cliente=" + cliente + ", servicio=" + servicio + '}';
    }

    /**
     * Este metodo extrae la parte de la fecha sobre el string que va a recibir del servidor.
     * Esta respuesta tiene el siguente formato dd/MM/yyyy HH:mm:ss, solo se extrae la fecha.
     * @param date Cadena con la fechaHora con el formato dd/MM/yyyy HH:mm:ss.
     * @return  La fecha extraida
     */
    public String extraerFecha(@NotNull String date){
        return date.substring(0,10);
    }

    /**
     * Este metodo extrae la parte de la hora sobre el string que va a recibir del servidor.
     * Esta respuesta tiene el siguiente formato dd/MM/yyyy HH:mm:ss, solo se extrae la fecha.
     * @param date Cadena con la fechaHora con el formato dd/MM/yyyy HH:mm:ss.
     * @return  La Hora extraida.
     */
    public String extraerHora(@NotNull String date){
        return date.substring(11, 18);
    }

    /**
     * Este metodo une la fecha y hora obtenidos por los elementos visuales correspondientes.
     * Este los une utilizando el formato que necesita el servidor dd/MM/yyyy HH:mm:ss.
     * @param dateFecha La fecha obtenida por el calendario del dispositivo.
     * @param dateHora La hora obtenida por el Spinner del dispositivo
     * @return  La cadena resultante de la concatenacion de los parametros
     */
    public String concatFechaHora(@NotNull String dateFecha, @NotNull String dateHora){
        String fechaCompleta = dateFecha.concat(" ").concat(dateHora);

        return fechaCompleta;
    }

    /**
     * Este metodo reconstruye la fecha resivida del servidor a un formato aceptable para su
     * manipulacion y su registro a la BD.
     * 0123456789
     * yyyy/mm/dd
     * @param fecha_respuesta La respuesta fecha del servidor
     * @return El reformateo de la feche recivida.
     */
    public String construirResultadoFecha(String fecha_respuesta){
        String y, m, d;
        y = fecha_respuesta.substring(0, 4);
        m = fecha_respuesta.substring(5, 7);
        d = fecha_respuesta.substring(8);

        String fechaReCons = d + "/" + m + "/" + y;

        return fechaReCons;
    }
}
