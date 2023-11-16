package com.example.happypetapp.ui.cita.altaCitaViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happypetapp.R;
import com.example.happypetapp.model.Servicio;

import java.util.List;

public class MyservicioCitaViewAdapter extends RecyclerView.Adapter<MyservicioCitaViewAdapter.ViewHolder> {

    private final List<Servicio> mListServCi;
    private Context ctx;
    private OnServicioCitaListener mOnServicioCitaListener;

    public MyservicioCitaViewAdapter(List<Servicio> mListServCi, Context ctx, OnServicioCitaListener mListener) {
        this.mListServCi = mListServCi;
        this.ctx = ctx;
        this.mOnServicioCitaListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_cita_servicio, parent, false);
        return new ViewHolder(view, mOnServicioCitaListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mItem = mListServCi.get(position);
        holder.txtNServicioSel.setText(holder.mItem.getNombreS());
        holder.txtPServicioSel.setText(Double.toString(holder.mItem.getPrecio()));
        holder.txtDServicioSel.setText(holder.mItem.getDescrip());
    }

    @Override
    public int getItemCount() {
        return mListServCi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final View mView;
        public final TextView txtNServicioSel;
        public final TextView txtPServicioSel;
        public final TextView txtDServicioSel;
        public Servicio mItem;
        public OnServicioCitaListener onServicioCitaListener;

        public ViewHolder(View view, OnServicioCitaListener onServicioCitaListener) {
            super(view);
            mView = view;
            this.onServicioCitaListener = onServicioCitaListener;

            txtNServicioSel = view.findViewById(R.id.txtNServicioSel);
            txtPServicioSel = view.findViewById(R.id.txtPServicioSel);
            txtDServicioSel = view.findViewById(R.id.txtDServicioSel);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onServicioCitaListener.onServicioCitaClick(getAdapterPosition());
        }
    }

    public interface OnServicioCitaListener{
        void onServicioCitaClick(int posicion);
    }
}
