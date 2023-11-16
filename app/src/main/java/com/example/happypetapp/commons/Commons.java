package com.example.happypetapp.commons;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

public class Commons {

    public static final String BASE_URL = "http://192.168.100.23:8084/HappyPetAppWEB/";


    //No se ha comprobado su funcionamiento
    public static AlertDialog.Builder constructorAlert(@NonNull String mensaje,
                                                       @NotNull String textPosBtn,
                                                       @NotNull String textNegBtn,
                                                       @NotNull String titulo,
                                                       @NotNull Context ctx,
                                                       @NotNull DialogInterface.OnClickListener accionPositiva,
                                                       @NotNull DialogInterface.OnClickListener accionNegativa){


        AlertDialog.Builder blr = new AlertDialog.Builder(ctx);
        blr.setTitle(titulo);
        blr.setMessage(mensaje);
        blr.setPositiveButton(textPosBtn, accionPositiva);
        blr.setNegativeButton(textNegBtn,accionNegativa);

        return blr;
    }
}
