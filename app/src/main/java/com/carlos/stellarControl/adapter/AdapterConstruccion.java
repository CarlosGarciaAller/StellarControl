package com.carlos.stellarControl.adapter;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.carlos.stellarControl.R;
import com.carlos.stellarControl.activities.MainConstruccion;
import com.carlos.stellarControl.model.Construccion;
import com.carlos.stellarControl.utils.Global;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class AdapterConstruccion extends FirestoreRecyclerAdapter<Construccion, AdapterConstruccion.ViewHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private DocumentReference docRef;

    public AdapterConstruccion(@NonNull FirestoreRecyclerOptions<Construccion> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Construccion construccion) {
        holder.nombre.setText(construccion.getNombre());
        holder.descripcion.setText(construccion.getDescripcion());
        Picasso.get().load(construccion.getImagen()).into(holder.imgConstruccion);
        //holder.requisitosInvestigaciones.put("0",construccion.getRequisitosInvestigaciones());

        switch(MainConstruccion.construccion){
            case "Recursos":
                docRef = Global.fFirestore.collection("Recursos_Jugador").document(Global.idPlanetaSeleccionado).collection("Recursos_Planeta").document(construccion.getNombre());
                break;
            case "Instalaciones":
                docRef = Global.fFirestore.collection("Instalaciones_Jugador").document(Global.idPlanetaSeleccionado).collection("Instalaciones_Planeta").document(construccion.getNombre());
                break;
            case "Investigaciones":
                docRef = Global.fFirestore.collection("Investigaciones_Jugador").document(Global.fAuth.getUid()).collection("Investigaciones_Jugador").document(construccion.getNombre());
                break;
            case "Naves":
                Log.e("Check",Global.idPlanetaSeleccionado);
                docRef = Global.fFirestore.collection("Naves_Jugador").document(Global.idPlanetaSeleccionado).collection("Naves_Planeta").document(construccion.getNombre());
                break;
            case "Defensas":
                docRef = Global.fFirestore.collection("Defensas_Jugador").document(Global.idPlanetaSeleccionado).collection("Defensas_Planeta").document(construccion.getNombre());
                break;
        }

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        holder.cantidad.setText(String.valueOf(document.getLong("cantidad")));
                        holder.costeMetal.setText(String.valueOf(document.getLong("costeMetal")));
                        holder.costeCristal.setText(String.valueOf(document.getLong("costeCristal")));
                        holder.costeDeuterio.setText(String.valueOf(document.getLong("costeDeuterio")));
                        holder.costeEnergia.setText(String.valueOf(document.getLong("costeEnergia")));
                    } else {
                        Log.d("Check", "No such document");
                    }
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_construcciones, viewGroup, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgConstruccion, imgHelp;
        TextView nombre, descripcion, costeMetal, costeCristal, costeDeuterio, costeEnergia, cantidad, nivel, textCantidad, textNivel;
        TableRow rowMetal, rowEnergia;
        EditText etCantidad;
        Button btnConstruir, btnDemoler;
        /*Map requisitosInvestigaciones = new HashMap<String,Integer>();
        Map requisitosInstalaciones = new HashMap<String,Integer>();*/

        Global global = new Global();

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            this.imgConstruccion = itemView.findViewById(R.id.imgConstruccion);
            this.imgHelp = itemView.findViewById(R.id.imgHelp);
            this.nombre = itemView.findViewById(R.id.tvConstruccion);
            this.costeMetal = itemView.findViewById(R.id.tvCosteMetal);
            this.costeCristal = itemView.findViewById(R.id.tvCosteCristal);
            this.costeDeuterio = itemView.findViewById(R.id.tvCosteDeuterio);
            this.costeEnergia = itemView.findViewById(R.id.tvCosteEnergia);
            this.rowMetal = itemView.findViewById(R.id.rowMetal);
            this.rowEnergia = itemView.findViewById(R.id.rowEnergia);
            this.cantidad = itemView.findViewById(R.id.tvCantidad);
            this.nivel = itemView.findViewById(R.id.tvNivel);
            this.textCantidad = itemView.findViewById(R.id.tvTextCantidad);
            this.textNivel = itemView.findViewById(R.id.tvTextNivel);
            this.etCantidad = itemView.findViewById(R.id.etCantidadEnviar);
            this.btnConstruir = itemView.findViewById(R.id.btnConstruir);
            this.btnDemoler = itemView.findViewById(R.id.btnDemoler);
            this.descripcion = new TextView(itemView.getContext());



            if ("Recursos".equals(MainConstruccion.construccion) || "Instalaciones".equals(MainConstruccion.construccion)){
                textCantidad.setVisibility(View.GONE);
                etCantidad.setVisibility(View.GONE);
                nivel.setVisibility(View.GONE);
            }
            if("Investigaciones".equals(MainConstruccion.construccion)){
                textNivel.setVisibility(View.GONE);
                etCantidad.setVisibility(View.GONE);
                nivel.setVisibility(View.GONE);
                btnDemoler.setVisibility(View.GONE);
            }
            else{
                //this.cantidad = itemView.findViewById(R.id.tvNivel);
                textNivel.setVisibility(View.GONE);
            }

            imgHelp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(itemView.getContext());
                    mBuilder.setTitle("Informacion");
                    mBuilder.setIcon(R.drawable.icon);
                    mBuilder.setView(descripcion);
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

            btnConstruir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    //Global.mejorarConstruccion(MainConstruccion.construccion, nombre.getText().toString(), Integer.parseInt(String.valueOf(cantidad.getText())));
                }
            });

            //Toast.makeText(itemView.getContext(), "Construir"+costeEnergia.getText(), Toast.LENGTH_SHORT).show();

        }


    }

}