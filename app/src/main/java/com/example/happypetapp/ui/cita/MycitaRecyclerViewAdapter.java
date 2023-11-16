package com.example.happypetapp.ui.cita;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happypetapp.R;
import com.example.happypetapp.model.Cita;
import com.example.happypetapp.ui.mascota.MymascRecyclerViewAdapter;

import java.util.List;

public class MycitaRecyclerViewAdapter extends RecyclerView.Adapter<MycitaRecyclerViewAdapter.ViewHolder> {

    private final List<Cita> mListCita;
    private Context ctx;
    private OnCitaListener mOnCitaListener;

    public MycitaRecyclerViewAdapter(List<Cita> items, Context ctx, OnCitaListener mListener) {
        mListCita = items;
        this.ctx = ctx;
        this.mOnCitaListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_cita, parent, false);

        return new ViewHolder(view, mOnCitaListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mListCita.get(position);

        switch(holder.mItem.getEstatus()){
            case 1:
                holder.mView.setBackgroundColor(holder.mView.getResources().getColor(R.color.estatus_1));
                break;
            case 2:
                holder.mView.setBackgroundColor(holder.mView.getResources().getColor(R.color.estatus_2));
                break;
            case 0:
                holder.mView.setBackgroundColor(holder.mView.getResources().getColor(R.color.estatus_0));
                break;
        }

        holder.txtNServicio.setText(holder.mItem.getServicio().getNombreS());
        holder.txtNMascota.setText(holder.mItem.getMascota().getNombre());
        holder.txtPrecioS.setText("$"+ holder.mItem.getServicio().getPrecio());
        holder.txtFecha.setText(holder.mItem.getFechaHoraIniAsString());
        holder.txtDescripcionS.setText(holder.mItem.getServicio().getDescrip());

        Log.i("ROW", holder.mItem.getServicio().getNombreS());

    }

    @Override
    public int getItemCount() {
        return mListCita.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final View mView;
        public final TextView txtNServicio;
        public final TextView txtNMascota;
        public final TextView txtPrecioS;
        public final TextView txtFecha;
        public final TextView txtDescripcionS;
        public Cita mItem;

        public OnCitaListener onCitaListener;

        public ViewHolder(View view, OnCitaListener onCitaListener) {
            super(view);
            mView = view;
            this.onCitaListener = onCitaListener;

            txtNServicio = view.findViewById(R.id.txtNServicio);
            txtNMascota = view.findViewById(R.id.txtNMascota);
            txtPrecioS = view.findViewById(R.id.txtPrecioS);
            txtFecha = view.findViewById(R.id.txtFecha);
            txtDescripcionS = view.findViewById(R.id.txtDescripcionS);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onCitaListener.onCitaClick(getAdapterPosition(), v);
        }
    }

    public interface OnCitaListener{
        void onCitaClick(int posicion, View v);
    }
}
