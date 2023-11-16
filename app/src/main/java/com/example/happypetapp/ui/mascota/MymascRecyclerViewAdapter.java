package com.example.happypetapp.ui.mascota;

import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.happypetapp.R;
import com.example.happypetapp.model.Mascota;

import java.util.List;



/**
 * TODO: Replace the implementation with code for your data type.
 */
public class MymascRecyclerViewAdapter extends RecyclerView.Adapter<MymascRecyclerViewAdapter.ViewHolder> {

    private final List<Mascota> mListMcs;

    private Context ctx;

    private OnMascotaListener mOnMascotaListener;

    public MymascRecyclerViewAdapter(List<Mascota> items, Context ctx, OnMascotaListener mListener) {
        mListMcs = items;
        this.ctx = ctx;
        this.mOnMascotaListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_masc, parent, false);
        return new ViewHolder(view, mOnMascotaListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mListMcs.get(position);
        holder.txtNombreMsc.setText(holder.mItem.getNombre());
        holder.txtRazaMsc.setText(holder.mItem.getRaza());
        holder.txtEspecieMsc.setText(holder.mItem.getEspecie());
        holder.txtGeneroMsc.setText(holder.mItem.getSexo());
        holder.txtDescrMsc.setText(holder.mItem.getDescripcion());
        holder.txtEdadMsc.setText(holder.mItem.getEdad());

        Glide.with(ctx).load("data:image/png|jpg|jpeg|gif;base64,"  + holder.mItem.getFoto())
                .into(holder.imgFotoMsc);

    }


    @Override
    public int getItemCount() {
        return mListMcs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final View mView;
        public final TextView txtNombreMsc;
        public final TextView txtRazaMsc;
        public final TextView txtEspecieMsc;
        public final TextView txtGeneroMsc;
        public final TextView txtDescrMsc;
        public final TextView txtEdadMsc;
        public final ImageView imgFotoMsc;
        public Mascota mItem;

        public OnMascotaListener onMascotaListener;

        public ViewHolder(View view, OnMascotaListener onMascotaListener) {
            super(view);
            mView = view;
            this.onMascotaListener = onMascotaListener;

            txtNombreMsc = view.findViewById(R.id.txtNombreMsc);
            txtRazaMsc = view.findViewById(R.id.txtRazaMsc);
            txtEspecieMsc = view.findViewById(R.id.txtEspecieMsc);
            txtGeneroMsc = view.findViewById(R.id.txtGeneroMsc);
            txtDescrMsc = view.findViewById(R.id.txtDescrMsc);
            txtEdadMsc = view.findViewById(R.id.txtEdadMsc);
            imgFotoMsc = view.findViewById(R.id.imgFotoMsc);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onMascotaListener.onMascotaClick(getAdapterPosition(), v);
        }

    }

    public interface OnMascotaListener {
        void onMascotaClick(int posicion, View v);
    }
}