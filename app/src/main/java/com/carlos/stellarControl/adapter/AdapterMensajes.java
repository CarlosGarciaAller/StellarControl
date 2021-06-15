package com.carlos.stellarControl.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.carlos.stellarControl.R;
import com.carlos.stellarControl.model.Mensaje;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class AdapterMensajes extends FirestoreRecyclerAdapter<Mensaje, AdapterMensajes.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AdapterMensajes(@NonNull FirestoreRecyclerOptions<Mensaje> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AdapterMensajes.ViewHolder holder, int position, @NonNull Mensaje mensaje) {
        holder.remitente.setText(mensaje.getRemitente());
        holder.asunto.setText(mensaje.getAsunto());
        holder.contenido.setText(mensaje.getContenido());
    }

    @NonNull
    @Override
    public AdapterMensajes.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_mensajes, viewGroup, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView remitente, asunto, contenido;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            this.remitente = itemView.findViewById(R.id.tvRemitente);
            this.asunto = itemView.findViewById(R.id.tvAsunto);
            this.contenido = itemView.findViewById(R.id.tvContenido);
        }
    }
}

