package com.example.happypetapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.happypetapp.apiRest.ApiRestInterface;
import com.example.happypetapp.commons.Commons;
import com.example.happypetapp.model.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    //EDITTEXT
    EditText txtNombreUL;
    EditText txtPassUl;

    //TEXTVIEW
    TextView lbRegistro;

    //BUTTONS
    Button btnIniciarS;

    int idC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtNombreUL = findViewById(R.id.txtNombreUL);
        txtPassUl = findViewById(R.id.txtPassUL);
        lbRegistro = findViewById(R.id.lbRegistro);
        btnIniciarS = findViewById(R.id.btnIniciarS);

        btnIniciarS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userIn = txtNombreUL.getText().toString();
                String pasIn  = txtPassUl.getText().toString();

                if(userIn.equals("") || pasIn.equals("")){
                    Toast.makeText(
                            getApplicationContext(),
                            "Registro Incompleto" + "\n" + "Llene todos los campos.",
                            Toast.LENGTH_SHORT).show();
                }else{
                    consumirServicioLogin(userIn, pasIn);
                }
            }
        });

        lbRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent changeRegis = new Intent(v.getContext(), RegistroActivity.class);
                startActivity(changeRegis);
            }
        });
    }

    private void consumirServicioLogin(String nombreUsu, String passUsu){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Commons.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiRestInterface callLogin = retrofit.create(ApiRestInterface.class);

        Call<Result> loginService = callLogin.userLogin(nombreUsu, passUsu);

        loginService.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(
                            getApplicationContext(),
                            "Ha ocurrido un error con la insercion " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }else{
                    //Variable para obtener el id del usuario, este retornado por el servidor.
                    int numId = Integer.parseInt(response.body().getResult());
                    /*
                    Si el envio de datos al servidor fue correcto, se entrara en un arbol de
                    condiciones. Primero, si el Result.getError es diferente de nulo, mostrara el
                    error obtenido por el servidor y lo mostrara mediante Toast. Si es nulo (sin
                    errores del lado del servidor), se entrara en otra sentencia if. En esta se evalua
                    si el valor obtenido "numId" es menor o igual a 0, si es asi, mostrara un Toast
                    de usuario no encontrado; si es mayor a 0, se define la variable idC con el numero
                    recibido, ademas de crear el intent para pasar a la actividad principal. Este
                    tendra informacion extra, que sera el idC con el idCliente, este valor con la key
                    "IDCL
                     */
                    if(response.body().getError() != null){
                        Toast.makeText(
                                getApplicationContext(),
                                "Ocurrio un error en el servidor:" + "\n",
                                Toast.LENGTH_LONG).show();
                    }else {

                        if(numId <= 0){
                            Toast.makeText(
                                    getApplicationContext(),
                                    "No se encoantro el usuario",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Bienvenido",
                                    Toast.LENGTH_SHORT).show();

                            idC = numId;

                            Intent toHome = new Intent(getApplicationContext(), MainActivity.class);
                            toHome.putExtra("IDCL", idC);
                            startActivity(toHome);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(
                        getApplicationContext(),
                        "Error de Conexion: " + "\n" + "Error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}