package com.carlos.stellarControl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    String planetaSeleccionado;
    Global global = new Global();
    Button btnConstruir;

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

        Global.tvMetal = (TextView) findViewById(R.id.tvMetal);
        Global.tvCristal = (TextView) findViewById(R.id.tvCristal);
        Global.tvDeuterio = (TextView) findViewById(R.id.tvDeuterio);
        Global.tvPlaneta = (TextView) findViewById(R.id.tvPlaneta);
        Global.tvCoordenadas = (TextView) findViewById(R.id.tvCoordenadas);

        tvTituloConstruccion = (TextView) findViewById(R.id.tvTituloConstruccion);
        tvTituloConstruccion.setText(construccion);

        rvConstrucciones = (RecyclerView) findViewById(R.id.rvConstrucciones);
        rvConstrucciones.setLayoutManager(new LinearLayoutManager(this));

        switch(MainConstruccion.construccion){
            case "Recursos":
                query = global.fFirestore.collection("Recursos");
                break;
            case "Instalaciones":
                query = global.fFirestore.collection("Instalaciones");
                break;
            case "Investigaciones":
                query = global.fFirestore.collection("Investigaciones");
                break;
            case "Naves":
                query = global.fFirestore.collection("Naves");
                break;
            case "Defensas":
                query = global.fFirestore.collection("Defensas");
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
                global.cargarPlanetas();
                //Global.desplegarPlanetas(MainConstruccion.this, new AlertDialog.Builder(MainConstruccion.this));
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        adaptadorConstrucciones.startListening();

        //Mostrar el planeta seleccionado en el selecctor de planetas
        global.mostrarPlanetaSeleccionado(global.fFirestore.collection("Planetas").document(global.fAuth.getCurrentUser().getUid()).collection("Planetas_Jugador").whereEqualTo("nombre",global.planetaSeleccionado));

        //Cargar los recursos del planeta seleccionado
        global.cargarRecursos(global.planetaSeleccionado);

        Toast.makeText(MainConstruccion.this, "Planeta seleccionado: "+global.planetaSeleccionado, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop(){
        super.onStop();
        adaptadorConstrucciones.stopListening();
    }

    public static void mejorarConstruccion(String construccion, String nombre, int nivel){
        switch(construccion){
            case "Recursos":
                nivel = nivel+1;
                Global.fFirestore.collection("Recursos_Jugador").document("36RIVrMuyHkBXSMHtjOx").collection("Recursos_Planeta").document(nombre).update("cantidad", nivel);
                break;
            case "Instalaciones":
                nivel = nivel+1;
                Global.fFirestore.collection("Instalaciones_Jugador").document("36RIVrMuyHkBXSMHtjOx").collection("Instalaciones_Planeta").document(nombre).update("cantidad", nivel);
                break;
            case "Investigaciones":
                nivel = nivel+1;
                Global.fFirestore.collection("Investigaciones_Jugador").document("36RIVrMuyHkBXSMHtjOx").collection("Investigaciones_Planeta").document(nombre).update("cantidad", nivel);
                break;
            case "Naves":
                nivel = nivel+1;
                Global.fFirestore.collection("Naves_Jugador").document("36RIVrMuyHkBXSMHtjOx").collection("Naves_Planeta").document(nombre).update("cantidad", +nivel);
                break;
            case "Defensas":
                nivel = nivel+1;
                Global.fFirestore.collection("Defensas_Jugador").document("36RIVrMuyHkBXSMHtjOx").collection("Defensas_Planeta").document(nombre).update("cantidad", +nivel);
                break;
        }
        /*query = fFirestore.collection(construccion).document(fAuth.getCurrentUser().getUid()).collection("Recursos_Jugador").document("9DiJdIR9Qp41ElEY9nJX");

        fFirestore.collection(construccion).document(fAuth.getCurrentUser().getUid()).collection("Recursos_Planeta").document("9DiJdIR9Qp41ElEY9nJX").update("cantidad", nivel);*/
    }
}