package com.carlos.stellarControl.adapter;

import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Map;

public class AdapterConstruccion extends FirestoreRecyclerAdapter<Construccion, AdapterConstruccion.ViewHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private DocumentReference docRef, docRequisitos;
    Intent intent;

    public AdapterConstruccion(@NonNull FirestoreRecyclerOptions<Construccion> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Construccion construccion) {
        holder.nombre.setText(construccion.getNombre());
        holder.descripcion.setText(construccion.getDescripcion());
        Picasso.get().load(construccion.getImagen()).into(holder.imgConstruccion);

        if("Investigaciones".equals(MainConstruccion.construccion)){
            docRef = Global.fFirestore.collection("Investigaciones_Jugador").document(Global.fAuth.getUid()).collection("Investigaciones_Jugador").document(construccion.getNombre());
        }
        else{
            docRef = Global.fFirestore.collection(MainConstruccion.construccion+"_Jugador").document(Global.idPlanetaSeleccionado).collection(MainConstruccion.construccion+"_Planeta").document(construccion.getNombre());
        }
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        holder.cantidad.setText(String.valueOf(document.getLong("cantidad")));
                        if (Integer.parseInt(String.valueOf(document.getLong("cantidad"))) == 0){
                            holder.btnDemoler.setVisibility(View.GONE);
                        }
                        if (Integer.parseInt(String.valueOf(document.getLong("costeMetal"))) == 0) {
                            holder.rowMetal.setVisibility(View.GONE);
                        }
                        else{
                            holder.tvCosteMetal.setText(String.valueOf(document.getLong("costeMetal")));
                        }
                        if (Integer.parseInt(String.valueOf(document.getLong("costeCristal"))) == 0){
                            holder.rowCristal.setVisibility(View.GONE);
                        }
                        else{
                            holder.tvCosteCristal.setText(String.valueOf(document.getLong("costeCristal")));
                        }
                        if (Integer.parseInt(String.valueOf(document.getLong("costeDeuterio"))) == 0){
                            holder.rowDeuterio.setVisibility(View.GONE);
                        }
                        else{
                            holder.tvCosteDeuterio.setText(String.valueOf(document.getLong("costeDeuterio")));
                        }
                        if (Integer.parseInt(String.valueOf(document.getLong("costeEnergia"))) == 0){
                            holder.rowEnergia.setVisibility(View.GONE);
                        }
                        else{
                            holder.tvCosteEnergia.setText(String.valueOf(document.getLong("costeEnergia")));
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_construcciones, viewGroup, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgConstruccion, imgHelp, imgRequisitos;
        TextView nombre, descripcion, requisitos, tvCosteMetal, tvCosteCristal, tvCosteDeuterio, tvCosteEnergia, cantidad, tvCantidadProducir, textCantidad, textNivel;
        TableRow rowMetal, rowCristal, rowDeuterio, rowEnergia;
        EditText etCantidad;
        Button btnConstruir, btnDemoler;
        Integer nivel, costeMetal, costeCristal, costeDeuterio, costeEnergia;

        Global global = new Global();

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            this.imgConstruccion = itemView.findViewById(R.id.imgConstruccion);
            this.imgHelp = itemView.findViewById(R.id.imgHelp);
            this.imgRequisitos = itemView.findViewById(R.id.imgRequisitos);
            this.nombre = itemView.findViewById(R.id.tvConstruccion);
            this.tvCosteMetal = itemView.findViewById(R.id.tvCosteMetal);
            this.tvCosteCristal = itemView.findViewById(R.id.tvCosteCristal);
            this.tvCosteDeuterio = itemView.findViewById(R.id.tvCosteDeuterio);
            this.tvCosteEnergia = itemView.findViewById(R.id.tvCosteEnergia);
            this.rowMetal = itemView.findViewById(R.id.rowMetal);
            this.rowCristal = itemView.findViewById(R.id.rowCristal);
            this.rowDeuterio = itemView.findViewById(R.id.rowDeuterio);
            this.rowEnergia = itemView.findViewById(R.id.rowEnergia);
            this.cantidad = itemView.findViewById(R.id.tvCantidad);
            this.tvCantidadProducir = itemView.findViewById(R.id.tvCantidadProducir);
            this.textCantidad = itemView.findViewById(R.id.tvTextCantidad);
            this.textNivel = itemView.findViewById(R.id.tvTextNivel);
            this.etCantidad = itemView.findViewById(R.id.etCantidadEnviar);
            this.btnConstruir = itemView.findViewById(R.id.btnConstruir);
            this.btnDemoler = itemView.findViewById(R.id.btnDemoler);

            this.descripcion = new TextView(itemView.getContext());
            this.requisitos = new TextView(itemView.getContext());
            requisitos.setText("requisitos");

            if ("Recursos".equals(MainConstruccion.construccion) || "Instalaciones".equals(MainConstruccion.construccion)){
                textNivel.setVisibility(View.VISIBLE);
                textCantidad.setVisibility(View.GONE);
                tvCantidadProducir.setVisibility(View.GONE);
                etCantidad.setVisibility(View.GONE);
            }
            if("Investigaciones".equals(MainConstruccion.construccion)){
                textNivel.setVisibility(View.GONE);
                textCantidad.setVisibility(View.GONE);
                tvCantidadProducir.setVisibility(View.GONE);
                etCantidad.setVisibility(View.GONE);
                btnDemoler.setVisibility(View.GONE);
            }
            if("Naves".equals(MainConstruccion.construccion) || "Defensas".equals(MainConstruccion.construccion)){
                //this.cantidad = itemView.findViewById(R.id.tvNivel);
                textNivel.setVisibility(View.GONE);
                textCantidad.setVisibility(View.VISIBLE);
                tvCantidadProducir.setVisibility(View.VISIBLE);
                etCantidad.setVisibility(View.VISIBLE);
            }

            //if (Integer.parseInt(String.valueOf(tvCosteMetal.getText())) == 0){
            Log.e("Coste metal: ",String.valueOf(tvCosteMetal.getText()));


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

            imgRequisitos.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(itemView.getContext());
                    mBuilder.setTitle("Requisitos");
                    mBuilder.setIcon(R.drawable.icon);

                    docRequisitos = Global.fFirestore.collection("Requisitos").document(String.valueOf(nombre.getText()));
                    docRequisitos.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document != null && document.exists()) {
                                    ArrayList<String> list = new ArrayList<>();
                                    Map<String, Object> map = task.getResult().getData();
                                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                                        list.add(entry.getKey());
                                        list.add(entry.getValue().toString());
                                        Log.d("TAG", list.get(0) + ": " + list.get(1));
                                        requisitos.setText(list.get(0)+": "+list.get(1));
                                        Log.e("Coste metal: ", requisitos.getText().toString());

                                        //list.clear();
                                    }
                                    mBuilder.setItems(list.toArray(new String[0]), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) { }
                                    });
                                } else {
                                    Log.d("Check", "No such document");
                                }
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

            btnConstruir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){

                    nivel = Integer.parseInt(String.valueOf(cantidad.getText()));
                    costeMetal = Integer.parseInt(String.valueOf(tvCosteMetal.getText()));
                    costeCristal = Integer.parseInt(String.valueOf(tvCosteCristal.getText()));
                    costeDeuterio = Integer.parseInt(String.valueOf(tvCosteDeuterio.getText()));
                    costeEnergia = Integer.parseInt(String.valueOf(tvCosteEnergia.getText()));

                    if("Investigaciones".equals(MainConstruccion.construccion)){
                        investigar(nivel);
                    }
                    else{
                        mejorarConstruccion(MainConstruccion.construccion, nivel);
                    }

                    intent = new Intent(v.getContext(), MainConstruccion.class);
                    intent.putExtra("Construcción", MainConstruccion.construccion);
                    v.getContext().startActivity(intent);
                }
            });

            btnDemoler.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){

                    nivel = Integer.parseInt(String.valueOf(cantidad.getText()));
                    costeMetal = Integer.parseInt(String.valueOf(tvCosteMetal.getText()));
                    costeCristal = Integer.parseInt(String.valueOf(tvCosteCristal.getText()));
                    costeDeuterio = Integer.parseInt(String.valueOf(tvCosteDeuterio.getText()));
                    costeEnergia = Integer.parseInt(String.valueOf(tvCosteEnergia.getText()));


                    demolerConstruccion(MainConstruccion.construccion, nivel);

                    intent = new Intent(v.getContext(), MainConstruccion.class);
                    intent.putExtra("Construcción", MainConstruccion.construccion);
                    v.getContext().startActivity(intent);
                }
            });
        }

        public void mejorarConstruccion(String construccion, int nivel){
            Global.fFirestore.collection(construccion+"_Jugador").document(Global.idPlanetaSeleccionado).collection(construccion+"_Planeta").document(nombre.getText().toString()).update("cantidad", nivel + 1);
            if(costeMetal > 0){
                Global.fFirestore.collection(construccion+"_Jugador").document(Global.idPlanetaSeleccionado).collection(construccion+"_Planeta").document(nombre.getText().toString()).update("costeMetal", costeMetal * 2);
            }
            if (costeCristal > 0){
                Global.fFirestore.collection(construccion+"_Jugador").document(Global.idPlanetaSeleccionado).collection(construccion+"_Planeta").document(nombre.getText().toString()).update("costeCristal", costeCristal * 2);
            }
            if (costeDeuterio > 0){
                Global.fFirestore.collection(construccion+"_Jugador").document(Global.idPlanetaSeleccionado).collection(construccion+"_Planeta").document(nombre.getText().toString()).update("costeDeuterio", costeDeuterio * 2);
            }
            if (costeEnergia > 0){
                Global.fFirestore.collection(construccion+"_Jugador").document(Global.idPlanetaSeleccionado).collection(construccion+"_Planeta").document(nombre.getText().toString()).update("costeEnergia", costeEnergia * 2);
            }
        }

        public void demolerConstruccion(String construccion, int nivel){
            Global.fFirestore.collection(construccion+"_Jugador").document(Global.idPlanetaSeleccionado).collection(construccion+"_Planeta").document(nombre.getText().toString()).update("cantidad", nivel - 1);
            if(costeMetal > 0){
                Global.fFirestore.collection(construccion+"_Jugador").document(Global.idPlanetaSeleccionado).collection(construccion+"_Planeta").document(nombre.getText().toString()).update("costeMetal", costeMetal / 2);
            }
            if (costeCristal > 0){
                Global.fFirestore.collection(construccion+"_Jugador").document(Global.idPlanetaSeleccionado).collection(construccion+"_Planeta").document(nombre.getText().toString()).update("costeCristal", costeCristal / 2);
            }
            if (costeDeuterio > 0){
                Global.fFirestore.collection(construccion+"_Jugador").document(Global.idPlanetaSeleccionado).collection(construccion+"_Planeta").document(nombre.getText().toString()).update("costeDeuterio", costeDeuterio / 2);
            }
            if (costeEnergia > 0){
                Global.fFirestore.collection(construccion+"_Jugador").document(Global.idPlanetaSeleccionado).collection(construccion+"_Planeta").document(nombre.getText().toString()).update("costeEnergia", costeEnergia / 2);
            }
        }

        public void investigar(int nivel){
            Global.fFirestore.collection("Investigaciones_Jugador").document(Global.fAuth.getUid()).collection("Investigaciones_Jugador").document(nombre.getText().toString()).update("cantidad", nivel + 1);
            if(costeMetal > 0){
                Global.fFirestore.collection("Investigaciones_Jugador").document(Global.fAuth.getUid()).collection("Investigaciones_Jugador").document(nombre.getText().toString()).update("costeMetal", costeMetal * 2);
            }
            if (costeCristal > 0){
                Global.fFirestore.collection("Investigaciones_Jugador").document(Global.fAuth.getUid()).collection("Investigaciones_Jugador").document(nombre.getText().toString()).update("costeCristal", costeCristal * 2);
            }
            if (costeDeuterio > 0){
                Global.fFirestore.collection("Investigaciones_Jugador").document(Global.fAuth.getUid()).collection("Investigaciones_Jugador").document(nombre.getText().toString()).update("costeDeuterio", costeDeuterio * 2);
            }
            if (costeEnergia > 0){
                Global.fFirestore.collection("Investigaciones_Jugador").document(Global.fAuth.getUid()).collection("Investigaciones_Jugador").document(nombre.getText().toString()).update("costeEnergia", costeEnergia * 2);
            }
        }

        public void comprobarRequisitos(){

        }
    }

}