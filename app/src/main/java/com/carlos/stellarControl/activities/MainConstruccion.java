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
import com.carlos.stellarControl.adapter.AdapterConstruccion;
import com.carlos.stellarControl.model.Construccion;
import com.carlos.stellarControl.utils.Global;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainConstruccion extends AppCompatActivity {
    Intent intent;
    Query query;
    public static String construccion;
    TextView tvTituloConstruccion;
    private RecyclerView rvConstrucciones;
    private AdapterConstruccion adaptadorConstrucciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_construcciones);

        Bundle extras = getIntent().getExtras();
        construccion = extras.getString("Construcción");

        Global.fFirestore = FirebaseFirestore.getInstance();
        Global.fAuth = FirebaseAuth.getInstance();

        Global.mensajes = (ImageView) findViewById(R.id.imgMensajes);
        Global.settings = (ImageView) findViewById(R.id.imgSettings);
        Global.imgBack = (ImageView) findViewById(R.id.imgBack);
        Global.imgPlanetaSeleccionado = (ImageView) findViewById(R.id.imgPlanetaSeleccionado);

        Global.tvMetal = (TextView) findViewById(R.id.tvMetal);
        Global.tvCristal = (TextView) findViewById(R.id.tvCristal);
        Global.tvDeuterio = (TextView) findViewById(R.id.tvDeuterio);
        Global.tvEnergia = (TextView) findViewById(R.id.tvEnergia);
        Global.tvPlaneta = (TextView) findViewById(R.id.tvPlaneta);
        Global.tvCoordenadas = (TextView) findViewById(R.id.tvCoordenadas);
        Global.imgPlanetaSeleccionado = (ImageView) findViewById(R.id.imgPlanetaSeleccionado);
        Global.listPlanetas = (LinearLayout) findViewById(R.id.listPlanetas);

        tvTituloConstruccion = (TextView) findViewById(R.id.tvTituloConstruccion);
        tvTituloConstruccion.setText(construccion);

        rvConstrucciones = (RecyclerView) findViewById(R.id.rvConstrucciones);
        rvConstrucciones.setLayoutManager(new LinearLayoutManager(this));

        switch(MainConstruccion.construccion){
            case "Recursos":
                query = Global.fFirestore.collection("Recursos");
                break;
            case "Instalaciones":
                query = Global.fFirestore.collection("Instalaciones");
                break;
            case "Investigaciones":
                query = Global.fFirestore.collection("Investigaciones");
                break;
            case "Naves":
                query = Global.fFirestore.collection("Naves");
                break;
            case "Defensas":
                query = Global.fFirestore.collection("Defensas");
                break;
        }

        FirestoreRecyclerOptions<Construccion> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Construccion>().setQuery(query, Construccion.class).build();

        adaptadorConstrucciones = new AdapterConstruccion(firestoreRecyclerOptions);
        adaptadorConstrucciones.notifyDataSetChanged();
        rvConstrucciones.setAdapter(adaptadorConstrucciones);

        Global.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.volverGeneral(MainConstruccion.this);
            }
        });

        Global.mensajes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainConstruccion.this, MainMensajes.class);
                intent.putExtra("categoria", "Consulta");
                startActivity(intent);
            }
        });

        Global.settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.desplegarOpciones(MainConstruccion.this);
            }
        });

        Global.listPlanetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cargar los planetas en el selector
                //Global.cargarSelectPlanetas();
                Global.desplegarPlanetas(MainConstruccion.this, new AlertDialog.Builder(MainConstruccion.this));
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        adaptadorConstrucciones.startListening();

        //Mostrar el planeta seleccionado en el selecctor de planetas
        Global.mostrarPlanetaSeleccionado(Global.fFirestore.collection("Planetas").document(Global.fAuth.getCurrentUser().getUid()).collection("Planetas_Jugador").whereEqualTo("nombre",Global.planetaSeleccionado));
    }

    @Override
    protected void onStop(){
        super.onStop();
        adaptadorConstrucciones.stopListening();
    }
}