package com.carlos.stellarControl.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

public class AdapterNaves extends FirestoreRecyclerAdapter<Construccion, AdapterNaves.ViewHolder> {

    private DocumentReference docRef;

    public AdapterNaves(@NonNull FirestoreRecyclerOptions<Construccion> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AdapterNaves.ViewHolder holder, int position, @NonNull Construccion construccion) {
        Picasso.get().load(construccion.getImagen()).into(holder.imgNaveFlota);

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
    public AdapterNaves.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_naves, viewGroup, false);
        return new AdapterNaves.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgNaveFlota, imgSelectAll, imgDeleteAll;
        TextView cantidadNave;
        EditText cantidadEnviar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.imgNaveFlota = itemView.findViewById(R.id.imgNaveFlota);
            this.imgSelectAll = itemView.findViewById(R.id.imgSelectAll);
            this.imgDeleteAll = itemView.findViewById(R.id.imgDeleteAll);
            this.cantidadNave = itemView.findViewById(R.id.tvCantidadNave);
            this.cantidadEnviar = itemView.findViewById(R.id.etCantidadEnviar);

            itemView.findViewById(R.id.imgSelectAll).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    cantidadEnviar.setText(cantidadNave.getText());
                }
            });

            itemView.findViewById(R.id.imgDeleteAll).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    cantidadEnviar.setText("");
                }
            });
        }
    }
}
