package com.carlos.stellarControl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
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
    Metal metal = new Metal();
    Cristal cristal = new Cristal();
    Deuterio deuterio = new Deuterio();
    //Recursos recursos = new Recursos();
    private boolean isRunning = false;

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

        Global.tvMetal = (TextView) findViewById(R.id.tvMetal);
        Global.tvCristal = (TextView) findViewById(R.id.tvCristal);
        Global.tvDeuterio = (TextView) findViewById(R.id.tvDeuterio);
        Global.tvPlaneta = (TextView) findViewById(R.id.tvPlaneta);
        Global.tvCoordenadas = (TextView) findViewById(R.id.tvCoordenadas);
        Global.listPlanetas = (LinearLayout) findViewById(R.id.listPlanetas);

        //Fin variables globales

        //variables locales

        btnVision = (Button) findViewById(R.id.btnVision);
        btnRecursos = (Button) findViewById(R.id.btnRecursos);
        btnInstalaciones = (Button) findViewById(R.id.btnInstalaciones);
        btnInvestigaciones = (Button) findViewById(R.id.btnInvestigaciones);
        btnHangar = (Button) findViewById(R.id.btnHangar);
        btnDefensas = (Button) findViewById(R.id.btnDefensas);
        btnFlota = (Button) findViewById(R.id.btnFlota);
        btnGalaxia = (Button) findViewById(R.id.btnGalaxia);
        btnAlianza = (Button) findViewById(R.id.btnAlianza);

        //Fin variables locales

        /*if (getClass().getName().equals("com.carlos.stellarControl.activities.MainGeneral")){
            Global.imgBack.setVisibility(View.GONE);
        }*/

        Toast.makeText(MainGeneral.this, "Cargando Planeta seleccionado: "+Global.planetaSeleccionado, Toast.LENGTH_SHORT).show();

        /*metal.start();

        cristal.start();

        deuterio.start();*/

        isRunning = true;

        Global.mensajes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interrumpirProduccion();
                intent = new Intent(MainGeneral.this, MainMensajes.class);
                startActivity(intent);
            }
        });

        Global.settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interrumpirProduccion();
                Global.desplegarOpciones(MainGeneral.this);
            }
        });

        Global.listPlanetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interrumpirProduccion();
                Global.desplegarPlanetas(MainGeneral.this, new AlertDialog.Builder(MainGeneral.this));
            }
        });

        btnVision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interrumpirProduccion();
                intent = new Intent(MainGeneral.this, MainPlaneta.class);
                intent.putExtra("NombrePlaneta", Global.planetaSeleccionado);
                startActivity(intent);
            }
        });

        btnRecursos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interrumpirProduccion();
                intent = new Intent(MainGeneral.this, MainConstruccion.class);
                intent.putExtra("Construcción", "Recursos");
                startActivity(intent);
            }
        });

        btnInstalaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interrumpirProduccion();
                intent = new Intent(MainGeneral.this, MainConstruccion.class);
                intent.putExtra("Construcción", "Instalaciones");
                startActivity(intent);
            }
        });

        btnInvestigaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interrumpirProduccion();
                intent = new Intent(MainGeneral.this, MainConstruccion.class);
                intent.putExtra("Construcción", "Investigaciones");
                startActivity(intent);
            }
        });

        btnHangar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interrumpirProduccion();
                intent = new Intent(MainGeneral.this, MainConstruccion.class);
                intent.putExtra("Construcción", "Naves");
                startActivity(intent);
            }
        });

        btnDefensas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interrumpirProduccion();
                intent = new Intent(MainGeneral.this, MainConstruccion.class);
                intent.putExtra("Construcción", "Defensas");
                startActivity(intent);
            }
        });

        btnFlota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interrumpirProduccion();
                intent = new Intent(MainGeneral.this, MainFlota.class);
                startActivity(intent);
            }
        });

        btnGalaxia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interrumpirProduccion();
                intent = new Intent(MainGeneral.this, MainGalaxia.class);
                intent.putExtra("anteriorActividad", "general");
                startActivity(intent);
            }
        });

        btnAlianza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interrumpirProduccion();
                intent = new Intent(MainGeneral.this, MainAlianza.class);
                startActivity(intent);
            }
        });

        Global.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interrumpirProduccion();
                Global.volverGeneral(MainGeneral.this);
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();

        //Cargar los recursos del planeta seleccionado
        if (anteriorActividad.equals("login") || anteriorActividad.equals("registrar") || anteriorActividad.equals("abandonar")) {
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


    }

    @Override
    protected void onStop(){
        super.onStop();
        interrumpirProduccion();
    }

    @Override
    public void onPause() {
        super.onPause();
        interrumpirProduccion();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        interrumpirProduccion();
    }

    public void interrumpirProduccion(){
        metal.interrupt();
        cristal.interrupt();
        deuterio.interrupt();
    }

    class Metal extends Thread {
        @Override
        public void run() {
            if (!isRunning) {
                while (Global.cantidadMetal <= Global.capacidadMetal) {
                    SystemClock.sleep(2000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Global.producirRecursos("Mina de metal", "metal");
                            Global.producirRecursos("Mina de cristal", "cristal");
                            Global.producirRecursos("Sintetizador de deuterio", "deuterio");
                        }
                    });
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    class Cristal extends Thread {
        @Override
        public void run() {
            if (!isRunning) {
                while (Global.cantidadCristal <= Global.capacidadCristal) {
                    SystemClock.sleep(4000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Global.producirRecursos("Mina de metal", "cristal");
                        }
                    });
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    class Deuterio extends Thread {
        @Override
        public void run() {
            if (!isRunning) {
                while (Global.cantidadDeuterio <= Global.capacidadDeuterio) {
                    SystemClock.sleep(6000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Global.producirRecursos("Mina de metal", "deuterio");
                        }
                    });
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}