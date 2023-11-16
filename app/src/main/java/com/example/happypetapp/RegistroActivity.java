package com.example.happypetapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.happypetapp.apiRest.ApiRestInterface;
import com.example.happypetapp.commons.Commons;
import com.example.happypetapp.model.Cliente;
import com.example.happypetapp.model.Persona;
import com.example.happypetapp.model.Result;
import com.example.happypetapp.model.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistroActivity extends AppCompatActivity {

    //EDITTEXT
    EditText txtNombreUsr;
    EditText txtApePUsr;
    EditText txtApeMUsr;
    EditText txtCalleUsr;
    EditText txtColoniaUsr;
    EditText txtCPUsr;
    EditText txtTelUsr;
    EditText txtUsuarioN;
    EditText txtPassN;
    EditText txtEmailUrs;

    //RADIOGROUP AND RADIOBUTTON
    RadioGroup rbgGeneroUsr;
    RadioButton rbMascUsr; //MASCULINO
    RadioButton rbFemeUsr; //FEMENINO

    //BUTTON
    Button btnRegistrar;

    /*-------------------*/
    private int idC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //Inicializacion componentes:
        txtNombreUsr    = findViewById(R.id.txtNombreUsr);
        txtApePUsr      = findViewById(R.id.txtApePUsr);
        txtApeMUsr      = findViewById(R.id.txtApeMUsr);
        txtCalleUsr     = findViewById(R.id.txtCalleUsr);
        txtColoniaUsr   = findViewById(R.id.txtColoniaUsr);
        txtCPUsr        = findViewById(R.id.txtCPUsr);
        txtTelUsr       = findViewById(R.id.txtTelUsr);
        txtUsuarioN     = findViewById(R.id.txtUsuarioN);
        txtPassN        = findViewById(R.id.txtPassN);
        txtEmailUrs     = findViewById(R.id.txtEmailUrs);
        rbgGeneroUsr    = findViewById(R.id.rbgGeneroUsr);
        rbMascUsr       = findViewById(R.id.rbMascUsr);
        rbFemeUsr       = findViewById(R.id.rbFemeUsr);
        btnRegistrar    = findViewById(R.id.btnRegistrarNU);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = txtNombreUsr.getText().toString();
                String apeP = txtApePUsr.getText().toString();
                String apeM = txtApeMUsr.getText().toString();
                String calle = txtCalleUsr.getText().toString();
                String colonia = txtColoniaUsr.getText().toString();
                String cp = txtCPUsr.getText().toString();
                String tel = txtTelUsr.getText().toString();
                String usuN = txtUsuarioN.getText().toString();
                String pass = txtPassN.getText().toString();
                String email = txtEmailUrs.getText().toString();
                String gen = null;
                //Evaluacion de los radio buttons
                if(rbMascUsr.isChecked() == true){
                    gen = "M";
                    Log.i("GEN: ", "M");
                }else if(rbFemeUsr.isChecked() == true){
                    gen = "F";
                    Log.i("GEN: ", "H");
                }

                //Validacion de campos vacios
                if(nom.equals("") || apeP.equals("") ||
                        apeM.equals("") || calle.equals("") ||
                        colonia.equals("") || cp.equals("") ||
                        tel.equals("") || usuN.equals("") ||
                        pass.equals("") || email.equals("")){

                    Toast.makeText(
                            getApplicationContext(),
                            "Registro Incompleto" + "\n" + "Llene todos los campos.",
                            Toast.LENGTH_SHORT).show();
                }else{

                    Persona obP = new Persona(nom, apeP, apeM, gen, calle, colonia,
                            Integer.parseInt(cp), tel);
                    Usuario objU = new Usuario(usuN, pass, "cliente");
                    Cliente objC = new Cliente(email, true, obP, objU);

                    consumirServicioRegistrarUsu(objC);
                }
            }
        });


    }

    /**
     * Metodo para realizar la insercion del nuevo cliente en el servidor
     * @param cl
     */
    private void consumirServicioRegistrarUsu(Cliente cl){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Commons.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiRestInterface getService = retrofit.create(ApiRestInterface.class);

        Call<Result> insertUsr = getService.insertCliente(
                cl.getCorreo(),
                cl.getPersona().getNombre(),
                cl.getPersona().getApellidoPaterno(),
                cl.getPersona().getApellidoMaterno(),
                cl.getPersona().getGenero(),
                cl.getPersona().getCalle(),
                cl.getPersona().getColonia(),
                Integer.toString(cl.getPersona().getCp()),
                cl.getPersona().getTelefono(),
                cl.getUsuario().getNombreUsuario(),
                cl.getUsuario().getContrasena());

        insertUsr.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(
                            getApplicationContext(),
                            "Ha ocurrido un error con la insercion " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }else{

                    /**
                     * Si salio bien, se construira el intent necesario para cambiar de actividad,
                     * Se agregara un extra con la clave IDCL y como valor sera el id del cliente
                     * que respondio el servicio.
                     * El cambio de actividad sera hacia la MainAcivity.
                     */
                    Toast.makeText(
                            getApplicationContext(),
                            "Registro exitoso",
                            Toast.LENGTH_SHORT).show();

                    idC = Integer.parseInt(response.body().getResult());

                    Intent toHome = new Intent(getApplicationContext(), MainActivity.class);
                    toHome.putExtra("IDCL", idC);
                    startActivity(toHome);

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