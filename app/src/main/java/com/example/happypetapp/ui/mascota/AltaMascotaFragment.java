package com.example.happypetapp.ui.mascota;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AltaMascotaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AltaMascotaFragment extends Fragment {

    //EDITTEXTS   MscN = Mascota Nueva
    private EditText txtNombreMscN;
    private EditText txtEdadMscN;
    private EditText txtRazaMscN;
    private EditText txtEspecieMscN;
    private EditText txtDescripcionMscN;

    //RADIO BUTTONS
    private RadioButton rbMachoMscN;
    private RadioButton rbHembraMscN;

    //BUTTONS
    private Button btnRegisrarMscN;
    private Button btnCargarFotoMscN;

    //IMAGEVIEW
    private ImageView imgFotoMscN;



    private static final int SOLICITAR_CODIGO_DE_PERMISO = 100;
    private static final int SOLICITAR_GALERIA_IMAGENES = 101;
    private String fotoEncode; //Variable para almacenar el codigo Base64 de la imagen seleccionada.

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AltaMascotaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AltaMascotaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AltaMascotaFragment newInstance(String param1, String param2) {
        AltaMascotaFragment fragment = new AltaMascotaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Alta de Mascotas");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_alta_mascota, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtNombreMscN       = view.findViewById(R.id.txtNombreMscN);
        txtEdadMscN         = view.findViewById(R.id.txtEdadMscN);
        txtRazaMscN         = view.findViewById(R.id.txtRazaMscN);
        txtEspecieMscN      = view.findViewById(R.id.txtEspecieMscN);
        txtDescripcionMscN  = view.findViewById(R.id.txtDescripcionMscN);
        rbMachoMscN         = view.findViewById(R.id.rbMachoMscN);
        rbHembraMscN        = view.findViewById(R.id.rbHembraMscN);
        imgFotoMscN         = view.findViewById(R.id.imgForoMscN);
        btnRegisrarMscN     = view.findViewById(R.id.btnRegistrarMscN);
        btnCargarFotoMscN   = view.findViewById(R.id.btnCargarFotoMscN);

        //EVENTO CARGAR FOTO DESDE LA GALERIA
        btnCargarFotoMscN.setOnClickListener(new View.OnClickListener() {
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

        btnRegisrarMscN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idC = MainActivity.idCliente;
                String nombreM = txtNombreMscN.getText().toString();
                String edadM = txtEdadMscN.getText().toString();
                String razaM = txtRazaMscN.getText().toString();
                String especieE = txtEspecieMscN.getText().toString();
                String descriM = txtDescripcionMscN.getText().toString();
                String gen = null;
                if(rbMachoMscN.isChecked() == true){
                    gen = "M";
                }else if(rbHembraMscN.isChecked() == true){
                    gen = "H";
                }

                if(nombreM.equals("") || edadM.equals("") ||
                        razaM.equals("") || especieE.equals("") ||
                        descriM.equals("") || fotoEncode.equals("")){


                    Toast.makeText(
                            getContext(),
                            "Registro Incompleto" + "\n" + "Llene todos los campos.",
                            Toast.LENGTH_SHORT).show();


                }else{

                    Cliente objC = new Cliente();
                    objC.setIdCliente(idC);
                    Mascota objM = new Mascota(nombreM, edadM, razaM, especieE, gen,
                            descriM, 1, fotoEncode, objC);

                    altaMascota(objM);

                }

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
                imgFotoMscN.setImageURI(foto);

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

    private void abrirGaleria(){
        Intent intentGaleria = new Intent(Intent.ACTION_GET_CONTENT);
        intentGaleria.setType("image/*");

        startActivityForResult(intentGaleria, SOLICITAR_GALERIA_IMAGENES);
    }

    private void altaMascota(Mascota m){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Commons.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiRestInterface api = retrofit.create(ApiRestInterface.class);

        Call<Result> insertMascota = api.insertMascota(
                m.getNombre(),
                m.getEdad(),
                m.getRaza(),
                m.getEspecie(),
                m.getSexo(),
                m.getDescripcion(),
                m.getFoto(),
                m.getCliente().getIdCliente());

        insertMascota.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(
                            getContext(),
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
                            getContext(),
                            "Registro exitoso",
                            Toast.LENGTH_SHORT).show();

                    limpiarCampos();

                    
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

    private void  limpiarCampos(){
        txtNombreMscN.setText("");
        txtEdadMscN.setText("");
        txtRazaMscN.setText("");
        txtEspecieMscN.setText("");
        txtDescripcionMscN.setText("");
        imgFotoMscN.setImageURI(null);

    }
}