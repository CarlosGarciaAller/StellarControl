package com.carlos.stellarControl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.carlos.stellarControl.R;
import com.carlos.stellarControl.utils.Global;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class MainGeneral extends AppCompatActivity {
    Intent intent;
    Query query;
    Button btnVision, btnRecursos, btnInstalaciones, btnInvestigaciones ,btnHangar, btnDefensas, btnFlota, btnGalaxia, btnAlianza;
    String anteriorActividad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);

        //Variables extras

        Bundle extras = getIntent().getExtras();
        anteriorActividad = extras.getString("anteriorActividad");
        //planetaSeleccionado = extras.getString("nombrePlaneta");

        // Fin variables extras

        //Variables globales

        Global.fAuth = FirebaseAuth.getInstance();
        Global.fFirestore = FirebaseFirestore.getInstance();

        Global.mensajes = (ImageView) findViewById(R.id.imgMensajes);
        Global.settings = (ImageView) findViewById(R.id.imgSettings);
        Global.imgBack = (ImageView) findViewById(R.id.imgBack);

        Global.tvMetal = (TextView)findViewById(R.id.tvMetal);
        Global.tvCristal = (TextView)findViewById(R.id.tvCristal);
        Global.tvDeuterio = (TextView)findViewById(R.id.tvDeuterio);
        Global.tvPlaneta = (TextView)findViewById(R.id.tvPlaneta);
        Global.tvCoordenadas = (TextView)findViewById(R.id.tvCoordenadas);
        Global.listPlanetas = (LinearLayout)findViewById(R.id.listPlanetas);

        //Fin variables globales

        //variables locales

        btnVision = (Button)findViewById(R.id.btnVision);
        btnRecursos = (Button)findViewById(R.id.btnRecursos);
        btnInstalaciones = (Button)findViewById(R.id.btnInstalaciones);
        btnInvestigaciones = (Button)findViewById(R.id.btnInvestigaciones);
        btnHangar = (Button)findViewById(R.id.btnHangar);
        btnDefensas = (Button)findViewById(R.id.btnDefensas);
        btnFlota = (Button)findViewById(R.id.btnFlota);
        btnGalaxia = (Button)findViewById(R.id.btnGalaxia);
        btnAlianza = (Button)findViewById(R.id.btnAlianza);

        //Fin variables locales

        //Cargar los recursos del planeta seleccionado
        if (anteriorActividad.equals("login") || anteriorActividad.equals("registrar")) {
            query = Global.fFirestore.collection("Planetas").document(Global.fAuth.getCurrentUser().getUid()).collection("Planetas_Jugador").whereEqualTo("colonia",false);
            /*query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            planetaSeleccionado = document.getString("nombre");
                            Toast.makeText(MainGeneral.this, "Cargando Planeta seleccionado: "+document.getString("nombre"), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });*/
            Toast.makeText(MainGeneral.this, "Cargando Planeta seleccionado: "+Global.planetaSeleccionado, Toast.LENGTH_SHORT).show();
        }
        else{
            query = Global.fFirestore.collection("Planetas").document(Global.fAuth.getCurrentUser().getUid()).collection("Planetas_Jugador").whereEqualTo("nombre",Global.planetaSeleccionado);

        }

        //Mostrar el planeta seleccionado en el selecctor de planetas
        Global.mostrarPlanetaSeleccionado(query);

        //Cargar los planetas del jugador en el selector
        Global.cargarPlanetas();

        //capacidadMetal = Global.obtenerCapacidad(query, "capacidadMetal");
        //Toast.makeText(MainGeneral.this, planetaSeleccionado, Toast.LENGTH_SHORT).show();
        //Toast.makeText(MainGeneral.this, String.valueOf(capacidadMetal), Toast.LENGTH_SHORT).show();

        /*if (getClass().getName().equals("com.carlos.stellarControl.activities.MainGeneral")){
            Global.imgBack.setVisibility(View.GONE);
        }*/

        Toast.makeText(MainGeneral.this, "Cargando Planeta seleccionado: "+Global.planetaSeleccionado, Toast.LENGTH_SHORT).show();

        Global.mensajes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainGeneral.this, MainMensajes.class);
                startActivity(intent);
            }
        });

        Global.settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.desplegarOpciones(MainGeneral.this);
            }
        });

        Global.listPlanetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.desplegarPlanetas(MainGeneral.this, new AlertDialog.Builder(MainGeneral.this));
                //global.mostrarPlanetaSeleccionado(global.mFirestore.collection("Planetas").document(global.mAuth.getCurrentUser().getUid()).collection("Planetas_Jugador").whereEqualTo("nombre",global.planetaSeleccionado));
            }
        });

        btnVision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainGeneral.this, MainPlaneta.class);
                intent.putExtra("NombrePlaneta", Global.planetaSeleccionado);
                startActivity(intent);
            }
        });

        btnRecursos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainGeneral.this, MainConstruccion.class);
                intent.putExtra("Construcción", "Recursos");
                startActivity(intent);
            }
        });

        btnInstalaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainGeneral.this, MainConstruccion.class);
                intent.putExtra("Construcción", "Instalaciones");
                startActivity(intent);
            }
        });

        btnInvestigaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainGeneral.this, MainConstruccion.class);
                intent.putExtra("Construcción", "Investigaciones");
                startActivity(intent);
            }
        });

        btnHangar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainGeneral.this, MainConstruccion.class);
                intent.putExtra("Construcción", "Naves");
                startActivity(intent);
            }
        });

        btnDefensas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainGeneral.this, MainConstruccion.class);
                intent.putExtra("Construcción", "Defensas");
                startActivity(intent);
            }
        });

        btnFlota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainGeneral.this, MainFlota.class);
                startActivity(intent);
            }
        });

        btnGalaxia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainGeneral.this, MainGalaxia.class);
                intent.putExtra("anteriorActividad", "general");
                startActivity(intent);
            }
        });

        btnAlianza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainGeneral.this, MainAlianza.class);
                startActivity(intent);
            }
        });

        Global.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.volverGeneral(MainGeneral.this);
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();

        /*Global.docRef = Global.fFirestore.collection("Recursos_Jugador").document(Global.idPlanetaSeleccionado).collection("Recursos_Planeta").document("Mina de metal");
        Global.docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        Log.d("Nivel de metal", String.valueOf(document.getLong("cantidad").intValue()));

                    } else {
                        Log.d("Check", "No such document");
                    }
                }
            }
        });*/

        Log.d("Metal disponible", Global.tvMetal.getText().toString());
        Log.d("Capacidad Metal", Global.capacidadMetal.toString());

        if (Integer.parseInt(String.valueOf(Global.tvMetal.getText())) < Global.cantidadMetal){
            Log.d("Metal", "Espacio disponible");
        }
        else{
            Log.d("Metal", "Lleno");
        }

        Metal metal = new Metal();
        metal.start();

        Cristal cristal = new Cristal();
        cristal.start();

        Deuterio deuterio = new Deuterio();
        deuterio.start();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    class Metal extends Thread {
        @Override
        public void run() {
            while(Global.cantidadMetal <= Global.capacidadMetal){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Global.producirRecursos("Mina de metal", "metal");
                    }
                });
                try{
                    Thread.sleep(1000);
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }

    class Cristal extends Thread {
        @Override
        public void run() {
            while(Global.cantidadCristal <= Global.capacidadCristal){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Global.producirRecursos("Mina de metal", "cristal");
                    }
                });
                try{
                    Thread.sleep(2000);
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }

    class Deuterio extends Thread {
        @Override
        public void run() {
            while(Global.cantidadDeuterio <= Global.capacidadDeuterio){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Global.producirRecursos("Mina de metal", "deuterio");
                    }
                });
                try{
                    Thread.sleep(3000);
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
}