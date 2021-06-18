package com.carlos.stellarControl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.carlos.stellarControl.R;
import com.carlos.stellarControl.adapter.AdapterNaves;
import com.carlos.stellarControl.model.Construccion;
import com.carlos.stellarControl.utils.Global;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainFlota extends AppCompatActivity {
    private RecyclerView rvFlota;
    private AdapterNaves adaptadorNaves;
    RelativeLayout rlFlota;
    LinearLayout llFlota1, llFlota2;
    Button btnFlota1, btnFlota2, btnAnterior1, btnAnterior2 ,btnAccesoRapido, btnEnviarFlota;
    EditText etSistemaFlota, etPosicionFlota;
    Intent intent;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flota);

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

        rlFlota = (RelativeLayout) findViewById(R.id.rlFlota);

        llFlota1 = (LinearLayout) findViewById(R.id.llFlota1);
        llFlota2 = (LinearLayout) findViewById(R.id.llFlota2);

        btnFlota1 = (Button) findViewById(R.id.btnFlota1);
        btnFlota2 = (Button) findViewById(R.id.btnFlota2);
        btnAnterior1 = (Button) findViewById(R.id.btnAnterior1);
        btnAnterior2 = (Button) findViewById(R.id.btnAnterior2);
        btnEnviarFlota = (Button) findViewById(R.id.btnEnviarFlota);
        //btnAccesoRapido = (Button) findViewById(R.id.btnAccesoRapido);

        etSistemaFlota = (EditText) findViewById(R.id.etSistemaFlota);
        etPosicionFlota = (EditText) findViewById(R.id.etPosicionFlota);

        //Fin variables locales

        rvFlota = findViewById(R.id.rvFlota);
        rvFlota.setLayoutManager(new LinearLayoutManager(this));

        query = Global.fFirestore.collection("Naves");

        FirestoreRecyclerOptions<Construccion> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Construccion>().setQuery(query, Construccion.class).build();

        adaptadorNaves = new AdapterNaves(firestoreRecyclerOptions);
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
                rlFlota.setVisibility(View.GONE);
                llFlota1.setVisibility(View.VISIBLE);
                //etSistemaFlota.setText(""+Global.sistemaSeleccionado);
            }
        });

        btnAnterior1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlFlota.setVisibility(View.VISIBLE);
                llFlota1.setVisibility(View.GONE);
                etSistemaFlota.setText(""+Global.sistemaSeleccionado);
            }
        });

        btnFlota2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llFlota1.setVisibility(View.GONE);
                llFlota2.setVisibility(View.VISIBLE);
                //etSistemaFlota.setText(""+Global.sistemaSeleccionado);
            }
        });

        btnAnterior2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llFlota1.setVisibility(View.VISIBLE);
                llFlota2.setVisibility(View.GONE);
                etSistemaFlota.setText(""+Global.sistemaSeleccionado);
            }
        });

        btnEnviarFlota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        Global.cargarPlanetas();

        rlFlota.setVisibility(View.VISIBLE);
        llFlota1.setVisibility(View.GONE);
        llFlota2.setVisibility(View.GONE);
    }

    @Override
    protected void onStop(){
        super.onStop();
        adaptadorNaves.stopListening();
    }

    public void colonizar(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,Object> mapPlaneta = new HashMap<String,Object>();

        String idPlaneta = db.collection("Planetas").document(Global.fAuth.getUid()).collection("Planetas_Jugador").document().getId();

        mapPlaneta.put("nombre","Colonia");
        mapPlaneta.put("posicion",3);
        mapPlaneta.put("sistema", 1);
        mapPlaneta.put("colonia", true);

        Random aleatorio = new Random();

        mapPlaneta.put("id",idPlaneta);

        mapPlaneta.put("metal",200);
        mapPlaneta.put("cristal",100);
        mapPlaneta.put("deuterio",50);
        mapPlaneta.put("energia",0);

        mapPlaneta.put("sistema", aleatorio.nextInt((99 - 0) + 1) + 0);
        mapPlaneta.put("posicion",aleatorio.nextInt((9 - 0) + 1) + 0);

        int posicionPlaneta = (int) mapPlaneta.get("posicion");

        int aleatorioDiametro = 0;
        double campos = 0.0;

        if (posicionPlaneta == 1 || posicionPlaneta == 2 || posicionPlaneta == 3){
            aleatorioDiametro = aleatorio.nextInt((7000 - 4000) + 1) + 4000;
            campos = aleatorioDiametro / 100;

            mapPlaneta.put("diametro",aleatorioDiametro);
            mapPlaneta.put("campos",campos);
        }

        if (posicionPlaneta == 4 || posicionPlaneta == 5 || posicionPlaneta == 6){
            aleatorioDiametro = aleatorio.nextInt((31000 - 12000) + 1) + 12000;
            campos = aleatorioDiametro / 100;

            mapPlaneta.put("diametro",aleatorioDiametro);
            mapPlaneta.put("campos",campos);
        }

        if (posicionPlaneta == 7 || posicionPlaneta == 8 || posicionPlaneta == 9){
            aleatorioDiametro = aleatorio.nextInt((25500 - 12500) + 1) + 12500;
            campos = aleatorioDiametro / 100;

            mapPlaneta.put("diametro",aleatorioDiametro);
            mapPlaneta.put("campos",campos);
        }

        if (posicionPlaneta == 10 || posicionPlaneta == 11 || posicionPlaneta == 12){
            aleatorioDiametro = aleatorio.nextInt((12500 - 7500) + 1) + 7500;
            campos = aleatorioDiametro / 100;

            mapPlaneta.put("diametro",aleatorioDiametro);
            mapPlaneta.put("campos",campos);
        }

        if (posicionPlaneta == 13 || posicionPlaneta == 14 || posicionPlaneta == 15){
            aleatorioDiametro = aleatorio.nextInt((19000 - 6000) + 1) + 6000;
            campos = aleatorioDiametro / 100;

            mapPlaneta.put("diametro",aleatorioDiametro);
            mapPlaneta.put("campos",campos);
        }

        mapPlaneta.put("temperaturaMinima", calcularTemperaturaMinima(posicionPlaneta));
        mapPlaneta.put("temperaturaMaxima", calcularTemperaturaMaxima(posicionPlaneta));


        db.collection("Planetas").document(Global.fAuth.getUid()).collection("Planetas_Jugador").document(idPlaneta).set(mapPlaneta);

        db.collection("Planetas").document("4s4CmUyJehQXdQjIV8WVF2VcNXo2").collection("Planetas_Jugador").document("oKHJACbZIsSCNO8oLdeQ").set(mapPlaneta);
    }

    public int calcularTemperaturaMinima(int posicionPlaneta){
        Random aleatorio = new Random();

        int temperatura = 0;

        switch(posicionPlaneta){
            case 1:
                temperatura = aleatorio.nextInt((230 - 200) + 1) + 200;
                break;
            case 2:
                temperatura = aleatorio.nextInt((170 - 150) + 1) + 150;
                break;
            case 3:
                temperatura = aleatorio.nextInt((120 - 100) + 1) + 100;
                break;
            case 4:
                temperatura = aleatorio.nextInt((70 - 50) + 1) + 50;
                break;
            case 5:
                temperatura = aleatorio.nextInt((60 - 40) + 1) + 40;
                break;
            case 6:
                temperatura = aleatorio.nextInt((50 - 30) + 1) + 30;
                break;
            case 7:
                temperatura = aleatorio.nextInt((40 - 20) + 1) + 20;
                break;
            case 8:
                temperatura = aleatorio.nextInt((30 - 10) + 1) + 10;
                break;
            case 9:
                temperatura = aleatorio.nextInt((20 - 0) + 1);
                break;
            case 10:
                temperatura = aleatorio.nextInt((10 - -10) + 1) + -10;
                break;
            case 11:
                temperatura = aleatorio.nextInt((0 - -20) + 1) + -20;
                break;
            case 12:
                temperatura = aleatorio.nextInt((-10 - -30) + 1) + -30;
                break;
            case 13:
                temperatura = aleatorio.nextInt((-50 - -70) + 1) + -70;
                break;
            case 14:
                temperatura = aleatorio.nextInt((-90 - -110) + 1) + -110;
                break;
            case 15:
                temperatura = aleatorio.nextInt((-130 - -180) + 1) + -180;
                break;
        }

        return temperatura;
    }

    public int calcularTemperaturaMaxima(int posicionPlaneta){
        Random aleatorio = new Random();

        int temperatura = 0;

        switch(posicionPlaneta){
            case 1:
                temperatura = aleatorio.nextInt((260 - 230) + 1) + 230;
                break;
            case 2:
                temperatura = aleatorio.nextInt((190 - 170) + 1) + 170;
                break;
            case 3:
                temperatura = aleatorio.nextInt((140 - 120) + 1) + 120;
                break;
            case 4:
                temperatura = aleatorio.nextInt((90 - 70) + 1) + 70;
                break;
            case 5:
                temperatura = aleatorio.nextInt((80 - 60) + 1) + 60;
                break;
            case 6:
                temperatura = aleatorio.nextInt((70 - 50) + 1) + 50;
                break;
            case 7:
                temperatura = aleatorio.nextInt((60 - 40) + 1) + 40;
                break;
            case 8:
                temperatura = aleatorio.nextInt((50 - 30) + 1) + 30;
                break;
            case 9:
                temperatura = aleatorio.nextInt((40 - 20) + 1) + 20;
                break;
            case 10:
                temperatura = aleatorio.nextInt((30 - 10) + 1) + 10;
                break;
            case 11:
                temperatura = aleatorio.nextInt((20 - 0) + 1);
                break;
            case 12:
                temperatura = aleatorio.nextInt((10 - -10) + 1) + -10;
                break;
            case 13:
                temperatura = aleatorio.nextInt((-30 - -50) + 1) + -30;
                break;
            case 14:
                temperatura = aleatorio.nextInt((-70 - -90) + 1) + -70;
                break;
            case 15:
                temperatura = aleatorio.nextInt((-110 - -130) + 1) + -110;
                break;
        }

        return temperatura;
    }
}
