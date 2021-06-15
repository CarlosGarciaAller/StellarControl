package com.carlos.stellarControl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.carlos.stellarControl.R;
import com.carlos.stellarControl.utils.Global;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class MainFlota extends AppCompatActivity {
    LinearLayout llFlota1, llFlota2, llFlota3;
    ImageView imgCazadorLigero, imgCazadorPesado, imgCrucero, imgNaveBatalla, imgAcorazado, imgBombardero, imgDestructor, imgEstrella, imgPequeñaCarga, imgGrandeCarga, imgColonizador;
    Button btnFlota1, btnFlota2, btnAnterior1, btnAnterior2 ,btnAccesoRapido, btnEnviarFlota;
    EditText etCazadorLigero, etCazadorPesado, etCrucero, etAcorazado, etDestructor, etEstrella, etCargaPequeña, etCargaGrande, etColonizador, etSistemaFlota, etPosicionFlota;
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

        llFlota1 = (LinearLayout) findViewById(R.id.llFlota1);
        llFlota2 = (LinearLayout) findViewById(R.id.llFlota2);
        llFlota3 = (LinearLayout) findViewById(R.id.llFlota3);

        imgCazadorLigero = (ImageView) findViewById(R.id.imgCazadorLigero);
        imgCazadorPesado = (ImageView) findViewById(R.id.imgCazadorPesado);
        imgCrucero = (ImageView) findViewById(R.id.imgCrucero);
        imgNaveBatalla = (ImageView) findViewById(R.id.imgNaveBatalla);
        imgAcorazado = (ImageView) findViewById(R.id.imgAcorazado);
        imgBombardero = (ImageView) findViewById(R.id.imgBombardero);
        imgDestructor = (ImageView) findViewById(R.id.imgDestructor);
        imgEstrella = (ImageView) findViewById(R.id.imgEstrella);
        imgPequeñaCarga = (ImageView) findViewById(R.id.imgPequeñaCarga);
        imgGrandeCarga = (ImageView) findViewById(R.id.imgGrandeCarga);
        imgColonizador = (ImageView) findViewById(R.id.imgColonizador);

        btnFlota1 = (Button) findViewById(R.id.btnFlota1);
        btnFlota2 = (Button) findViewById(R.id.btnFlota2);
        btnAnterior1 = (Button) findViewById(R.id.btnAnterior1);
        btnAnterior2 = (Button) findViewById(R.id.btnAnterior2);
        btnEnviarFlota = (Button) findViewById(R.id.btnEnviarFlota);
        //btnAccesoRapido = (Button) findViewById(R.id.btnAccesoRapido);

        etCazadorLigero = (EditText) findViewById(R.id.etCazadorLigero);
        etCazadorPesado = (EditText) findViewById(R.id.etCazadorPesado);
        etCrucero = (EditText) findViewById(R.id.etCrucero);
        etAcorazado = (EditText) findViewById(R.id.etAcorazado);
        etDestructor = (EditText) findViewById(R.id.etDestructor);
        etEstrella = (EditText) findViewById(R.id.etEstrella);
        etCargaPequeña = (EditText) findViewById(R.id.etCargaPequeña);
        etCargaGrande = (EditText) findViewById(R.id.etCargaGrande);
        etColonizador = (EditText) findViewById(R.id.etColonizador);
        etSistemaFlota = (EditText) findViewById(R.id.etSistemaFlota);
        etPosicionFlota = (EditText) findViewById(R.id.etPosicionFlota);

        //Fin variables locales

        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/stellar-control.appspot.com/o/Naves%2FCazador_Ligero.jpg?alt=media&token=127a08d0-38ce-4b1c-97eb-7a54e7b6b986").into(imgCazadorLigero);
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/stellar-control.appspot.com/o/Naves%2FCazador_Pesado.jpg?alt=media&token=0c63f264-88f3-4cbc-9ece-716e8a7d1525").into(imgCazadorPesado);
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/stellar-control.appspot.com/o/Naves%2FCrucero.jpg?alt=media&token=22c78348-070f-4dd8-8acb-c7acf9627f58").into(imgCrucero);
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/stellar-control.appspot.com/o/Naves%2FNave_Batalla.jpg?alt=media&token=7bf1ca0a-73d0-43ef-a5d5-a602aec04d4f").into(imgNaveBatalla);
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/stellar-control.appspot.com/o/Naves%2FAcorazado.jpg?alt=media&token=9cafa3d8-9db4-4cf6-92e5-c64f5417581b").into(imgAcorazado);
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/stellar-control.appspot.com/o/Naves%2FBomber.jpg?alt=media&token=a5fa9ed2-3b39-439f-b7af-54ac3e1a2bb6").into(imgBombardero);
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/stellar-control.appspot.com/o/Naves%2FDestroyer.jpg?alt=media&token=544e2171-095f-476d-a784-5603a7d76b0c").into(imgDestructor);
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/stellar-control.appspot.com/o/Naves%2FEstrella.jpg?alt=media&token=dba0820a-2734-4638-9b94-b0766f2435fc").into(imgEstrella);
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/stellar-control.appspot.com/o/Naves%2FNave_peq.jpg?alt=media&token=ed14bea0-80d1-47de-8aa2-a969e7521b78").into(imgPequeñaCarga);
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/stellar-control.appspot.com/o/Naves%2FNave_gran.jpg?alt=media&token=c662da07-6c46-481c-bb85-2c636f07ea5f").into(imgGrandeCarga);
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/stellar-control.appspot.com/o/Naves%2FColonizador.jpg?alt=media&token=0f32fcd0-f2d5-4161-9690-6a5cc2a1263c").into(imgColonizador);

        //Mostrar el planeta seleccionado en el selecctor de planetas
        Global.mostrarPlanetaSeleccionado(Global.fFirestore.collection("Planetas").document(Global.fAuth.getCurrentUser().getUid()).collection("Planetas_Jugador").whereEqualTo("nombre",Global.planetaSeleccionado));

        //Cargar los planetas en el selector
        Global.cargarPlanetas();

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
                llFlota1.setVisibility(View.GONE);
                llFlota2.setVisibility(View.VISIBLE);
                //etSistemaFlota.setText(""+Global.sistemaSeleccionado);
            }
        });
        btnAnterior1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llFlota1.setVisibility(View.VISIBLE);
                llFlota2.setVisibility(View.GONE);
                etSistemaFlota.setText(""+Global.sistemaSeleccionado);
            }
        });
        btnFlota2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llFlota2.setVisibility(View.GONE);
                llFlota3.setVisibility(View.VISIBLE);
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
    }

    @Override
    protected void onStart(){
        super.onStart();
        llFlota1.setVisibility(View.VISIBLE);
        llFlota2.setVisibility(View.GONE);
        llFlota3.setVisibility(View.GONE);
    }

    @Override
    protected void onStop(){
        super.onStop();
    }
}
