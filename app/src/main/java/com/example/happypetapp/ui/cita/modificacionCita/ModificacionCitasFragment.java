package com.example.happypetapp.ui.cita.modificacionCita;

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
import com.example.happypetapp.model.Mascota;
import com.example.happypetapp.model.Result;
import com.example.happypetapp.model.Servicio;
import com.example.happypetapp.ui.cita.CitaFragment;
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


public class ModificacionCitasFragment extends Fragment implements MyservicioCitaViewAdapter.OnServicioCitaListener, MymascotaCitaViewAdapter.OnMascCitaListener{

    private TextView txtNMascotaSelM;
    private TextView txtServicioSelM;
    private TextView txtFechaIniM;

    private RecyclerView rclMascotaListM;
    private RecyclerView rclServicioListM;

    private MyservicioCitaViewAdapter adapterServCiM;
    private MymascotaCitaViewAdapter adapterMMCiM;


    private CalendarView clvCalendarioCiM;
    private Spinner spnHorariosM;

    private Button btnModificarCitaM;

    private String[] horarios = {"10:00 AM", "12:00 PM", "14:00 PM", "16:00 PM", "18:00 PM"};
    private ArrayAdapter<String> adapterSP;

    private Cita itemCitaToUpdate;
    private Cita objCita = new Cita();
    private List<Servicio> mListSerCiM;
    private List<Mascota> mListMascCiM;
    private String date = "";
    private String hora = "";
    private int idM;
    private int idSr;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_modificacion_citas, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Modificación de Citas");

        mListSerCiM = new ArrayList<>();
        mListMascCiM = new ArrayList<>();
        

        adapterSP = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, horarios);

        CitaFragment cf = new CitaFragment();

        itemCitaToUpdate = cf.getItemListCita();

        idM = itemCitaToUpdate.getMascota().getIdMascota();
        idSr = itemCitaToUpdate.getServicio().getIdServicio();
        date = objCita.extraerFecha(itemCitaToUpdate.getFechaHoraIniAsString());
        hora = objCita.extraerHora(itemCitaToUpdate.getFechaHoraIniAsString());

        initComponents(root);


        rclServicioListM.setLayoutManager(new LinearLayoutManager(getActivity()));
        rclMascotaListM.setLayoutManager(new LinearLayoutManager(getActivity()));

        cargarListaServicios();
        cargarListaMascota(MainActivity.idCliente);


        clvCalendarioCiM.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String mt = null;
                String dy = null;
                int m = (month + 1);
                int d = dayOfMonth;

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

                date = dy + "/" + mt + "/" + year;

                txtFechaIniM.setText(date);
            }
        });

        spnHorariosM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hora = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnModificarCitaM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("¿Los datos son correctos?");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String fechaI = objCita.concatFechaHora(date, configHora(hora));
                        Log.i("FECHA_HORA", fechaI);
                        modificarCitaM(itemCitaToUpdate.getIdCita(), idM, idSr, date, hora);
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
        });

        return root;
    }



    private void initComponents(@NotNull View v){
        txtNMascotaSelM = v.findViewById(R.id.txtNMascotaSelM);
        txtServicioSelM = v.findViewById(R.id.txtServicioSelM);
        txtFechaIniM = v.findViewById(R.id.txtFechaIniM);
        rclMascotaListM = v.findViewById(R.id.rclMascotaListM);
        rclServicioListM = v.findViewById(R.id.rclServicioListM);
        clvCalendarioCiM = v.findViewById(R.id.clvCalendarioCiM);
        spnHorariosM = v.findViewById(R.id.spnHorariosM);
        btnModificarCitaM = v.findViewById(R.id.btnModificarCitaM);


        txtNMascotaSelM.setText(itemCitaToUpdate.getMascota().getNombre());
        txtServicioSelM.setText(itemCitaToUpdate.getServicio().getNombreS());
        txtFechaIniM.setText(objCita.construirResultadoFecha(objCita.extraerFecha(itemCitaToUpdate.getFechaHoraIniAsString())));
        spnHorariosM.setAdapter(adapterSP);
    }

    private void cargarListaMascota(int idC){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Commons.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiRestInterface getServices = retrofit.create(ApiRestInterface.class);
        Call<List<Mascota>> getMascotas = getServices.getAllMascotaCli(idC);

        getMascotas.enqueue(new Callback<List<Mascota>>() {
            @Override
            public void onResponse(Call<List<Mascota>> call, Response<List<Mascota>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(
                            getContext(),
                            "Ha ocurrido un error con la consuta " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }else{
                    for(Mascota m: response.body()){
                        mListMascCiM.add(m);
                    }

                    adapterMMCiM = new MymascotaCitaViewAdapter(mListMascCiM, getContext(), ModificacionCitasFragment.this::onMascCitaClick);
                    rclMascotaListM.setAdapter(adapterMMCiM);
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

    private void cargarListaServicios(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Commons.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiRestInterface getServices = retrofit.create(ApiRestInterface.class);

        Call<List<Servicio>> getServicios = getServices.getAllServicio();

        getServicios.enqueue(new Callback<List<Servicio>>() {
            @Override
            public void onResponse(Call<List<Servicio>> call, Response<List<Servicio>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(
                            getContext(),
                            "Ha ocurrido un error con la consuta " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }else{
                    for(Servicio s: response.body()){
                        mListSerCiM.add(s);
                    }

                    adapterServCiM = new MyservicioCitaViewAdapter(mListSerCiM, getContext(), ModificacionCitasFragment.this::onServicioCitaClick);
                    rclServicioListM.setAdapter(adapterServCiM);
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
     *
     * @param idCi ID Cita
     * @param idM  ID Mascota
     * @param idS  ID Servicio
     * @param fechaIni
     * @param hora
     */
    private void modificarCitaM(int idCi, int idM, int idS, String fechaIni, String hora) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Commons.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiRestInterface getServices = retrofit.create(ApiRestInterface.class);

        String fechaI = objCita.concatFechaHora(date, configHora(hora));

        Call<Result> updateCita = getServices.updateCita(fechaI, idM, idS, idCi);

        updateCita.enqueue(new Callback<Result>() {
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
                                "Modificacion Exitosa",
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


    @Override
    public void onMascCitaClick(int posicion) {
        txtNMascotaSelM.setText(mListMascCiM.get(posicion).getNombre());
        idM = mListMascCiM.get(posicion).getIdMascota();
    }

    @Override
    public void onServicioCitaClick(int posicion) {
        txtServicioSelM.setText(mListSerCiM.get(posicion).getNombreS());
        idSr = mListSerCiM.get(posicion).getIdServicio();
    }

    private String configHora(@NotNull String hora){
        String h = hora.substring(0, 5).concat(":00");
        return h;
    }

    private void limpiarCampos(){
        idM = 0;
        idSr = 0;
        hora = "";
        date = "";
    }
}