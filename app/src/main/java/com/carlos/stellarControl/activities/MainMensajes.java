package com.carlos.stellarControl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.carlos.stellarControl.R;
import com.carlos.stellarControl.adapter.AdapterMensajes;
import com.carlos.stellarControl.model.Mensaje;
import com.carlos.stellarControl.utils.Global;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainMensajes extends AppCompatActivity {
    Intent intent;
    private RecyclerView rvMensajes;
    private AdapterMensajes adaptadorMensajes;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajes);

        //Variables globales

        Global.fAuth = FirebaseAuth.getInstance();
        Global.fFirestore = FirebaseFirestore.getInstance();

        Global.settings = (ImageView) findViewById(R.id.imgSettings);
        Global.imgBack = (ImageView) findViewById(R.id.imgBack);
        Global.imgPlanetaSeleccionado = (ImageView) findViewById(R.id.imgPlanetaSeleccionado);

        Global.tvMetal = (TextView) findViewById(R.id.tvMetal);
        Global.tvCristal = (TextView) findViewById(R.id.tvCristal);
        Global.tvDeuterio = (TextView) findViewById(R.id.tvDeuterio);
        Global.tvEnergia = (TextView) findViewById(R.id.tvEnergia);
        Global.tvPlaneta = (TextView) findViewById(R.id.tvPlaneta);
        Global.tvCoordenadas = (TextView) findViewById(R.id.tvCoordenadas);
        Global.listPlanetas = (LinearLayout) findViewById(R.id.listPlanetas);

        //Fin variables globales

        rvMensajes = findViewById(R.id.rvMensajes);
        rvMensajes.setLayoutManager(new LinearLayoutManager(this));

        query = Global.fFirestore.collection("Mensajes").document(Global.fAuth.getCurrentUser().getUid()).collection("Mensajes_Jugador");

        FirestoreRecyclerOptions<Mensaje> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Mensaje>().setQuery(query, Mensaje.class).build();

        adaptadorMensajes = new AdapterMensajes(firestoreRecyclerOptions);
        adaptadorMensajes.notifyDataSetChanged();
        rvMensajes.setAdapter(adaptadorMensajes);

        Global.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.volverGeneral(MainMensajes.this);
            }
        });

        Global.settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.desplegarOpciones(MainMensajes.this);
            }
        });

        Global.listPlanetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.desplegarPlanetas(MainMensajes.this, new AlertDialog.Builder(MainMensajes.this));
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();

        adaptadorMensajes.startListening();

        //Mostrar el planeta seleccionado en el selecctor de planetas
        Global.mostrarPlanetaSeleccionado(Global.fFirestore.collection("Planetas").document(Global.fAuth.getCurrentUser().getUid()).collection("Planetas_Jugador").whereEqualTo("nombre",Global.planetaSeleccionado));

        //Cargar los planetas en el selector
        Global.cargarSelectPlanetas();
    }

    @Override
    protected void onStop(){
        super.onStop();

        adaptadorMensajes.stopListening();
    }
}
