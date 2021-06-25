package com.carlos.stellarControl.adapter;

import android.content.Intent;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.carlos.stellarControl.R;
import com.carlos.stellarControl.activities.MainConstruccion;
import com.carlos.stellarControl.model.Construccion;
import com.carlos.stellarControl.utils.Global;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class AdapterConstruccion extends FirestoreRecyclerAdapter<Construccion, AdapterConstruccion.ViewHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    Intent intent;
    //private boolean noCumpleInstalaciones, noCumpleInvestigaciones = false;

    public AdapterConstruccion(@NonNull FirestoreRecyclerOptions<Construccion> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Construccion construccion) {
        holder.nombre.setText(construccion.getNombre());
        holder.descripcion.setText(construccion.getDescripcion());
        Picasso.get().load(construccion.getImagen()).into(holder.imgConstruccion);
        DocumentReference docConstruccion;
        if("Investigaciones".equals(MainConstruccion.construccion)){
            docConstruccion = Global.fFirestore.collection("Investigaciones_Jugador").document(Global.fAuth.getUid()).collection("Investigaciones_Jugador").document(construccion.getNombre());
        }
        else{
            docConstruccion = Global.fFirestore.collection(MainConstruccion.construccion+"_Jugador").document(Global.idPlanetaSeleccionado).collection(MainConstruccion.construccion+"_Planeta").document(construccion.getNombre());
        }
        docConstruccion.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
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
                            if (!Global.recursosSuficientes("Metal", Integer.parseInt(String.valueOf(holder.tvCosteMetal.getText())))){
                                //holder.tvCosteMetal.setTextColor(Color.parseColor("#FF0000"));
                            }
                        }
                        if (Integer.parseInt(String.valueOf(document.getLong("costeCristal"))) == 0){
                            holder.rowCristal.setVisibility(View.GONE);
                        }
                        else{
                            holder.tvCosteCristal.setText(String.valueOf(document.getLong("costeCristal")));
                            if (!Global.recursosSuficientes("Cristal", Integer.parseInt(String.valueOf(holder.tvCosteCristal.getText())))){
                                //holder.tvCosteCristal.setTextColor(Color.parseColor("#FF0000"));
                            }
                        }
                        if (Integer.parseInt(String.valueOf(document.getLong("costeDeuterio"))) == 0){
                            holder.rowDeuterio.setVisibility(View.GONE);
                        }
                        else{
                            holder.tvCosteDeuterio.setText(String.valueOf(document.getLong("costeDeuterio")));
                            if (!Global.recursosSuficientes("Deuterio", Integer.parseInt(String.valueOf(holder.tvCosteDeuterio.getText())))){
                                //holder.tvCosteDeuterio.setTextColor(Color.parseColor("#FF0000"));
                            }
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
        ImageView imgConstruccion, imgHelp;
        TextView nombre, descripcion, requisito, nivelRequerido, tvCosteMetal, tvCosteCristal, tvCosteDeuterio, tvCosteEnergia, cantidad, tvCantidadProducir, textCantidad, textNivel;
        TableRow rowMetal, rowCristal, rowDeuterio, rowEnergia;
        EditText etCantidad;
        Button btnMejorar, btnConstruir, btnInvestigar, btnDemoler;
        Integer nivel, costeMetal, costeCristal, costeDeuterio, costeEnergia;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            this.imgConstruccion = itemView.findViewById(R.id.imgConstruccion);
            this.imgHelp = itemView.findViewById(R.id.imgHelp);
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
            this.btnMejorar = itemView.findViewById(R.id.btnMejorar);
            this.btnConstruir = itemView.findViewById(R.id.btnConstruir);
            this.btnDemoler = itemView.findViewById(R.id.btnDemoler);
            this.btnInvestigar = itemView.findViewById(R.id.btnInvestigar);

            this.descripcion = new TextView(itemView.getContext());
            this.requisito = new TextView(itemView.getContext());
            this.nivelRequerido = new TextView(itemView.getContext());

            etCantidad.setInputType(InputType.TYPE_CLASS_NUMBER);

            if ("Recursos".equals(MainConstruccion.construccion) || "Instalaciones".equals(MainConstruccion.construccion)){
                textNivel.setVisibility(View.VISIBLE);
                textCantidad.setVisibility(View.GONE);
                tvCantidadProducir.setVisibility(View.GONE);
                etCantidad.setVisibility(View.GONE);
                btnMejorar.setVisibility(View.VISIBLE);
                btnConstruir.setVisibility(View.GONE);
                btnInvestigar.setVisibility(View.GONE);
                btnDemoler.setText("Demoler");
            }
            if ("Investigaciones".equals(MainConstruccion.construccion)){
                textNivel.setVisibility(View.VISIBLE);
                textCantidad.setVisibility(View.GONE);
                tvCantidadProducir.setVisibility(View.GONE);
                etCantidad.setVisibility(View.GONE);
                btnInvestigar.setVisibility(View.VISIBLE);
                btnMejorar.setVisibility(View.GONE);
                btnConstruir.setVisibility(View.GONE);
                btnDemoler.setVisibility(View.GONE);
            }
            if ("Naves".equals(MainConstruccion.construccion) || "Defensas".equals(MainConstruccion.construccion)){
                textCantidad.setVisibility(View.VISIBLE);
                textNivel.setVisibility(View.GONE);
                tvCantidadProducir.setVisibility(View.VISIBLE);
                etCantidad.setVisibility(View.VISIBLE);
                btnConstruir.setVisibility(View.VISIBLE);
                btnMejorar.setVisibility(View.GONE);
                btnInvestigar.setVisibility(View.GONE);
                btnDemoler.setText("Desguazar");
            }

            imgHelp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    Toast.makeText(v.getContext(), nombre.getText()+": "+descripcion.getText(), Toast.LENGTH_SHORT).show();
                }
            });

            btnMejorar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    nivel = Integer.parseInt(String.valueOf(cantidad.getText()));
                    costeMetal = Integer.parseInt(String.valueOf(tvCosteMetal.getText()));
                    costeCristal = Integer.parseInt(String.valueOf(tvCosteCristal.getText()));
                    costeDeuterio = Integer.parseInt(String.valueOf(tvCosteDeuterio.getText()));
                    costeEnergia = Integer.parseInt(String.valueOf(tvCosteEnergia.getText()));
                    boolean insuficiente = false;

                    if(Global.camposUsadosSeleccionado.equals(Global.camposTotalesSeleccionado)){
                        Toast.makeText(v.getContext(), "No hay espacio suficiente para mas construcciones, libera espacio y/o construye un terraformer para ampliar espacio.", Toast.LENGTH_SHORT).show();
                        Log.d("Check", "Espacio insuficiente, Espacio disponible: "+Global.camposTotalesSeleccionado+" Espacios ocupados: "+Global.camposUsadosSeleccionado);
                    }
                    else{
                        if (comprobarRequisitosInstalaciones(nombre.getText().toString()) || comprobarRequisitosInvestigaciones(nombre.getText().toString())){
                            Toast.makeText(v.getContext(), "No cumples los requisitos para construir", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            if (costeMetal > 0 || costeCristal > 0 || costeDeuterio > 0){
                                if (Integer.parseInt(String.valueOf(Global.tvMetal.getText())) < costeMetal ||
                                        Integer.parseInt(String.valueOf(Global.tvCristal.getText())) < costeCristal ||
                                        Integer.parseInt(String.valueOf(Global.tvDeuterio.getText())) < costeDeuterio){
                                    insuficiente = true;
                                }
                            }
                            if(insuficiente){
                                Toast.makeText(v.getContext(), "Recursos insuficientes", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                mejorarConstruccion(MainConstruccion.construccion, nivel);

                                intent = new Intent(v.getContext(), MainConstruccion.class);
                                intent.putExtra("Construcción", MainConstruccion.construccion);
                                v.getContext().startActivity(intent);
                            }
                        }
                    }
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
                    boolean insuficiente = false;

                    /*comprobarRequisitosInstalaciones(nombre.getText().toString());
                    comprobarRequisitosInvestigaciones(nombre.getText().toString());*/
                    /*Toast.makeText(v.getContext(), comprobarRequisitosInstalaciones(nombre.getText().toString())+":"+comprobarRequisitosInvestigaciones(nombre.getText().toString()), Toast.LENGTH_SHORT).show();
                    if (comprobarRequisitosInstalaciones(nombre.getText().toString()) || comprobarRequisitosInvestigaciones(nombre.getText().toString())){
                        Toast.makeText(v.getContext(), "No cumples los requisitos para construir", Toast.LENGTH_SHORT).show();
                    }*/
                    //else{
                        if (etCantidad.getText().toString().equals("0") || etCantidad.getText().toString().isEmpty()){
                            etCantidad.setText("1");
                        }
                        else{
                            Integer cantidad = Integer.parseInt(etCantidad.getText().toString());
                            costeMetal = costeMetal * cantidad;
                            costeCristal = costeCristal * cantidad;
                            costeDeuterio = costeDeuterio * cantidad;
                        }
                        if (costeMetal > 0 || costeCristal > 0 || costeDeuterio > 0){
                            if (Integer.parseInt(String.valueOf(Global.tvMetal.getText())) < costeMetal ||
                                    Integer.parseInt(String.valueOf(Global.tvCristal.getText())) < costeCristal ||
                                    Integer.parseInt(String.valueOf(Global.tvDeuterio.getText())) < costeDeuterio){
                                insuficiente = true;
                            }
                        }
                        if(insuficiente){
                            Toast.makeText(v.getContext(), "Recursos insuficientes", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            mejorarConstruccion(MainConstruccion.construccion, nivel);

                            intent = new Intent(v.getContext(), MainConstruccion.class);
                            intent.putExtra("Construcción", MainConstruccion.construccion);
                            v.getContext().startActivity(intent);
                        }
                    //}
                }
            });

            btnInvestigar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nivel = Integer.parseInt(String.valueOf(cantidad.getText()));
                    costeMetal = Integer.parseInt(String.valueOf(tvCosteMetal.getText()));
                    costeCristal = Integer.parseInt(String.valueOf(tvCosteCristal.getText()));
                    costeDeuterio = Integer.parseInt(String.valueOf(tvCosteDeuterio.getText()));
                    costeEnergia = Integer.parseInt(String.valueOf(tvCosteEnergia.getText()));
                    boolean insuficiente = false;
                    comprobarRequisitosInstalaciones(nombre.getText().toString());
                    comprobarRequisitosInvestigaciones(nombre.getText().toString());
                    /*if (comprobarRequisitosInstalaciones(nombre.getText().toString()) && comprobarRequisitosInvestigaciones(nombre.getText().toString())){
                        Toast.makeText(v.getContext(), "No cumples los requisitos para construir", Toast.LENGTH_SHORT).show();
                    }*/
                    //else{
                        if (costeMetal > 0 || costeCristal > 0 || costeDeuterio > 0){
                            if (Integer.parseInt(String.valueOf(Global.tvMetal.getText())) < costeMetal ||
                                    Integer.parseInt(String.valueOf(Global.tvCristal.getText())) < costeCristal ||
                                    Integer.parseInt(String.valueOf(Global.tvDeuterio.getText())) < costeDeuterio ||
                                    Integer.parseInt(String.valueOf(Global.tvEnergia.getText())) < costeEnergia){
                                insuficiente = true;
                            }
                        }
                        if(insuficiente){
                            Toast.makeText(v.getContext(), "Recursos insuficientes", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            investigar(nivel);

                            intent = new Intent(v.getContext(), MainConstruccion.class);
                            intent.putExtra("Construcción", MainConstruccion.construccion);
                            v.getContext().startActivity(intent);
                        }
                    //}
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
            DocumentReference docConstruccion = Global.fFirestore.collection(construccion+"_Jugador").document(Global.idPlanetaSeleccionado).collection(construccion+"_Planeta").document(nombre.getText().toString());
            DocumentReference docPlaneta = Global.fFirestore.collection("Planetas").document(Global.fAuth.getCurrentUser().getUid()).collection("Planetas_Jugador").document(Global.idPlanetaSeleccionado);
            if(costeMetal > 0){
                docPlaneta.update("metal", Integer.parseInt(Global.tvMetal.getText().toString()) - costeMetal);
            }
            if (costeCristal > 0){
                docPlaneta.update("cristal", Integer.parseInt(Global.tvCristal.getText().toString()) - costeCristal);
            }
            if (costeDeuterio > 0){
                docPlaneta.update("deuterio", Integer.parseInt(Global.tvDeuterio.getText().toString()) - costeDeuterio);
            }
            if (costeEnergia > 0){
                docConstruccion.update("costeEnergia", costeEnergia * 2);
            }
            if (MainConstruccion.construccion.equals("Naves") || MainConstruccion.construccion.equals("Defensas")){
                docConstruccion.update("cantidad", nivel + Integer.parseInt(etCantidad.getText().toString()));
            }
            else{
                docConstruccion.update("cantidad", nivel + 1);
                if(costeMetal > 0){
                    docConstruccion.update("costeMetal", costeMetal * 2);
                }
                if (costeCristal > 0){
                    docConstruccion.update("costeCristal", costeCristal * 2);
                }
                if (costeDeuterio > 0){
                    docConstruccion.update("costeDeuterio", costeDeuterio * 2);
                }
                if (costeEnergia > 0){
                    docConstruccion.update("costeEnergia", costeEnergia * 2);
                }
                docPlaneta.update("camposUsados", Global.camposUsadosSeleccionado + 1);
            }
            if (nombre.getText().toString().equals("Planta de energia solar")){
                docPlaneta.update("energia", Global.capacidadMetal / 2);
            }
            if (nombre.getText().toString().equals("Almacen de metal")){
                docPlaneta.update("capacidadMetal", Global.capacidadMetal * 2);
            }
            if (nombre.getText().toString().equals("Almacen de cristal")){
                docPlaneta.update("capacidadCristal", Global.capacidadCristal * 2);
            }
            if (nombre.getText().toString().equals("Almacen de deuterio")){
                docPlaneta.update("capacidadDeuterio", Global.capacidadDeuterio * 2);
            }
            if (nombre.getText().toString().equals("Terraformer")){
                docPlaneta.update("camposTotales", Global.camposTotalesSeleccionado + 5);
            }
        }

        public void demolerConstruccion(String construccion, int nivel){
            DocumentReference docConstruccion = Global.fFirestore.collection(construccion+"_Jugador").document(Global.idPlanetaSeleccionado).collection(construccion+"_Planeta").document(nombre.getText().toString());
            DocumentReference docPlaneta = Global.fFirestore.collection("Planetas").document(Global.fAuth.getCurrentUser().getUid()).collection("Planetas_Jugador").document(Global.idPlanetaSeleccionado);
            if (MainConstruccion.construccion.equals("Naves") || MainConstruccion.construccion.equals("Defensas")){
                docConstruccion.update("cantidad", nivel - 1);
            }
            else{
                docConstruccion.update("cantidad", nivel - 1);
                if(costeMetal > 0){
                    docConstruccion.update("costeMetal", costeMetal / 2);
                }
                if (costeCristal > 0){
                    docConstruccion.update("costeCristal", costeCristal / 2);
                }
                if (costeDeuterio > 0){
                    docConstruccion.update("costeDeuterio", costeDeuterio / 2);
                }
                docPlaneta.update("camposUsados", Global.camposUsadosSeleccionado - 1);
            }
            if (nombre.getText().toString().equals("Planta de energia solar")){
                docPlaneta.update("energia", Global.capacidadMetal / 2);
            }
            if (nombre.getText().toString().equals("Almacen de metal")){
                docPlaneta.update("capacidadMetal", Global.capacidadMetal / 2);
            }
            if (nombre.getText().toString().equals("Almacen de cristal")){
                docPlaneta.update("capacidadCristal", Global.capacidadCristal / 2);
            }
            if (nombre.getText().toString().equals("Almacen de deuterio")){
                docPlaneta.update("capacidadDeuterio", Global.capacidadDeuterio / 2);
            }
            if (nombre.getText().toString().equals("Terraformer")){
                docPlaneta.update("camposTotales", Global.camposTotalesSeleccionado - 5);
            }
        }

        public void investigar(int nivel){
            DocumentReference docInvestigacion = Global.fFirestore.collection("Investigaciones_Jugador").document(Global.fAuth.getUid()).collection("Investigaciones_Jugador").document(nombre.getText().toString());
            docInvestigacion.update("cantidad", nivel + 1);
            if(costeMetal > 0){
                docInvestigacion.update("costeMetal", costeMetal * 2);
            }
            if (costeCristal > 0){
                docInvestigacion.update("costeCristal", costeCristal * 2);
            }
            if (costeDeuterio > 0){
                docInvestigacion.update("costeDeuterio", costeDeuterio * 2);
            }
            if (costeEnergia > 0){
                docInvestigacion.update("costeEnergia", costeEnergia * 2);
            }
        }

        //al tratar de construir una construcción, comprobamos si cumple los requisitos de instalaciones
        public boolean comprobarRequisitosInstalaciones(String construccion){
            final boolean[] noCumpleInstalaciones = {false};
            //comprobamos los requisitos de tipo investigacion
            CollectionReference docRequisitosInstalaciones = Global.fFirestore.collection("Requisitos").document(construccion).collection("Requisitos_Instalaciones");
            docRequisitosInstalaciones.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                    if (task1.isSuccessful()) {
                        for (DocumentSnapshot document1 : task1.getResult()) {
                            if (document1 != null && document1.exists()) {
                                //comprobamos los requisitos de tipo instalacion
                                DocumentReference docInstalaciones = Global.fFirestore.collection("Instalaciones_Jugador").document(Global.idPlanetaSeleccionado).collection("Instalaciones_Planeta").document(document1.getString("nombre"));
                                docInstalaciones.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task2) {
                                        DocumentSnapshot document2 = task2.getResult();
                                        if (document2 != null && document2.exists()) {
                                            if (document2.getLong("cantidad") < document1.getLong("nivel")){
                                                noCumpleInstalaciones[0] = true;
                                                Log.d("Check", "No cumple los requisitos, " + " Instalacion requerida: "+document1.getString("nombre")+" Nivel requerido: "+document1.getLong("nivel").toString()+ " Instalacion actual: "+document2.getString("nombre")+" Nivel actual: "+document2.getLong("cantidad"));
                                            }
                                        }
                                        else{
                                            Log.d("Check", "No such document");
                                        }
                                    }
                                });
                            } else {
                                Log.d("Check", "No such document");
                            }
                            /*Log.d("Check", String.valueOf(noCumpleInstalaciones));
                            if (noCumpleInstalaciones){
                                break;
                            }*/
                        }
                    }

                }
            });
            return noCumpleInstalaciones[0];
        }

        //al tratar de construir una construcción, comprobamos si cumple los requisitos de investigaciones
        public boolean comprobarRequisitosInvestigaciones(String construccion){
            final boolean[] noCumpleInvestigaciones = {false};
            //recogemos los requisitos de una construccion tipo instalacion
            CollectionReference docRequisitosInvestigaciones = Global.fFirestore.collection("Requisitos").document(construccion).collection("Requisitos_Investigaciones");
            docRequisitosInvestigaciones.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                    if (task1.isSuccessful()) {
                        for (DocumentSnapshot document1 : task1.getResult()) {
                            if (document1 != null && document1.exists()) {
                                //comprobamos los requisitos de tipo investigacion
                                DocumentReference docInstalaciones = Global.fFirestore.collection("Investigaciones_Jugador").document(Global.fAuth.getCurrentUser().getUid()).collection("Investigaciones_Jugador").document(document1.getString("nombre"));
                                docInstalaciones.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task2) {
                                        DocumentSnapshot document2 = task2.getResult();
                                        if (document2 != null && document2.exists()) {
                                            if (document2.getLong("cantidad") < document1.getLong("nivel")){
                                                noCumpleInvestigaciones[0] = true;
                                                Log.d("Check", "No cumple los requisitos, " + " Investigacion requerido: "+document1.getString("nombre")+" Nivel requerido: "+document1.getLong("nivel").toString()+ " Investigacion actual: "+document2.getString("nombre")+" Nivel actual: "+document2.getLong("cantidad"));
                                            }
                                        }
                                        else{
                                            Log.d("Check", "No such document");
                                        }
                                    }
                                });
                            } else {
                                Log.d("Check", "No such document");
                            }
                            /*Log.d("Check", String.valueOf(noCumpleInvestigaciones));
                            if (noCumpleInvestigaciones){
                                break;
                            }*/
                        }
                    }
                }
            });
            return noCumpleInvestigaciones[0];
        }
    }
}