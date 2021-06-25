package com.carlos.stellarControl.adapter;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.carlos.stellarControl.R;
import com.carlos.stellarControl.model.Construccion;
import com.carlos.stellarControl.utils.Global;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class AdapterNaves extends FirestoreRecyclerAdapter<Construccion, AdapterNaves.ViewHolder> {

    private DocumentReference docRef;
    private Map<String, String> mapFlota;

    public AdapterNaves(@NonNull FirestoreRecyclerOptions<Construccion> options, Map<String, String> mapFlota) {
        super(options);
        this.mapFlota = mapFlota;
    }

    public Map<String, String> getFlota() {
        return mapFlota;
    }

    @Override
    protected void onBindViewHolder(@NonNull AdapterNaves.ViewHolder holder, int position, @NonNull Construccion construccion) {
        Picasso.get().load(construccion.getImagen()).into(holder.imgNaveFlota);
        holder.naveFlota.setText(construccion.getNombre());

        docRef = Global.fFirestore.collection("Naves_Jugador").document(Global.idPlanetaSeleccionado).collection("Naves_Planeta").document(construccion.getNombre());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        holder.cantidadNave.setText(String.valueOf(document.getLong("cantidad")));
                    } else {
                        Log.d("Check", "No such document");
                    }
                }
            }
        });
    }

    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_naves, viewGroup, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgNaveFlota, imgReducir, imgIncrementar, imgSelectAll, imgDeleteAll;
        TextView naveFlota, cantidadNave, cantidadEnviar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.imgNaveFlota = itemView.findViewById(R.id.imgNaveFlota);
            this.imgReducir = itemView.findViewById(R.id.imgReducir);
            this.imgIncrementar = itemView.findViewById(R.id.imgIncrementar);
            this.imgSelectAll = itemView.findViewById(R.id.imgSelectAll);
            this.imgDeleteAll = itemView.findViewById(R.id.imgDeleteAll);
            this.naveFlota = itemView.findViewById(R.id.tvNaveFlota);
            this.cantidadNave = itemView.findViewById(R.id.tvCantidadNave);
            this.cantidadEnviar = itemView.findViewById(R.id.tvCantidadEnviar);
            cantidadEnviar.setText("0");

            cantidadEnviar.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    mapFlota.put(naveFlota.getText().toString(), cantidadEnviar.getText().toString());
                    if(Integer.parseInt(cantidadNave.getText().toString()) == 0 || cantidadNave.getText() == null){
                        mapFlota.remove(naveFlota.getText().toString());
                    }
                    else{
                        mapFlota.put(naveFlota.getText().toString(), cantidadEnviar.getText().toString());
                    }
                    return false;
                }
            });

            itemView.findViewById(R.id.imgReducir).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    int cantidad = Integer.parseInt(cantidadEnviar.getText().toString());
                    if(cantidad > 0){
                        cantidad = cantidad -1;
                        cantidadEnviar.setText(String.valueOf(cantidad));
                        mapFlota.put(naveFlota.getText().toString(), cantidadEnviar.getText().toString());
                    }
                }
            });

            itemView.findViewById(R.id.imgIncrementar).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    int cantidad = Integer.parseInt(cantidadEnviar.getText().toString());
                    int total = Integer.parseInt(cantidadNave.getText().toString());
                    if(cantidad < total){
                        cantidad = cantidad +1;
                        cantidadEnviar.setText(String.valueOf(cantidad));
                        mapFlota.put(naveFlota.getText().toString(), cantidadEnviar.getText().toString());
                    }
                    if (cantidad == 0){
                        mapFlota.remove(naveFlota.getText().toString());
                    }
                }
            });

            itemView.findViewById(R.id.imgSelectAll).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    cantidadEnviar.setText(cantidadNave.getText());
                    mapFlota.put(naveFlota.getText().toString(), cantidadEnviar.getText().toString());
                }
            });

            itemView.findViewById(R.id.imgDeleteAll).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    cantidadEnviar.setText("");
                    mapFlota.remove(naveFlota.getText().toString());
                }
            });
        }
    }
}
