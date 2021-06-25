package com.carlos.stellarControl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.carlos.stellarControl.R;
import com.carlos.stellarControl.adapter.AdapterNaves;
import com.carlos.stellarControl.model.Construccion;
import com.carlos.stellarControl.utils.Global;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class MainFlota extends AppCompatActivity {
    private RecyclerView rvFlota;
    private AdapterNaves adaptadorNaves;
    RelativeLayout rlFlota;
    LinearLayout llObjetivo, llMision;
    TextView tvPlanetaOrigen, tvCoordenadasOrigen, tvMision, tvDescripcionMision;
    Button btnFlota1, btnFlota2, btnAnterior1, btnAnterior2, btnEnviarFlota;
    EditText etSistemaFlota, etPosicionFlota, etMetal, etCristal, etDeuterio;
    ImageView imgColonizar, imgAtacar, imgTransportar, imgDesplegar, imgAllMetal, imgAllCristal, imgAllDeuterio, imgOutMetal, imgOutCristal, imgOutDeuterio;
    Intent intent;
    Query query;
    Map <String, String> mapFlota = new HashMap<String,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flota);

        //Variables globales

        Global.fAuth = FirebaseAuth.getInstance();
        Global.fFirestore = FirebaseFirestore.getInstance();
        Global.database = FirebaseDatabase.getInstance();

        Global.mensajes = (ImageView) findViewById(R.id.imgMensajes);
        Global.settings = (ImageView) findViewById(R.id.imgSettings);
        Global.imgBack = (ImageView) findViewById(R.id.imgBack);

        Global.tvMetal = (TextView) findViewById(R.id.tvMetal);
        Global.tvCristal = (TextView) findViewById(R.id.tvCristal);
        Global.tvDeuterio = (TextView) findViewById(R.id.tvDeuterio);
        Global.tvEnergia = (TextView) findViewById(R.id.tvEnergia);
        Global.tvPlaneta = (TextView) findViewById(R.id.tvPlaneta);
        Global.tvCoordenadas = (TextView) findViewById(R.id.tvCoordenadas);
        Global.listPlanetas = (LinearLayout) findViewById(R.id.listPlanetas);

        //Fin variables globales

        //variables locales

        rlFlota = (RelativeLayout) findViewById(R.id.rlFlota);

        llObjetivo = (LinearLayout) findViewById(R.id.llObjetivo);
        llMision = (LinearLayout) findViewById(R.id.llMision);

        tvPlanetaOrigen = (TextView) findViewById(R.id.tvPlanetaOrigen);
        tvCoordenadasOrigen = (TextView) findViewById(R.id.tvCoordenadasOrigen);
        tvMision = (TextView) findViewById(R.id.tvMision);
        tvDescripcionMision = (TextView) findViewById(R.id.tvDescripcionMision);

        btnFlota1 = (Button) findViewById(R.id.btnFlota1);
        btnFlota2 = (Button) findViewById(R.id.btnFlota2);
        btnAnterior1 = (Button) findViewById(R.id.btnAnterior1);
        btnAnterior2 = (Button) findViewById(R.id.btnAnterior2);
        btnEnviarFlota = (Button) findViewById(R.id.btnEnviarFlota);
        //btnAccesoRapido = (Button) findViewById(R.id.btnAccesoRapido);


        etSistemaFlota = (EditText) findViewById(R.id.etSistemaFlota);
        etPosicionFlota = (EditText) findViewById(R.id.etPosicionFlota);
        etMetal = (EditText) findViewById(R.id.etMetal);
        etCristal = (EditText) findViewById(R.id.etCristal);
        etDeuterio = (EditText) findViewById(R.id.etDeuterio);

        imgColonizar = (ImageView) findViewById(R.id.imgColonizar);
        imgAtacar = (ImageView) findViewById(R.id.imgAtacar);
        imgTransportar = (ImageView) findViewById(R.id.imgTransportar);
        imgDesplegar = (ImageView) findViewById(R.id.imgDesplegar);
        imgAllMetal = (ImageView) findViewById(R.id.imgAllMetal);
        imgAllCristal = (ImageView) findViewById(R.id.imgAllCristal);
        imgAllDeuterio = (ImageView) findViewById(R.id.imgAllDeuterio);
        imgOutMetal = (ImageView) findViewById(R.id.imgOutMetal);
        imgOutCristal = (ImageView) findViewById(R.id.imgOutCristal);
        imgOutDeuterio = (ImageView) findViewById(R.id.imgOutDeuterio);

        //Fin variables locales

        rvFlota = findViewById(R.id.rvFlota);
        rvFlota.setLayoutManager(new LinearLayoutManager(this));

        query = Global.fFirestore.collection("Naves");

        FirestoreRecyclerOptions<Construccion> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Construccion>().setQuery(query, Construccion.class).build();

        adaptadorNaves = new AdapterNaves(firestoreRecyclerOptions, mapFlota);
        adaptadorNaves.notifyDataSetChanged();
        rvFlota.setAdapter(adaptadorNaves);

        Global.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.volverGeneral(MainFlota.this);
            }
        });

        Global.mensajes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainFlota.this, MainMensajes.class);
                startActivity(intent);
            }
        });

        Global.settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.desplegarOpciones(MainFlota.this);
            }
        });

        Global.listPlanetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.desplegarPlanetas(MainFlota.this, new AlertDialog.Builder(MainFlota.this));
                //global.mostrarPlanetaSeleccionado(global.mFirestore.collection("Planetas").document(global.mAuth.getCurrentUser().getUid()).collection("Planetas_Jugador").whereEqualTo("nombre",global.planetaSeleccionado));
            }
        });

        btnFlota1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adaptadorNaves.getFlota();
                Log.e("inicio", "flota");
                for (Map.Entry<String,String> entry : mapFlota.entrySet()) {
                    Log.e(entry.getKey(), entry.getValue());
                }
                Log.e("fin", "flota");
                rlFlota.setVisibility(View.GONE);
                llObjetivo.setVisibility(View.VISIBLE);
                btnFlota1.setVisibility(View.GONE);
                btnFlota2.setVisibility(View.VISIBLE);
                btnAnterior1.setVisibility(View.VISIBLE);
                tvPlanetaOrigen.setText(Global.planetaSeleccionado);
                tvCoordenadasOrigen.setText("["+Global.sistemaSeleccionado+":"+Global.posicionSeleccionado+"]");
            }
        });

        btnAnterior1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlFlota.setVisibility(View.VISIBLE);
                llObjetivo.setVisibility(View.GONE);
                btnFlota1.setVisibility(View.VISIBLE);
                btnFlota2.setVisibility(View.GONE);
                btnAnterior1.setVisibility(View.GONE);
            }
        });

        btnFlota2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llObjetivo.setVisibility(View.GONE);
                llMision.setVisibility(View.VISIBLE);
                btnFlota2.setVisibility(View.GONE);
                btnAnterior1.setVisibility(View.GONE);
                btnAnterior2.setVisibility(View.VISIBLE);
                btnEnviarFlota.setVisibility(View.VISIBLE);
            }
        });

        btnAnterior2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llObjetivo.setVisibility(View.VISIBLE);
                llMision.setVisibility(View.GONE);
                btnFlota2.setVisibility(View.VISIBLE);
                btnAnterior2.setVisibility(View.GONE);
                btnEnviarFlota.setVisibility(View.GONE);
            }
        });

        btnEnviarFlota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference docPlaneta = Global.fFirestore.collection("Sistemas").document(String.valueOf(etSistemaFlota.getText())).collection("Planetas_Sistemas").document(String.valueOf(etPosicionFlota.getText()));
                switch (tvMision.getText().toString()){
                    case "Colonizar":
                        colonizar(Integer.parseInt(String.valueOf(etSistemaFlota.getText())),
                                Integer.parseInt(String.valueOf(etPosicionFlota.getText())));
                        break;
                    case "Atacar":
                        docPlaneta.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document != null && document.exists()) {
                                        DocumentReference docAlianza = Global.fFirestore.collection("Alianzas").document("79i88tr3igvf43u").collection("Planetas_Sistemas").document(document.getString("idPlanetaJugador"));
                                        docAlianza.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document != null && document.exists()) {
                                                        Toast.makeText(MainFlota.this, "Objetivo no valido para el ataque: no se puede atacar a los planetas de tus aliados.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        });
                                        if (document.getString("usuario") == Global.usuarioActual){
                                            Toast.makeText(MainFlota.this, "Objetivo no valido para el ataque: no se puede atacar a tus colonias.", Toast.LENGTH_SHORT).show();
                                        }
                                        if (document.getString("idPlanetaJugador") == " "){
                                            Toast.makeText(MainFlota.this, "Objetivo no valido para el ataque: planeta vacio", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            //Toast.makeText(MainFlota.this, "Atacar!", Toast.LENGTH_SHORT).show();
                                            atacar(Integer.parseInt(String.valueOf(etSistemaFlota.getText())), Integer.parseInt(String.valueOf(etPosicionFlota.getText())), document.getString("idPlanetaJugador"));
                                        }
                                    } else {
                                        Log.d("Check", "No such document");
                                    }
                                }
                            }
                        });
                        break;
                    case "Transportar":
                        docPlaneta.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document != null && document.exists()) {
                                        if (document.getString("idPlanetaJugador") == " "){
                                            Toast.makeText(MainFlota.this, "Objetivo no valido para el transporte: planeta vacio", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Log.d("Check", "Transportar a "+document.getString("idPlanetaJugador")+" sistema: "+etSistemaFlota.getText()+" posicion: "+etPosicionFlota.getText());
                                            transportar(Integer.parseInt(String.valueOf(etSistemaFlota.getText())), Integer.parseInt(String.valueOf(etPosicionFlota.getText())), document.getString("idPlanetaJugador"));
                                        }
                                    } else {
                                        Log.d("Check", "No such document");
                                    }
                                }
                            }
                        });
                        break;
                    case "Desplegar":
                        docPlaneta.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document != null && document.exists()) {
                                        if (document.getString("idPlanetaJugador").equals(" ")){
                                            Toast.makeText(MainFlota.this, "Objetivo no valido para el despliegue: planeta vacio", Toast.LENGTH_SHORT).show();
                                        }
                                        if (document.getString("usuario").equals(Global.usuarioActual) == false){
                                            Toast.makeText(MainFlota.this, "Objetivo no valido para el despliegue: no se puede desplegar naves a mundos ocupados.", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Log.d("Check", "Desplegar a "+document.getString("idPlanetaJugador")+" sistema: "+etSistemaFlota.getText()+" posicion: "+etPosicionFlota.getText());
                                            //Toast.makeText(MainFlota.this, "Desplegar a "+document.getString("idPlanetaJugador")+" sistema: "+Integer.parseInt(String.valueOf(etSistemaFlota.getText())+" posicion: "+Integer.parseInt(String.valueOf(etPosicionFlota.getText()))), Toast.LENGTH_SHORT).show();
                                            desplegar(Integer.parseInt(String.valueOf(etSistemaFlota.getText())), Integer.parseInt(String.valueOf(etPosicionFlota.getText())), document.getString("idPlanetaJugador"));
                                        }
                                    } else {
                                        Log.d("Check", "No such document");
                                    }
                                }
                            }
                        });
                }
            }
        });

        imgColonizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMision.setText("Colonizar");
                tvDescripcionMision.setText("Coloniza un nuevo planeta");
            }
        });

        imgAtacar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMision.setText("Atacar");
                tvDescripcionMision.setText("Ataca las naves y defensas del objetivo");
            }
        });

        imgTransportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMision.setText("Transportar");
                tvDescripcionMision.setText("Mueve recursos a otro planeta");
            }
        });

        imgDesplegar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMision.setText("Desplegar");
                tvDescripcionMision.setText("Despliega naves y recursos a otro planeta");
            }
        });

        imgAllMetal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etMetal.setText(Global.tvMetal.getText());
            }
        });

        imgAllCristal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etCristal.setText(Global.tvCristal.getText());
            }
        });

        imgAllDeuterio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etDeuterio.setText(Global.tvDeuterio.getText());
            }
        });

        imgOutMetal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etMetal.setText("0");
            }
        });

        imgOutCristal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etCristal.setText("0");
            }
        });

        imgOutDeuterio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etDeuterio.setText("0");
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        adaptadorNaves.startListening();
        //Mostrar el planeta seleccionado en el selecctor de planetas
        Global.mostrarPlanetaSeleccionado(Global.fFirestore.collection("Planetas").document(Global.fAuth.getCurrentUser().getUid()).collection("Planetas_Jugador").whereEqualTo("nombre",Global.planetaSeleccionado));

        //Cargar los planetas en el selector
        Global.cargarSelectPlanetas();

        rlFlota.setVisibility(View.VISIBLE);
        llObjetivo.setVisibility(View.GONE);
        llMision.setVisibility(View.GONE);
    }

    @Override
    protected void onStop(){
        super.onStop();
        adaptadorNaves.stopListening();
    }

    public void comprobarDestino(DocumentReference docPlaneta){

    }

    public void colonizar(int sistema, int posicion){
        DocumentReference docColonizador = Global.fFirestore.collection("Naves_Jugador").document(Global.idPlanetaSeleccionado).collection("Naves_Planeta").document("Colonizador");
        docColonizador.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        if (document.getLong("cantidad") == 0){
                            Toast.makeText(MainFlota.this, "No es posible colonizar sin un colonizador.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            DocumentReference docPlaneta = Global.fFirestore.collection("Sistemas").document(String.valueOf(sistema)).collection("Planetas_Sistemas").document(String.valueOf(posicion));
                            docPlaneta.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document != null && document.exists()) {
                                            if (document.getString("idPlanetaJugador").equals(" ")){
                                                String idPlaneta = Global.fFirestore.collection("Planetas").document(Global.fAuth.getCurrentUser().getUid()).collection("Planetas_Jugador").document().getId();

                                                Global.asignarPlaneta(Global.fAuth, Global.fFirestore, idPlaneta,"Colonia", Global.usuarioActual, Integer.parseInt(String.valueOf(etMetal.getText())),
                                                        Integer.parseInt(String.valueOf(etCristal.getText())),
                                                        Integer.parseInt(String.valueOf(etDeuterio.getText())),
                                                        sistema, posicion, true);
                                                Global.inicializarRecursosPlaneta(Global.fFirestore, idPlaneta);
                                                Global.inicializarInstalacionesPlaneta(Global.fFirestore, idPlaneta);
                                                Global.inicializarNavesPlaneta(Global.fFirestore, idPlaneta);
                                                Global.inicializarDefensasPlaneta(Global.fFirestore, idPlaneta);

                                                //Mensaje

                                                String asunto = "Informe colonización";
                                                String categoria = "Flota";
                                                String mensaje = "Se ha finalizado con exito la colonización del planeta ["+sistema+":"+posicion+"] Haz crecer tu nuevo mundo para seguir expandiendo tu imperio";
                                                String remitente = "Jefe de flota";
                                                Global.crearMensaje(Global.fFirestore, Global.fAuth.getCurrentUser().getUid(), asunto, categoria, mensaje, remitente);

                                                //Fin mensaje

                                                intent = new Intent(MainFlota.this, MainGeneral.class);
                                                intent.putExtra("anteriorActividad", "colonizar");
                                                Toast.makeText(MainFlota.this, "Planeta colonizado con éxito", Toast.LENGTH_SHORT).show();
                                                startActivity(intent);
                                            }
                                            else{
                                                Log.d("sistema: ", String.valueOf(sistema));
                                                Log.d("posicion: ", String.valueOf(posicion));
                                                Toast.makeText(MainFlota.this, "No es posible colonizar, el planeta ya está ocupado.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else{
                                            Log.d("Check", "No such document");
                                        }
                                    }
                                }
                            });
                        }
                    }
                    else{
                        Log.d("Check", "No such document");
                    }
                }
            }
        });
    }

    public void atacar(int sistema, int posicion, String idPlaneta){
        for (int i = 0; i < Global.naves.length; i++) {
            DocumentReference docPlanetaDestinoNaves = Global.fFirestore.collection("Naves_Jugador").document(idPlaneta).collection("Naves_Planeta").document(Global.naves[i]);
            docPlanetaDestinoNaves.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            docPlanetaDestinoNaves.update("cantidad", 0);
                        }
                    }
                }
            });
        }
        for (int i = 0; i < Global.defensas.length; i++) {
            DocumentReference docPlanetaDestinoDefensas = Global.fFirestore.collection("Defensas_Jugador").document(idPlaneta).collection("Defensas_Planeta").document(Global.defensas[i]);
            docPlanetaDestinoDefensas.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            docPlanetaDestinoDefensas.update("cantidad", 0);
                        }
                    }
                }
            });
        }

        //Mensaje

        String asunto = "Informe ataque";
        String categoria = "Informes de batalla";
        String mensaje = "La flota ha invadido y neutralizado con exito las naves y defensas del planeta objetivo: ["+sistema+":"+posicion+"]";
        String remitente = "Jefe de flota";
        Global.crearMensaje(Global.fFirestore, Global.fAuth.getCurrentUser().getUid(), asunto, categoria, mensaje, remitente);

        //Fin mensaje

        intent = new Intent(MainFlota.this, MainGeneral.class);
        intent.putExtra("anteriorActividad", "colonizar");
        Toast.makeText(MainFlota.this, "Planeta colonizado con éxito", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    public void transportar(int sistema, int posicion, String idPlaneta){
        Log.e("Desplegando", "flota");
        DocumentReference docPlanetaDestino = Global.fFirestore.collection("Planetas").document().collection("Planetas_Jugador").document(idPlaneta);
        docPlanetaDestino.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        docPlanetaDestino.update("metal", document.getLong("metal") + Integer.parseInt(String.valueOf(etMetal.getText())));
                        docPlanetaDestino.update("cristal", document.getLong("cristal") + Integer.parseInt(String.valueOf(etCristal.getText())));
                        docPlanetaDestino.update("deuterio", document.getLong("deuterio") + Integer.parseInt(String.valueOf(etDeuterio.getText())));
                    }
                }
            }
        });

        DocumentReference docPlanetaOrigen = Global.fFirestore.collection("Planetas").document(Global.fAuth.getCurrentUser().getUid()).collection("Planetas_Jugador").document(idPlaneta);
        docPlanetaOrigen.update("metal", Global.cantidadMetal - Integer.parseInt(String.valueOf(etMetal.getText())));
        docPlanetaOrigen.update("cristal", Global.cantidadCristal - Integer.parseInt(String.valueOf(etCristal.getText())));
        docPlanetaOrigen.update("deuterio", Global.cantidadDeuterio - Integer.parseInt(String.valueOf(etDeuterio.getText())));

        //Mensaje

        String asunto = "Informe transporte";
        String categoria = "Flota";
        String mensaje = "La flota ha entregado con exito la mercancia. " +
                "Metal: "+etMetal.getText()+" Cristal: "+etCristal.getText()+" Deuterio"+etDeuterio.getText()+" Al planeta ubicado en ["+sistema+":"+posicion+"]";
        String remitente = "Jefe de flota";
        Global.crearMensaje(Global.fFirestore, Global.fAuth.getCurrentUser().getUid(), asunto, categoria, mensaje, remitente);

        //Fin mensaje

        intent = new Intent(MainFlota.this, MainFlota.class);
        intent.putExtra("anteriorActividad", "colonizar");
        Toast.makeText(MainFlota.this, "Recursos transportados con exito", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    public void desplegar(int sistema, int posicion, String idPlaneta){
        Log.e("Desplegando", "flota");
        for (Map.Entry<String,String> entry : mapFlota.entrySet()) {
            Log.e(entry.getKey(), entry.getValue());
            DocumentReference docPlanetaDestino = Global.fFirestore.collection("Naves_Jugador").document(idPlaneta).collection("Naves_Planeta").document(entry.getKey());
            docPlanetaDestino.update("cantidad", Integer.parseInt(entry.getValue()));
            DocumentReference docPlanetaOrigen = Global.fFirestore.collection("Naves_Jugador").document(Global.idPlanetaSeleccionado).collection("Naves_Planeta").document(entry.getKey());
            int navesFuera = Integer.parseInt(entry.getValue()) - Integer.parseInt(entry.getValue());
            Log.e("Restando", String.valueOf(navesFuera));
            docPlanetaOrigen.update("cantidad", Integer.parseInt(entry.getValue()) - Integer.parseInt(entry.getValue()));
        }

        //Mensaje

        String asunto = "Informe despliegue";
        String categoria = "Flota";
        String mensaje = "La flota se ha desplegado con exito en el planeta ["+sistema+":"+posicion+"]";
        String remitente = "Jefe de flota";
        Global.crearMensaje(Global.fFirestore, Global.fAuth.getCurrentUser().getUid(), asunto, categoria, mensaje, remitente);

        //Fin mensaje

        intent = new Intent(MainFlota.this, MainGeneral.class);
        intent.putExtra("anteriorActividad", "colonizar");
        Toast.makeText(MainFlota.this, "Planeta colonizado con éxito", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }
}