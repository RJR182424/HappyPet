/*
NOMBRE DEL PROYECTO: HAPPYPET
NOMBRE DEL AUTOR: HAT
FECHA DE CRACION: 22 - 03 - 2021
DESCRIPCION: Interface para comsumir los RESTApi del apartado de Servicio de la
aplicacion
 */

package com.example.happypetapp.apiRest;

import android.widget.CalendarView;

import com.example.happypetapp.model.Cita;
import com.example.happypetapp.model.Cliente;
import com.example.happypetapp.model.Mascota;
import com.example.happypetapp.model.Producto;
import com.example.happypetapp.model.Result;
import com.example.happypetapp.model.Servicio;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiRestInterface {

    //---------------- METODOS GET --------------------------//
    //Obtener los servicios mediante el servicio getAll.
    @GET("api/servicio/getAll")
    Call<List<Servicio>> getAllServicio();

    //Obtener los productos mediante el servicio getAll.
    @GET("api/producto/getAll")
    Call<List<Producto>> getAllProductos();

    //Obtener las mascotas mediante el servicio getAllCli exclusico para
    //un cliente unico, este manda como parametro el id del cliente en sesion.
    @GET("api/mascota/getAllMCli")
    Call<List<Mascota>> getAllMascotaCli(@Query("idC") int idC);

    //Metodo para obtener los datos de un solo cliente, este para obtener los datos a modificar.
    @GET("api/cliente/getOne")
    Call<Cliente> getOneCliente(@Query("idC") int idC);

    //Metodo para obtener los datos de la cita de un cliente unico
    //este manda como parametro el id del cliente en sesion.
    @GET("api/cita/getAllCli")
    Call<List<Cita>> getAllCitaCl(@Query("idCliente") int idCl);



    //---------------- METODOS POST -------------------------//
    //Login de la aplicacion
    @POST("api/login/verificar")
    @FormUrlEncoded
    Call<Result> userLogin(
            @Field("userN") String usu,
            @Field("passU") String pas);

    //Ingreso de un cliente al servidor, para el registro de usuarios.
    @POST("api/cliente/insert")
    @FormUrlEncoded
    Call<Result> insertCliente(
            @Field("mail") String email,
            @Field("nombre") String nombre,
            @Field("apellidoP") String apeP,
            @Field("apellidoM") String apeM,
            @Field("genero") String gen,
            @Field("calle") String cal,
            @Field("colonia") String col,
            @Field("cp") String cp,
            @Field("telefono") String tel,
            @Field("nomUsuario") String nomUsuario,
            @Field("password") String pass
    );

    //Modificacion de datos de una mascota.
    @POST("api/cliente/update")
    @FormUrlEncoded
    Call<Result> updateCliente(
            @Field("mail") String email,
            @Field("nombreA") String nom,
            @Field("apellidoP") String apeP,
            @Field("apellidoM") String apeM,
            @Field("genero") String gen,
            @Field("calle") String cal,
            @Field("colonia") String col,
            @Field("cp") String cp,
            @Field("telefono") String tel,
            @Field("nomUsuario") String nomUsuario,
            @Field("idC") int idC,
            @Field("idP") int idP,
            @Field("idU") int idU
    );

    //Ingreso de una mascota al servidor.
    @POST("api/mascota/insert")
    @FormUrlEncoded
    Call<Result> insertMascota(
            @Field("nombre") String nombre,
            @Field("edad") String edad,
            @Field("raza") String raza,
            @Field("especie") String especie,
            @Field("sexo") String sexo,
            @Field("descripcion") String descripcion,
            @Field("foto")  String foto,
            @Field("cliente") int cliente
    );

    //Modificacion de datos de una mascota.
    @POST("api/mascota/update")
    @FormUrlEncoded
    Call<Result> updateMascota(
            @Field("idMascota") int idMascota,
            @Field("nombre") String nombre,
            @Field("edad") String edad,
            @Field("raza") String raza,
            @Field("especie") String especie,
            @Field("sexo") String sexo,
            @Field("descripcion") String descripcion,
            @Field("foto") String foto,
            @Field("cliente") int cliente
    );

    //Eliminacion de una mascota
    @POST("api/mascota/delete")
    @FormUrlEncoded
    Call<Result> deleteMascota(@Field("idMascota") int idMascota);

    //Agrega una cita
    @POST("api/cita/insert")
    @FormUrlEncoded
    Call<Result> insertCita(
            @Field("mascota") int idMasc,
            @Field("cliente") int idCli,
            @Field("servicio") int idSer,
            @Field("fechaIni") String fechaI
    );

    //Modifica una cita
    @POST("api/cita/updateCl")
    @FormUrlEncoded
    Call<Result> updateCita(
            @Field("fechaIni") String fechaIni,
            @Field("mascota") int idMascota,
            @Field("servicio") int idServicio,
            @Field("idCita") int idCita
    );
}