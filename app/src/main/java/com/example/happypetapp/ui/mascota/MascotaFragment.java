package com.example.happypetapp.ui.mascota;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happypetapp.MainActivity;
import com.example.happypetapp.R;
import com.example.happypetapp.apiRest.ApiRestInterface;
import com.example.happypetapp.commons.Commons;
import com.example.happypetapp.model.Mascota;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MascotaFragment extends Fragment implements MymascRecyclerViewAdapter.OnMascotaListener{

    private MascotaViewModel galleryViewModel;
    private TextView lbEjem;
    private Button btnToFragAltaMascota;
    private RecyclerView rclMascotas;
    private MymascRecyclerViewAdapter adapterMascota;
    static List<Mascota> mListMcs;
    static Mascota itemListMascota;
    private int idC;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(MascotaViewModel.class);

        mListMcs = new ArrayList<>();


        View root = inflater.inflate(R.layout.fragment_mascota, container, false);

        rclMascotas = root.findViewById(R.id.rclMascota);

        rclMascotas.setLayoutManager(new LinearLayoutManager(getActivity()));



        btnToFragAltaMascota = root.findViewById(R.id.btnToFragAltaMascota);

        consumirServicio(MainActivity.idCliente);

        /**
         * La funcionalidad de este boton, es cambiar de actividad con la ayuda de Navigation,
         * esto hace que se mapee, la navegacion de fragments
         */
        btnToFragAltaMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mascFragment.mListMcs = null;
                Navigation.findNavController(v).navigate(R.id.altaMascotaFragment);
            }
        });

        //idC = MainActivity.idCliente;

        /*
        final TextView textView = root.findViewById(R.id.text_gallery);

        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
         */

        return root;
    }

    private void consumirServicio(int idC){
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
                            "todo vien xD " + response.code(),
                            Toast.LENGTH_SHORT).show();*/

                    for(Mascota m:response.body()){
                        mListMcs.add(m);
                    }

                    adapterMascota = new MymascRecyclerViewAdapter(mListMcs, getContext(), MascotaFragment.this::onMascotaClick);
                    rclMascotas.setAdapter(adapterMascota);
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


    @Override
    public void onMascotaClick(int posicion, View v) {
        itemListMascota = mListMcs.get(posicion);
        //Log.i("TAG", itemListMascota.getNombre());
        Navigation.findNavController(v).navigate(R.id.modificarMascotaFragment);
    }


    /*
    Este metodo ayuda a mandar el objeto mascota con el fragment de modificar, utilizando un metodo
    GET sobre un atributo static, esto para conserva su valor en la clase.

    Cabe aclarar, que esto NO SE DEBE DE HACER ASÍ, es solo una solucion rapida para poder obtener
    los datos del elemento seleccionado para mostrarlos en el fragmets para la modificacion de
    mascotas.
     */

    public Mascota getItemListMascota(){
        return this.itemListMascota;
    }


}