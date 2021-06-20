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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainFlota extends AppCompatActivity {
    private RecyclerView rvFlota;
    private AdapterNaves adaptadorNaves;
    RelativeLayout rlFlota;
    LinearLayout llFlota1, llFlota2;
    TextView tvPlanetaOrigen, tvCoordenadasOrigen, tvMision;
    Button btnFlota1, btnFlota2, btnAnterior1, btnAnterior2 ,btnAccesoRapido, btnEnviarFlota;
    EditText etSistemaFlota, etPosicionFlota, etMetal, etCristal, etDeuterio;
    ImageView imgColonizar, imgAtacar, imgTransportar, imgDesplegar;
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

        tvPlanetaOrigen = (TextView) findViewById(R.id.tvPlanetaOrigen);
        tvCoordenadasOrigen = (TextView) findViewById(R.id.tvCoordenadasOrigen);
        tvMision = (TextView) findViewById(R.id.tvMision);

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
                btnFlota1.setVisibility(View.GONE);
                //etSistemaFlota.setText(""+Global.sistemaSeleccionado);
                tvPlanetaOrigen.setText(Global.planetaSeleccionado);
                tvCoordenadasOrigen.setText("["+Global.sistemaSeleccionado+":"+Global.posicionSeleccionado+"]");
            }
        });

        btnAnterior1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlFlota.setVisibility(View.VISIBLE);
                llFlota1.setVisibility(View.GONE);
                btnFlota1.setVisibility(View.VISIBLE);
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
                switch (tvMision.getText().toString()){
                    case "Colonizar":
                        colonizar(Integer.parseInt(String.valueOf(etSistemaFlota.getText())),
                                Integer.parseInt(String.valueOf(etPosicionFlota.getText())));
                        break;
                    case "Atacar":
                        break;
                    case "Transportar":
                        break;
                    case "Desplegar":
                        break;

                }
            }
        });

        imgColonizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMision.setText("Colonizar");
            }
        });

        imgAtacar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMision.setText("Atacar");
            }
        });

        imgTransportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMision.setText("Transportar");
            }
        });

        imgDesplegar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMision.setText("Desplegar");
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

    public void colonizar(int sistema, int posicion){
        DocumentReference docPlaneta = Global.fFirestore.collection("Sistemas").document(String.valueOf(sistema)).collection("Planetas_Sistemas").document(String.valueOf(posicion));
        docPlaneta.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        if (document.getString("idPlanetaJugador").equals(" ")){
                            String idPlaneta = Global.fFirestore.collection("Planetas").document(Global.fAuth.getUid()).collection("Planetas_Jugador").document().getId();

                            Global.asignarPlaneta(Global.fAuth, Global.fFirestore, idPlaneta,"Colonia", Integer.parseInt(String.valueOf(etMetal.getText())),
                                    Integer.parseInt(String.valueOf(etCristal.getText())),
                                    Integer.parseInt(String.valueOf(etDeuterio.getText())),
                                    sistema, posicion, true);
                            Global.inicializarRecursosPlaneta(Global.fFirestore, idPlaneta);
                            Global.inicializarInstalacionesPlaneta(Global.fFirestore, idPlaneta);
                            Global.inicializarNavesPlaneta(Global.fFirestore, idPlaneta);
                            Global.inicializarDefensasPlaneta(Global.fFirestore, idPlaneta);

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

    public void atacar(){

    }

    public void transportar(){

    }

    public void desplegar(){

    }
}
