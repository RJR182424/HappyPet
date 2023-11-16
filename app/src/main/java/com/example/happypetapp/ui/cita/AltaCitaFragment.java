package com.example.happypetapp.ui.cita;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.happypetapp.MainActivity;
import com.example.happypetapp.R;
import com.example.happypetapp.apiRest.ApiRestInterface;
import com.example.happypetapp.commons.Commons;
import com.example.happypetapp.model.Cita;
import com.example.happypetapp.model.Cliente;
import com.example.happypetapp.model.Mascota;
import com.example.happypetapp.model.Result;
import com.example.happypetapp.model.Servicio;
import com.example.happypetapp.ui.cita.altaCitaViewAdapters.MymascotaCitaViewAdapter;
import com.example.happypetapp.ui.cita.altaCitaViewAdapters.MyservicioCitaViewAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AltaCitaFragment extends Fragment implements MyservicioCitaViewAdapter.OnServicioCitaListener, MymascotaCitaViewAdapter.OnMascCitaListener{

    //TEXTVIEWS
    private TextView txtNMascotaSel;
    private TextView txtServicioSel;
    private TextView txtFechaCita;
    private TextView txtConfiMasc;
    private TextView txtConfiServicio;
    private TextView txtConfiFecha;
    private TextView txtConfiHora;
    private TextView txtConfiPrecio;

    //RECYCLEVIEWS
    private RecyclerView rclMascotaforSel;
    private RecyclerView rclServicioforSel;

    //RECYCLEVIEW ADAPTERS
    private MyservicioCitaViewAdapter adapterServCi;
    private MymascotaCitaViewAdapter adapterMascCi;

    //OTROS
    private CalendarView clvCalendarCi;
    private Spinner spnHorarios;

    //Arreglo con los datos que seran insertados en el spinner.
    private String[] horarios = {"10:00 AM", "12:00 PM", "14:00 PM", "16:00 PM", "18:00 PM"};
    //Adapter que construira la parte visual con la informacion del arreglo que llevara el spinner.
    private ArrayAdapter<String> adapterSP;

    //BUTTONS
    private Button btnConfirmarCita;

    //Variables
    private List<Servicio> mListSerCi;
    private List<Mascota> mListMascCi;

    private String date = "";
    private String hora = "";

    private int idM; //ID MASCOTA
    private int objCl;
    private int idSr; //ID SERVICIO

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Linea para cambiar el nombre del ActionBar del fragment
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Alta de Citas");

        //Listas para mostrar en los RecycleView
        mListSerCi = new ArrayList<>();
        mListMascCi = new ArrayList<>();

        View root = inflater.inflate(R.layout.fragment_alta_cita, container, false);

        //Inicializacion del adapter, como parametro es el contexto, el layout visual de los items
        //y la lista de datos que tendra el spínner.
        adapterSP = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, horarios);




        initComponent(root);

        rclServicioforSel.setLayoutManager(new LinearLayoutManager(getActivity()));
        rclMascotaforSel.setLayoutManager(new LinearLayoutManager(getActivity()));

        consultarServiciosCl();
        consultarMascotaCl(MainActivity.idCliente);

        //Evento del calendario, si sucede algun cambio de fecha en el.
        clvCalendarCi.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Log.i("DATE", dayOfMonth + "/" + (month + 1) + "/" + year);
                /*
                Si el evento devuelve un valor de un solo digito, debemos agregarle el "0" para
                el perfecto formato y no tener errores con el servidor
                 */
                String mt = null; //Mes
                String dy = null; //Dia

                /*
                El mes devuelve el numero del mes como si fuera un array (0 - 11), siendo 0 enero y
                11 diciembre, por esto se le debe de sumar 1 para su perfecta interpretacion con
                el servicio y para la comprencion del usuario.
                 */
                int m = (month + 1);
                int d = dayOfMonth;

                /*
                Las siguietes estructuras son para saber si es necesario agregar el 0. Si el numero
                es menor a 10, se debe de concatenar el 0 y almacenarlo en una variable String. si
                es mayor a 10, no se le agrega el 0 pero igualmete se almacena en una variable
                String.
                 */
                if(m < 10){
                    mt = "0"+m;
                }else{
                    mt = Integer.toString(m);
                }

                if(d < 10){
                    dy = "0"+d;
                }else{
                    dy = Integer.toString(d);
                }

                //Fecha resultante
                date = dy + "/" + mt + "/" + year;

                txtFechaCita.setText(date);
                txtConfiFecha.setText(date);
            }
        });

        //Evento spinner, si el usuario selecciona un elemento de la lista
        spnHorarios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Se optiene el valor seleccionado.
                hora = parent.getSelectedItem().toString();
                //Se imprime el valor seleccionado el pantalla.
                txtConfiHora.setText(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Evento confirmar la cita
        btnConfirmarCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creacion de Alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("¿Los datos son correctos?");

                //Si es positiva la respuesta
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Cita objC = new Cita();

                        //Se construlle la fecha necesaria para el servicio de altaCita
                        String fechaI = objC.concatFechaHora(date, configHora(hora));
                        Log.i("FECHA_HORA", fechaI);

                        insertCita(MainActivity.idCliente, idM, idSr, date, hora);

                    }
                });

                //Si es negativa la respuesta
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(
                                getContext(),
                                "Verifique sus datos antes",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                //Creacion del dialog
                AlertDialog dialog = builder.create();
                //Mostrar dialog
                dialog.show();
            }
        });

        return root;
    }

    /**
     * Inicializar los componentes vizuales
     * @param v Pantalla donde se ejecuta esta clase.
     */
    private void initComponent(@NotNull View v){

        txtNMascotaSel = v.findViewById(R.id.txtNMascotaSel);
        txtServicioSel = v.findViewById(R.id.txtServicioSel);
        txtFechaCita = v.findViewById(R.id.txtFechaCita);
        txtConfiMasc = v.findViewById(R.id.txtConfiMasc);
        txtConfiServicio = v.findViewById(R.id.txtConfiServicio);
        txtConfiFecha = v.findViewById(R.id.txtConfiFecha);
        txtConfiHora = v.findViewById(R.id.txtConfiHora);
        txtConfiPrecio = v.findViewById(R.id.txtConfiPrecio);

        rclMascotaforSel = v.findViewById(R.id.rclMascotaforSel);
        rclMascotaforSel.setLayoutManager(new LinearLayoutManager(getActivity()));

        rclServicioforSel = v.findViewById(R.id.rclServicioforSel);
        rclServicioforSel.setLayoutManager(new LinearLayoutManager(getActivity()));

        clvCalendarCi = v.findViewById(R.id.clvCalendarioCi);
        spnHorarios = v.findViewById(R.id.spnHorarios);
        spnHorarios.setAdapter(adapterSP);

        btnConfirmarCita = v.findViewById(R.id.btnConfirmarCita);
    }

    /**
     * Consulta de servicios
     */
    private void consultarServiciosCl(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Commons.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiRestInterface getServices = retrofit.create(ApiRestInterface.class);

        Call<List<Servicio>> getAllSer = getServices.getAllServicio();

        getAllSer.enqueue(new Callback<List<Servicio>>() {
            @Override
            public void onResponse(Call<List<Servicio>> call, Response<List<Servicio>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(
                            getContext(),
                            "Ha ocurrido un error con la consuta " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }else{
                    /*
                    Toast.makeText(
                            getContext(),
                            "todos vien xD " + response.code(),
                            Toast.LENGTH_SHORT).show();

                     */

                    for(Servicio s: response.body()){
                        mListSerCi.add(s);
                    }

                    adapterServCi = new MyservicioCitaViewAdapter(mListSerCi, getContext(), AltaCitaFragment.this::onServicioCitaClick);
                    rclServicioforSel.setAdapter(adapterServCi);

                }
            }

            @Override
            public void onFailure(Call<List<Servicio>> call, Throwable t) {
                Toast.makeText(
                        getContext(),"Error de Conexion: " + "\n" + "Error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Consulta de mascota
     * @param idC ID del cliente de la sesion
     */
    private void consultarMascotaCl(int idC){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Commons.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiRestInterface getCall = retrofit.create(ApiRestInterface.class);

        Call<List<Mascota>> getAllMCli = getCall.getAllMascotaCli(idC);

        getAllMCli.enqueue(new Callback<List<Mascota>>() {
            @Override
            public void onResponse(Call<List<Mascota>> call, Response<List<Mascota>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(
                            getContext(),
                            "Ha ocurrido un error con la consuta " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }else{
                    /*
                    Toast.makeText(
                            getContext(),
                            "todos vien xD " + response.code(),
                            Toast.LENGTH_SHORT).show();

                     */

                    for(Mascota m:response.body()){
                        mListMascCi.add(m);
                    }

                    adapterMascCi = new MymascotaCitaViewAdapter(mListMascCi, getContext(), AltaCitaFragment.this::onMascCitaClick);
                    rclMascotaforSel.setAdapter(adapterMascCi);
                }
            }

            @Override
            public void onFailure(Call<List<Mascota>> call, Throwable t) {
                Toast.makeText(
                        getContext(),"Error de Conexion: " + "\n" + "Error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Insercion de la nueva cita
     * @param idC ID del cliente de la sesion
     * @param idM ID de la mascota seleccionada para la cita
     * @param idS ID del servicio seleccionado para la cita
     * @param fechaIni Fecha inicial de la cita
     * @param hora Hora de la cita
     */
    private void insertCita(int idC, int idM, int idS, String fechaIni, String hora){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Commons.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiRestInterface getServices = retrofit.create(ApiRestInterface.class);

        Cita objC = new Cita();

        String fechaI = objC.concatFechaHora(date, configHora(hora));

        Call<Result> insertCita = getServices.insertCita(idM, idC, idS,fechaI);

        insertCita.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(
                            getContext(),
                            "Ha ocurrido un error con el registro " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }else{
                    if(response.body().getResult().equals("OK")){
                        Toast.makeText(
                                getContext(),
                                "Registro exitosa",
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
     * Evento al seleccionar el servicio
     * @param posicion Atributo de la pósicion seleccionada
     */
    @Override
    public void onServicioCitaClick(int posicion) {
        txtServicioSel.setText(mListSerCi.get(posicion).getNombreS());
        txtConfiServicio.setText(mListSerCi.get(posicion).getNombreS());
        txtConfiPrecio.setText("$" + mListSerCi.get(posicion).getPrecio());
        //Se obtiene el id del servicio seleccionado
        idSr = mListSerCi.get(posicion).getIdServicio();
    }

    /**
     * Evento al seleccionar la mascota
     * @param posicion Atributo de la pósicion seleccionada
     */
    @Override
    public void onMascCitaClick(int posicion) {
        txtNMascotaSel.setText(mListMascCi.get(posicion).getNombre());
        txtConfiMasc.setText(mListMascCi.get(posicion).getNombre());
        //Se optiene el id de la mascota seleccionada.
        idM = mListMascCi.get(posicion).getIdMascota();
    }

    /**
     * Se configura la hora seleccionada agregandole los segundos. Formato necesario para el
     * servicio
     * @param hora  Hora Seleccionada
     * @return  Nueva hora con los segundos incluidos.
     */
    private String configHora(@NotNull String hora){
        String h = hora.substring(0, 5).concat(":00");
        return h;
    }

    /**
     * Limpia los valores para un nuevo registro.
     */
    private void limpiarCampos(){
        idM = 0;
        idSr = 0;
        hora = "";
        date = "";

        txtNMascotaSel.setText("");
        txtServicioSel.setText("");
        txtFechaCita.setText("");
        txtConfiMasc.setText("");
        txtConfiServicio.setText("");
        txtConfiFecha.setText("");
        txtConfiHora.setText("");
        txtConfiPrecio.setText("");

    }

}