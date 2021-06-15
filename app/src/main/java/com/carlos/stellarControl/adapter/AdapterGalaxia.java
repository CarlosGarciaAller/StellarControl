package com.carlos.stellarControl.adapter;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.carlos.stellarControl.R;
import com.carlos.stellarControl.model.Planeta;
import com.carlos.stellarControl.utils.Global;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class AdapterGalaxia extends FirestoreRecyclerAdapter<Planeta, AdapterGalaxia.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private DocumentReference docRef;

    public AdapterGalaxia(@NonNull FirestoreRecyclerOptions<Planeta> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AdapterGalaxia.ViewHolder holder, int position, @NonNull Planeta planeta) {
        holder.nombre.setText(planeta.getNombre());
        holder.posicion.setText(String.valueOf(planeta.getPosicion()));
        if (planeta.getUsuario() == null){
            holder.usuario.setText("");
        }
        else{
            holder.usuario.setText(String.valueOf(planeta.getUsuario()));
        }

        docRef = Global.fFirestore.collection("Usuarios").document(Global.fAuth.getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        if(holder.nombre.getText().toString().equals("Kartem")){
                            holder.imgDesplegarPlaneta.setVisibility(View.GONE);
                        }
                    } else {
                        Log.d("Check", "No such document");
                    }
                }
            }
        });
    }

    @NonNull
    @Override
    public AdapterGalaxia.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_galaxy, viewGroup, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        EditText cantidad;
        TextView nombre, posicion, usuario;
        ImageView imgDesplegarPlaneta;
        String usuarioActual;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            this.cantidad = itemView.findViewById(R.id.etCantidad);
            this.nombre = itemView.findViewById(R.id.tvNombrePlaneta);
            this.posicion = itemView.findViewById(R.id.tvPosicionPlaneta);
            this.usuario = itemView.findViewById(R.id.tvNombreJugador);
            this.imgDesplegarPlaneta = itemView.findViewById(R.id.imgDesplegarPlaneta);

            itemView.findViewById(R.id.imgDesplegarPlaneta).setOnClickListener(new View.OnClickListener() {
                String[] listAcciones = new String[0];
                @Override
                public void onClick(View v){
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(v.getContext());
                    if (!usuario.getText().toString().equals("")) {
                        listAcciones = new String[] {"Espiar","Atacar","Agregar"};
                    }

                    else{
                        listAcciones = new String[] {"Colonizar"};
                    }

                    mBuilder.setIcon(R.drawable.icon);
                    mBuilder.setTitle("Acciones");

                    mBuilder.setItems(listAcciones, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            switch(listAcciones[i]){
                                case "Espiar":
                                    Toast.makeText(v.getContext(), "espiar", Toast.LENGTH_SHORT).show();
                                    break;
                                case "Atacar":
                                    Toast.makeText(v.getContext(), "atacar", Toast.LENGTH_SHORT).show();
                                    break;
                                case "Agregar":
                                    Toast.makeText(v.getContext(), "agregar", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    });
                    mBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog mDialog = mBuilder.create();
                    mDialog.show();
                }
            });
        }
    }
}