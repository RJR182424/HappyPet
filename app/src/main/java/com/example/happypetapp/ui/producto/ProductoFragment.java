package com.example.happypetapp.ui.producto;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happypetapp.R;
import com.example.happypetapp.apiRest.ApiRestInterface;
import com.example.happypetapp.commons.Commons;
import com.example.happypetapp.model.Producto;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductoFragment extends Fragment {

    private ProductoViewModel productoViewModel;
    private RecyclerView rclProductos;
    private List<Producto> mListPr;
    private MyproductoRecyclerViewAdapter adapterProductos;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        productoViewModel =
                new ViewModelProvider(this).get(ProductoViewModel.class);

        mListPr = new ArrayList<>();
        View root = inflater.inflate(R.layout.fragment_producto, container, false);
        rclProductos = root.findViewById(R.id.rclProductos);

        rclProductos.setLayoutManager(new LinearLayoutManager(getActivity()));


        consumirServicio();

        return root;
    }

    private void consumirServicio(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Commons.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiRestInterface getCall = retrofit.create(ApiRestInterface.class);
        Call<List<Producto>> call = getCall.getAllProductos();

        call.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
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

                    for(Producto p:response.body()){
                        mListPr.add(p);
                    }
                    adapterProductos = new MyproductoRecyclerViewAdapter(mListPr, getContext());
                    rclProductos.setAdapter(adapterProductos);

                    Log.i("NOMBRE: ", mListPr.get(0).getNombrepr());
                    Log.i("DESCRIPCION: ", mListPr.get(0).getNombrepr());
                    Log.i("PRECIO: ", mListPr.get(0).getNombrepr());
                }
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                Toast.makeText(
                        getContext(),"Error de Conexion: " + "\n" + "Error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}