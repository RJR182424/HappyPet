package com.example.happypetapp.ui.cita.altaCitaViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.happypetapp.R;
import com.example.happypetapp.model.Mascota;
import com.example.happypetapp.ui.mascota.MymascRecyclerViewAdapter;

import java.util.List;

public class MymascotaCitaViewAdapter extends RecyclerView.Adapter<MymascotaCitaViewAdapter.ViewHolder>{

    private final List<Mascota> mListMascCi;
    private Context ctx;
    private OnMascCitaListener mOnMascCitaListener;

    public MymascotaCitaViewAdapter(List<Mascota> items, Context ctx, OnMascCitaListener mListener) {
        mListMascCi = items;
        this.ctx = ctx;
        this.mOnMascCitaListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_cita_masc, parent, false);
        return new ViewHolder(view, mOnMascCitaListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mItem = mListMascCi.get(position);
        holder.txtNMascSel.setText(holder.mItem.getNombre());

        Glide.with(ctx).load("data:image/png|jpg|jpeg|gif;base64,"  + holder.mItem.getFoto())
                .into(holder.imgFotoMascSel);
    }


    @Override
    public int getItemCount() {
        return mListMascCi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final View mView;
        public final ImageView imgFotoMascSel;
        public final TextView txtNMascSel;
        public Mascota mItem;

        public OnMascCitaListener onMascCitaListener;

        public ViewHolder(View view, OnMascCitaListener onMascCitaListener) {
            super(view);
            mView = view;
            this.onMascCitaListener = onMascCitaListener;

            txtNMascSel = view.findViewById(R.id.txtNMascSel);
            imgFotoMascSel = view.findViewById(R.id.imgFotoMascSel);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onMascCitaListener.onMascCitaClick(getAdapterPosition());
        }
    }

    public interface OnMascCitaListener{
        void onMascCitaClick(int posicion);
    }
}
