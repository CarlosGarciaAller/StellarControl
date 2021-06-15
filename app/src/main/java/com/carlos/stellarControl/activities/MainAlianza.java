package com.carlos.stellarControl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.carlos.stellarControl.R;
import com.carlos.stellarControl.utils.Global;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainAlianza extends AppCompatActivity {
    TableLayout tlCrearAlianza, tlGestionAlianza;
    Button btnCrearAlianza, btnDisolverAlianza;
    EditText etNombreAlianza, etEtiquetaAlianza;
    TextView tvNombreAlianzaCreada;
    String idAlianza;
    Intent intent;
    DocumentReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alianza);

        // Variables extras

        Bundle extras = getIntent().getExtras();


        //Fin variables extras

        //Variables globales

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
        Global.tvPlaneta = (TextView) findViewById(R.id.tvPlaneta);
        Global.tvCoordenadas = (TextView) findViewById(R.id.tvCoordenadas);

        //Fin variables globales

        //variables locales

        tlCrearAlianza = (TableLayout) findViewById(R.id.tlCrearAlianza);
        tlGestionAlianza = (TableLayout) findViewById(R.id.tlGestionAlianza);
        btnCrearAlianza = (Button) findViewById(R.id.btnCrearAlianza);
        btnDisolverAlianza = (Button) findViewById(R.id.btnDisolverAlianza);
        etNombreAlianza = (EditText) findViewById(R.id.etNombreAlianza);
        etEtiquetaAlianza = (EditText) findViewById(R.id.etEtiquetaAlianza);
        tvNombreAlianzaCreada = (TextView) findViewById(R.id.tvNombreAlianzaCreada);

        //Fin variables locales

        Global.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.volverGeneral(MainAlianza.this);
            }
        });

        Global.mensajes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainAlianza.this, MainMensajes.class);
                startActivity(intent);
            }
        });

        Global.settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.desplegarOpciones(MainAlianza.this);
            }
        });

        Global.listPlanetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cargar los planetas en el selector
                Global.cargarPlanetas();
                Global.desplegarPlanetas(MainAlianza.this, new AlertDialog.Builder(MainAlianza.this));
            }
        });

        btnCrearAlianza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map mapAlianza = new HashMap<String,Object>();
                String[] miembros = {Global.fAuth.getCurrentUser().getUid()};
                idAlianza = Global.fFirestore.collection("Alianza").document().getId();
                mapAlianza.put("fundador",Global.fAuth.getCurrentUser().getUid());
                mapAlianza.put("nombre",etNombreAlianza.getText().toString());
                mapAlianza.put("etiqueta",etEtiquetaAlianza.getText().toString());
                mapAlianza.put("miembros", Arrays.asList(miembros));
                Global.fFirestore.collection("Alianzas").document(idAlianza).set(mapAlianza);
                Global.fFirestore.collection("Usuarios").document(Global.fAuth.getCurrentUser().getUid()).update("alianza",idAlianza);
                intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        btnDisolverAlianza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();

        docRef = Global.fFirestore.collection("Usuarios").document(Global.fAuth.getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        if (document.getString("alianza").isEmpty()){
                            tlCrearAlianza.setVisibility(View.VISIBLE);
                            tlGestionAlianza.setVisibility(View.GONE);
                        }
                        else{
                            tlCrearAlianza.setVisibility(View.GONE);
                            tlGestionAlianza.setVisibility(View.VISIBLE);
                            cargarAlianza(document.getString("alianza"));
                        }
                    } else {
                        Log.d("Check", "No such document");
                    }
                }
            }
        });

        docRef = Global.fFirestore.collection("Usuarios").document(Global.fAuth.getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        tvNombreAlianzaCreada.setText(document.getString("nombre"));
                    } else {
                        Log.d("Check", "No such document");
                    }
                }
            }
        });

        //Mostrar el planeta seleccionado en el selecctor de planetas

        Global.mostrarPlanetaSeleccionado(Global.fFirestore.collection("Planetas").document(Global.fAuth.getCurrentUser().getUid()).collection("Planetas_Jugador").whereEqualTo("nombre",Global.planetaSeleccionado));

        //Cargar los recursos del planeta seleccionado

        Global.cargarRecursos(Global.planetaSeleccionado);

        //Mostrar datos del planeta

    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    public void cargarAlianza(String alianza){
        docRef = Global.fFirestore.collection("Alianzas").document(alianza);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        tvNombreAlianzaCreada.setText(document.getString("nombre"));
                    } else {
                        Log.d("Check", "No such document");
                    }
                }
            }
        });
    }
}