package com.carlos.stellarControl.adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.carlos.stellarControl.R;
import com.carlos.stellarControl.activities.MainAlianza;
import com.carlos.stellarControl.model.Miembro;
import com.carlos.stellarControl.utils.Global;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class AdapterMiembros extends FirestoreRecyclerAdapter<Miembro, AdapterMiembros.ViewHolder> {

    Intent intent;
    String alianzaActual;

    public AdapterMiembros(@NonNull FirestoreRecyclerOptions<Miembro> options, String alianzaActual) {
        super(options);
        this.alianzaActual = alianzaActual;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Miembro miembro) {
        holder.nombre.setText(miembro.getNombre());
        holder.id = miembro.getId();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_miembros, viewGroup, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        String id;
        TextView nombre;
        ImageView imgExpulsar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.nombre = itemView.findViewById(R.id.tvMiembro);
            this.imgExpulsar = itemView.findViewById(R.id.imgExpulsar);

            itemView.findViewById(R.id.imgExpulsar).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(v.getContext());
                    mBuilder.setTitle("Expulsar miembro");
                    mBuilder.setIcon(R.drawable.icon);
                    mBuilder.setMessage("¿Está seguro de eliminar a este miembro de la alianza?");

                    mBuilder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DocumentReference docUser = Global.fFirestore.collection("Alianzas").document(alianzaActual).collection("Miembros_Alianza").document(id);
                            docUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document != null && document.exists()) {
                                            Global.fFirestore.collection("Usuarios").document(id).update("alianza", "");
                                        } else {
                                            Log.d("Check", "No such document");
                                        }
                                    }
                                }
                            });
                            docUser.delete();
                            intent = new Intent(v.getContext(), MainAlianza.class);
                            Toast.makeText(v.getContext(), "Jugador expulsado", Toast.LENGTH_SHORT).show();
                            v.getContext().startActivity(intent);
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
