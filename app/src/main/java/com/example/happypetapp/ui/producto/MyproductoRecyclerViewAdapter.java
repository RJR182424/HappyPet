package com.example.happypetapp.ui.producto;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.happypetapp.R;
import com.example.happypetapp.model.Producto;
import com.bumptech.glide.Glide;

import java.util.List;


public class MyproductoRecyclerViewAdapter extends RecyclerView.Adapter<MyproductoRecyclerViewAdapter.ViewHolder> {

    private final List<Producto> mListProductos;
    private Context ctx;
    public MyproductoRecyclerViewAdapter(List<Producto> items, Context ctx) {
        mListProductos= items;
        this.ctx = ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mListProductos.get(position);
        holder.txtNombrePr.setText(holder.mItem.getNombrepr());
        holder.txtDescrPr.setText(holder.mItem.getDescripccion());
        holder.txtPrecioPr.setText("$" + Double.toString(holder.mItem.getPrecio()));

        Glide.with(ctx).load("data:image/png|jpg|jpeg|gif;base64," + holder.mItem.getFoto())
                .into(holder.imgFotoP);
    }

    @Override
    public int getItemCount() {
        return mListProductos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView txtNombrePr;
        public final TextView txtDescrPr;
        public final TextView txtPrecioPr;
        public final ImageView imgFotoP;
        public Producto mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            txtNombrePr = view.findViewById(R.id.lbNombreP);
            txtDescrPr = view.findViewById(R.id.lbDescripcionP);
            txtPrecioPr = view.findViewById(R.id.lbPrecioP);
            imgFotoP = view.findViewById(R.id.imgFotoP);
        }

        /*
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }*/
    }
}