package com.example.happypetapp.ui.usuario;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.happypetapp.MainActivity;
import com.example.happypetapp.R;
import com.example.happypetapp.apiRest.ApiRestInterface;
import com.example.happypetapp.commons.Commons;
import com.example.happypetapp.model.Cliente;
import com.example.happypetapp.model.Persona;
import com.example.happypetapp.model.Result;
import com.example.happypetapp.model.Usuario;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UsuarioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UsuarioFragment extends Fragment {

    EditText txtNombreUsrM;
    EditText txtApePUsrM;
    EditText txtApeMUsrM;
    EditText txtCalleUsrM;
    EditText txtColoniaUsrM;
    EditText txtCPUsrM;
    EditText txtTelUsrM;
    EditText txtUsuarioM;
    EditText txtEmailUrsM;
    RadioButton rbMascUsrM;
    RadioButton rbFemeUsrM;
    Button btnGuardarMU;

    private static Cliente cl;
    private View vw;
    private int idC;
    //Areglo para almacenar los id's necesarios para el Update: 0 = idCli, 1 = idPer, 2 = idUsu
    private int[] ids = new int[3];


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UsuarioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UsrFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UsuarioFragment newInstance(String param1, String param2) {
        UsuarioFragment fragment = new UsuarioFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Datos de Usuario");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_usuario, container, false);
        vw = root;

        txtNombreUsrM       = root.findViewById(R.id.txtNombreUsrM);
        txtApePUsrM         = root.findViewById(R.id.txtApePUsrM);
        txtApeMUsrM         = root.findViewById(R.id.txtApeMUsrM);
        txtCalleUsrM        = root.findViewById(R.id.txtCalleUsrM);
        txtColoniaUsrM      = root.findViewById(R.id.txtColoniaUsrM);
        txtCPUsrM           = root.findViewById(R.id.txtCPUsrM);
        txtTelUsrM          = root.findViewById(R.id.txtTelUsrM);
        txtUsuarioM         = root.findViewById(R.id.txtUsuarioM);
        txtEmailUrsM        = root.findViewById(R.id.txtEmailUsrM);
        rbMascUsrM          = root.findViewById(R.id.rbMascUsrM);
        rbFemeUsrM          = root.findViewById(R.id.rbFemeUsrM);
        btnGuardarMU        = root.findViewById(R.id.btnGuardarMU);

        servicio(MainActivity.idCliente);

        //Toast.makeText(getContext(), cl.getCorreo(), Toast.LENGTH_LONG).show();
        return root;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnGuardarMU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = txtNombreUsrM.getText().toString();
                String apeP = txtApePUsrM.getText().toString();
                String apeM = txtApeMUsrM.getText().toString();
                String calle = txtCalleUsrM.getText().toString();
                String colonia = txtColoniaUsrM.getText().toString();
                String cp = txtCPUsrM.getText().toString();
                String tel = txtTelUsrM.getText().toString();
                String usuN = txtUsuarioM.getText().toString();
                String email = txtEmailUrsM.getText().toString();
                String gen = null;
                idC = MainActivity.idCliente;
                //Evaluacion de los radio buttons
                if(rbMascUsrM.isChecked() == true){
                    gen = "M";
                    Log.i("GEN: ", "M");
                }else if(rbFemeUsrM.isChecked() == true){
                    gen = "F";
                    Log.i("GEN: ", "H");
                }

                if(nom.equals("") || apeP.equals("") ||
                        apeM.equals("") || calle.equals("") ||
                        colonia.equals("") || cp.equals("") ||
                        tel.equals("") || usuN.equals("") ||
                        email.equals("")){

                    Toast.makeText(
                            getContext(),
                            "Registro Incompleto" + "\n" + "Llene todos los campos.",
                            Toast.LENGTH_SHORT).show();
                }else{

                    Persona objP = new Persona(ids[1], nom, apeP, apeM, gen, calle, colonia,
                            Integer.parseInt(cp), tel);
                    Usuario objU = new Usuario(ids[2], usuN, null, "cliente");
                    Cliente objC = new Cliente(ids[0], email, true, objP, objU);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("¿Deseas modificar tus datos?").setTitle("Modificacion Usuario");
                    builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {



                            for(int i = 0; i < ids.length; i++){
                                Log.i("IDS", Integer.toString(ids[i]));
                            }
                            updateCliente(objC);
                            Toast.makeText(getContext(), "Modificacion Exitosa!!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    /**
     * Metodo que hara la consulta al servidor.
     * @param idCliente ID del cliente a solicitar su informacion.
     */
    public void servicio(int idCliente){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Commons.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiRestInterface getServices = retrofit.create(ApiRestInterface.class);

        Call<Cliente> clienteCall = getServices.getOneCliente(idCliente);

        clienteCall.enqueue(new Callback<Cliente>() {
            @Override
            public void onResponse(Call<Cliente> call, Response<Cliente> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(
                            getContext(),
                            "Ha ocurrido un error " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }else{
                    if(response.body() == null){
                        Toast.makeText(
                                getContext(),
                                "No se encontro usuario a modificar, intentelo mas tarde",
                                Toast.LENGTH_SHORT).show();
                    }else{
                        initComponents(vw, response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<Cliente> call, Throwable t) {
                Toast.makeText(
                        getContext(),
                        "Error de Conexion: " + "\n" + "Error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Metodo para inicializar los componentes vizuales del fragment, ademas de asignarles
     * los valores establecidos por el cliente a modificar.
     * @param v Vista del fragment donde se encuentran los componentes
     * @param cl Objeto de cliente para obtener sus datos.
     */
    private void initComponents(@NotNull View v, @NotNull Cliente cl){


        txtNombreUsrM.setText(cl.getPersona().getNombre());
        txtApePUsrM.setText(cl.getPersona().getApellidoPaterno());
        txtApeMUsrM.setText(cl.getPersona().getApellidoMaterno());
        txtCalleUsrM.setText(cl.getPersona().getCalle());
        txtColoniaUsrM.setText(cl.getPersona().getColonia());
        txtCPUsrM.setText(Integer.toString(cl.getPersona().getCp()));
        txtTelUsrM.setText(cl.getPersona().getTelefono());
        txtUsuarioM.setText(cl.getUsuario().getNombreUsuario());
        txtEmailUrsM.setText(cl.getCorreo());
        if(cl.getPersona().getGenero().equals("M")){
            rbMascUsrM.setChecked(true);
        }else{
            rbFemeUsrM.setChecked(true);
        }

        ids[0] = cl.getIdCliente();
        ids[1] = cl.getPersona().getIdPersona();
        ids[2] = cl.getUsuario().getIdUsuario();

    }

    private void updateCliente(Cliente cl){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Commons.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiRestInterface getServices = retrofit.create(ApiRestInterface.class);

        Call<Result> updateCl = getServices.updateCliente(
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
                cl.getIdCliente(),
                cl.getPersona().getIdPersona(),
                cl.getUsuario().getIdUsuario());

        updateCl.enqueue(new Callback<Result>() {
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

            }
        });
    }

    private void limpiarCampos() {
        txtNombreUsrM.setText("");
        txtApePUsrM.setText("");
        txtApeMUsrM.setText("");
        txtCalleUsrM.setText("");
        txtColoniaUsrM.setText("");
        txtCPUsrM.setText("");
        txtTelUsrM.setText("");
        txtUsuarioM.setText("");
        txtEmailUrsM.setText("");
        rbMascUsrM.setChecked(true);
    }

}