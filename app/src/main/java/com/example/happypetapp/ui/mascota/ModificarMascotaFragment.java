package com.example.happypetapp.ui.mascota;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.happypetapp.MainActivity;
import com.example.happypetapp.R;
import com.example.happypetapp.apiRest.ApiRestInterface;
import com.example.happypetapp.commons.Commons;
import com.example.happypetapp.model.Cliente;
import com.example.happypetapp.model.Mascota;
import com.example.happypetapp.model.Result;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ModificarMascotaFragment extends Fragment {

    //EDITTEXTS   MscM = Mascota Modificacion
    private EditText txtNombreMscM;
    private EditText txtEdadMscM;
    private EditText txtRazaMscM;
    private EditText txtEspecieMscM;
    private EditText txtDescripcionMscM;

    //RADIO BUTTONS
    private RadioButton rbMachoMscM;
    private RadioButton rbHembraMscM;

    //BUTTONS
    private Button btnModificarMscM;
    private Button btnCargarFotoMscM;
    private Button btnEliminarMscM;

    //IMAGEVIEW
    private ImageView imgFotoMscM;

    private static final int SOLICITAR_CODIGO_DE_PERMISO = 100;
    private static final int SOLICITAR_GALERIA_IMAGENES = 101;
    private String fotoEncode; //Variable para almacenar el codigo Base64 de la imagen seleccionada.

    private Mascota mascSelectedToUpload;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Modificación de Mascotas");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_modificar_mascota, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MascotaFragment mo = new MascotaFragment();
        mascSelectedToUpload = mo.getItemListMascota();
        //Toast.makeText(getContext(), "sdfsdfsdf", Toast.LENGTH_SHORT).show();

        txtNombreMscM       = view.findViewById(R.id.txtNombreMscM);
        txtEdadMscM         = view.findViewById(R.id.txtEdadMscM);
        txtRazaMscM         = view.findViewById(R.id.txtRazaMscM);
        txtEspecieMscM      = view.findViewById(R.id.txtEspecieMscM);
        txtDescripcionMscM  = view.findViewById(R.id.txtDescripcionMscM);
        rbMachoMscM         = view.findViewById(R.id.rbMachoMscM);
        rbHembraMscM        = view.findViewById(R.id.rbHembraMscM);
        imgFotoMscM         = view.findViewById(R.id.imgFotoMscM);
        btnModificarMscM    = view.findViewById(R.id.btnModificarMscM);
        btnCargarFotoMscM   = view.findViewById(R.id.btnCargarFotoMscM);
        btnEliminarMscM     = view.findViewById(R.id.btnEliminarMscM);


        //Carga de datos de la mascota a modificar
        txtNombreMscM.setText(mascSelectedToUpload.getNombre());
        txtEdadMscM.setText(mascSelectedToUpload.getEdad());
        txtRazaMscM.setText(mascSelectedToUpload.getRaza());
        txtEspecieMscM.setText(mascSelectedToUpload.getEspecie());
        txtDescripcionMscM.setText(mascSelectedToUpload.getDescripcion());
        if(mascSelectedToUpload.getSexo().equals("M")){
            rbMachoMscM.setChecked(true);
        }else{
            rbHembraMscM.setChecked(true);
        }
        imgFotoMscM.setImageBitmap(decodeBase64(mascSelectedToUpload.getFoto()));
        //-----------------------------------------------------------------------//


        btnCargarFotoMscM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Para versiones anteriores a desde la api 23, android solicita los permisos
                cuando se instala la aplicacio. Si esto pasa, automaticamente abre la galeria,
                pero, si nuestra api es mayor o igual a la 23, necesitamos pedir los permisos
                en tiempo de ejecucion.
                 */
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    /*
                    Si nuestra api es mayor o igual a 23, necesitamos pedir permisos en tiempo
                    de ejecucion.
                     */


                    /*
                    Verifica si anteriormente teniamos los permisos para manipular archivos externos.
                    Si ya poseemos los permisos (PERMISSION_GRANTED), pasara a ejecutar el metodo
                    abrir galeria().
                     */
                    if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        abrirGaleria();


                    }else{ //Si no estaban habilitados los permisos
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, SOLICITAR_CODIGO_DE_PERMISO);
                    }
                }else{
                    /*
                    Si nuestra api es menor a 23, damos por hecho que tiene los permisos para
                    abrir la galeria.
                     */
                    abrirGaleria();
                }
            }
        });

        btnModificarMscM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nombreMd = txtNombreMscM.getText().toString();
                String especieMd = txtEspecieMscM.getText().toString();
                String razaMd = txtRazaMscM.getText().toString();
                String edadMd = txtEdadMscM.getText().toString();
                String descripMd = txtDescripcionMscM.getText().toString();
                String genMd = null;
                if(rbMachoMscM.isChecked() == true){
                    genMd = "M";
                }else if(rbHembraMscM.isChecked() == true){
                    genMd = "H";
                }

                if(fotoEncode == null){
                    fotoEncode = mascSelectedToUpload.getFoto();
                }

                Cliente objC = new Cliente();
                objC.setIdCliente(MainActivity.idCliente);
                Mascota objM = new Mascota(mascSelectedToUpload.getIdMascota(),
                        nombreMd, edadMd, razaMd, especieMd, genMd,
                        descripMd, 1, fotoEncode, objC);

                /*
                Mensaje de alerta para preguntar sobre una accion al usuario.
                 */
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("¿Deseas Modificar la mascota seleccionada?").setTitle("Modificar");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        modificarMascota(objM);
                        Toast.makeText(getContext(), "Modificado", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getContext(), "OKMODFI",Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        btnEliminarMscM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setMessage("¿Deseas Eliminar la mascota seleccionada?").setTitle("Eliminar");

                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eliminarMasctoa(mascSelectedToUpload.getIdMascota());
                        Toast.makeText(getContext(), "Eliminado", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getContext(), "OK", Toast.LENGTH_SHORT).show();
                    }
                });


                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }


    /*
    Metodo que se ejecuta cuando el usuario selecciono un elemento de la galeria, o cuando se salio
    de ella. Se optiene un codigo de resultado, si es -1, quiere decir que se selecciono algo
    y podemos manipular el data para obtener los datos de la image. Si el codifo de resultado
    es 0, se enviara un Toas con el texto de no haber seleccionado una imagen.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.i("TAG", "RRRRRRRRRRRRRRRRRR: " + resultCode + "----" + requestCode);
        if(requestCode == SOLICITAR_GALERIA_IMAGENES){
            if(resultCode == getActivity().RESULT_OK && data != null){
                Uri foto = data.getData();
                imgFotoMscM.setImageURI(foto);

                try{
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), foto);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                    byte[] bytes = stream.toByteArray();

                    fotoEncode = Base64.encodeToString(bytes, Base64.DEFAULT);

                    Log.i("TAG", fotoEncode);
                }catch(IOException e){

                }

            }else{
                Log.i("TAG", "Resultado: " + resultCode);
                Toast.makeText(getContext(), "No seleccionaste nada de la galeria", Toast.LENGTH_SHORT).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == SOLICITAR_CODIGO_DE_PERMISO){
            if(permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                abrirGaleria();
            }else{

                Toast.makeText(getContext(), "Necesitas habilitar los permisos", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    /**
     * Metodo para abrir la galeria
     */
    private void abrirGaleria(){
        Intent intentGaleria = new Intent(Intent.ACTION_GET_CONTENT);
        intentGaleria.setType("image/*");

        startActivityForResult(intentGaleria, SOLICITAR_GALERIA_IMAGENES);
    }


    /**
     * Metodo para decodificar Base64 y mostrar las imagenes del servidor.
     * @param base64 Texto Base64 a decodificar
     * @return  Objeto Bitmap con la imagen ya decodificada.
     */
    private Bitmap decodeBase64(String base64){
        byte[] bytes = Base64.decode(base64, Base64.DEFAULT);

        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        return bitmap;
    }


    /**
     * Metodo para realziar el envio de datos al servicio para la modificacion de datos de
     * una mascota seleccionada
     * @param m Objeto Mascota con los datos modificados.
     */
    private void modificarMascota(Mascota m){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Commons.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiRestInterface  getMethost = retrofit.create(ApiRestInterface.class);

        Call<Result> modificarMasc = getMethost.updateMascota(
                m.getIdMascota(),
                m.getNombre(),
                m.getEdad(),
                m.getRaza(),
                m.getEspecie(),
                m.getSexo(),
                m.getDescripcion(),
                m.getFoto(),
                m.getCliente().getIdCliente());

        modificarMasc.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(
                            getContext(),
                            "Ha ocurrido un error con la modificacion " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }else{
                    if(response.body().getResult().equals("OK")){
                        Toast.makeText(
                                getContext(),
                                "Modificacion exitosa",
                                Toast.LENGTH_SHORT).show();

                        limpiarCampos();
                    }else if(response.body().getError() != null){
                        Toast.makeText(getContext(), "Ha ocurrido un error en la péracion del " +
                                "servidor." + "\n" + response.body().getError(),
                                Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(
                        getContext(),
                        "Error de Conexion: " + "\n" + "Error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * Metodo que realiza el envio de datos al servicio para la eliminacion logica de una
     * mascota
     * @param idM Id de la mascota a eliminar
     */
    private void eliminarMasctoa(int idM){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Commons.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiRestInterface getMethods = retrofit.create(ApiRestInterface.class);

        Call<Result> deleteMascota = getMethods.deleteMascota(idM);

        deleteMascota.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(
                            getContext(),
                            "Ha ocurrido un error con la modificacion " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }else{
                    if(response.body().getResult().equals("OK")){
                        Toast.makeText(
                                getContext(),
                                "Eliminar exitosa",
                                Toast.LENGTH_SHORT).show();

                        limpiarCampos();
                    }else if(response.body().getError() != null){
                        Toast.makeText(getContext(), "Ha ocurrido un error en la péracion del " +
                                        "servidor." + "\n" + response.body().getError(),
                                Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(
                        getContext(),
                        "Error de Conexion: " + "\n" + "Error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Metodo para limpiar los campos al momento de realizar alguna accion sobre la mascota
     * (Modificar o Eliminar)
     */
    private void  limpiarCampos(){
        txtNombreMscM.setText("");
        txtEdadMscM.setText("");
        txtRazaMscM.setText("");
        txtEspecieMscM.setText("");
        txtDescripcionMscM.setText("");
        imgFotoMscM.setImageURI(null);

    }
}