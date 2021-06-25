package com.carlos.stellarControl.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
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
    Metal metal = new Metal();
    Cristal cristal = new Cristal();
    Deuterio deuterio = new Deuterio();
    boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);

        //Variables extras

        Bundle extras = getIntent().getExtras();
        anteriorActividad = extras.getString("anteriorActividad");

        // Fin variables extras

        //Variables globales

        Global.fAuth = FirebaseAuth.getInstance();
        Global.fFirestore = FirebaseFirestore.getInstance();

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

        //recursos.start();

        Global.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.volverGeneral(MainGeneral.this);
            }
        });

        Global.mensajes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainGeneral.this, MainMensajes.class);
                intent.putExtra("categoria", "Consulta");
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
                Global.comprobarConstruccion("Laboratorio de investigacion", MainGeneral.this);
            }
        });

        btnHangar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.comprobarConstruccion("Hangar", MainGeneral.this);
            }
        });

        btnDefensas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.comprobarConstruccion("Hangar", MainGeneral.this);
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
    }

    @Override
    protected void onStart(){
        super.onStart();

        //Mostrar el planeta seleccionado en el selecctor de planetas
        Global.mostrarPlanetaSeleccionado(Global.fFirestore.collection("Planetas").document(Global.fAuth.getCurrentUser().getUid()).collection("Planetas_Jugador").whereEqualTo("nombre",Global.planetaSeleccionado));

        Global.metalRunning = Global.espacioSuficiente(Global.tvMetal, Global.capacidadMetal);
        Global.cristalRunning = Global.espacioSuficiente(Global.tvCristal, Global.capacidadCristal);
        Global.deuterioRunning = Global.espacioSuficiente(Global.tvDeuterio, Global.capacidadDeuterio);

        //Cargar los planetas del jugador en el selector
        Global.cargarSelectPlanetas();

        if(!isRunning){
            metal.start();
            cristal.start();
            deuterio.start();
            isRunning = true;
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        //interrumpirProduccion();
        Log.e("Check", "Stop: Actualizando a base de datos");
    }

    @Override
    public void onPause() {
        //interrumpirProduccion();
        super.onPause();
        Log.e("Check", "Pause: Actualizando a base de datos");

    }

    @Override
    public void onRestart() {
        super.onRestart();
        Log.e("Check", "Reanudando");
    }

    @Override
    public void onDestroy(){
        interrumpirProduccion();
        super.onDestroy();
        Log.e("Check", "Destroy: Actualizando a base de datos");
    }

    public void interrumpirProduccion(){
        //recursos.interrupt();
        metal.interrupt();
        cristal.interrupt();
        deuterio.interrupt();
    }

    class Metal extends Thread {
        @Override
        public void run() {
            while (Global.metalRunning) {
                    SystemClock.sleep(6000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Global.producirRecursos("metal");
                            if (!Global.espacioSuficiente(Global.tvMetal, Global.capacidadMetal)) {
                                Global.metalRunning = false;
                                Global.tvMetal.setText(""+Global.capacidadMetal);
                                Global.tvMetal.setTextColor(Color.parseColor("#FF0000"));
                            }
                        }
                    });
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
        }
    }}

    class Cristal extends Thread {
        @Override
        public void run() {
                while (Global.cristalRunning) {
                    SystemClock.sleep(4000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Global.producirRecursos("cristal");
                            if (!Global.espacioSuficiente(Global.tvCristal, Global.capacidadCristal)) {
                                Global.cristalRunning = false;
                                Global.tvCristal.setText(""+Global.capacidadCristal);
                                Global.tvCristal.setTextColor(Color.parseColor("#FF0000"));
                            }
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

    class Deuterio extends Thread {
        @Override
        public void run() {
            while (Global.deuterioRunning) {
                SystemClock.sleep(2000);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Global.producirRecursos("deuterio");
                        if (!Global.espacioSuficiente(Global.tvDeuterio, Global.capacidadDeuterio)) {
                            Global.deuterioRunning = false;
                            Global.tvDeuterio.setText(""+Global.capacidadDeuterio);
                            Global.tvDeuterio.setTextColor(Color.parseColor("#FF0000"));
                        }
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