package com.example.happypetapp.ui.cita;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.happypetapp.MainActivity;
import com.example.happypetapp.R;
import com.example.happypetapp.apiRest.ApiRestInterface;
import com.example.happypetapp.commons.Commons;
import com.example.happypetapp.model.Cita;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CitaFragment extends Fragment implements MycitaRecyclerViewAdapter.OnCitaListener{

    private List<Cita> mListCi;
    private static Cita itemListCita;
    private Button btnGenerarCita;
    private RecyclerView rclCitas;
    private MycitaRecyclerViewAdapter adapterCita;
    static int idCita = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mListCi = new ArrayList<>();

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_cita, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Citas");

        rclCitas = root.findViewById(R.id.rclCitas);
        rclCitas.setLayoutManager(new LinearLayoutManager(getActivity()));

        btnGenerarCita = root.findViewById(R.id.btnGenerarCita);

        consultaCita(MainActivity.idCliente);

        btnGenerarCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.altaCitaFragment);
            }
        });

        return root;
    }


    @Override
    public void onCitaClick(int posicion, View v) {
        itemListCita = mListCi.get(posicion);
        idCita = mListCi.get(posicion).getIdCita();
        Navigation.findNavController(v).navigate(R.id.modificacionCitasFragment);
    }



    private void consultaCita(int idC){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Commons.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiRestInterface getServices = retrofit.create(ApiRestInterface.class);

        Call<List<Cita>> getCitaCl = getServices.getAllCitaCl(idC);

        getCitaCl.enqueue(new Callback<List<Cita>>() {
            @Override
            public void onResponse(Call<List<Cita>> call, Response<List<Cita>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(
                            getContext(),
                            "Ha ocurrido un error con la consuta " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }else{
                    /*
                    Toast.makeText(
                            getContext(),
                            "todo vien xD " + response.code(),
                            Toast.LENGTH_SHORT).show();

                     */

                    for(Cita c: response.body()){
                        mListCi.add(c);
                    }

                    adapterCita = new MycitaRecyclerViewAdapter(mListCi, getContext(), CitaFragment.this::onCitaClick);
                    rclCitas.setAdapter(adapterCita);
                }
            }

            @Override
            public void onFailure(Call<List<Cita>> call, Throwable t) {
                Toast.makeText(
                        getContext(),"Error de Conexion: " + "\n" + "Error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }



    public Cita getItemListCita(){ return this.itemListCita; }
}