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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.carlos.stellarControl.R;
import com.carlos.stellarControl.adapter.AdapterGalaxia;
import com.carlos.stellarControl.model.Planeta;
import com.carlos.stellarControl.utils.Global;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainGalaxia extends AppCompatActivity {
    private RecyclerView rvGalaxia;
    private AdapterGalaxia adaptadorGalaxia;
    Intent intent;
    String anteriorActividad;
    Query query;
    EditText etSistema;
    ImageView imgAnteriorSistema, imgSiguienteSistema;
    Button btnViajar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galaxy);

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

        etSistema = (EditText)findViewById(R.id.etSistema);
        imgAnteriorSistema = (ImageView)findViewById(R.id.imgAnteriorSistema);
        imgSiguienteSistema = (ImageView)findViewById(R.id.imgSiguienteSistema);

        btnViajar = (Button)findViewById(R.id.btnViajar);

        if (anteriorActividad.equals("general")){
            etSistema.setText(""+Global.sistemaSeleccionado);
        }
        if (anteriorActividad.equals("galaxia")){
            etSistema.setText(""+extras.getInt("sistema"));
        }

        rvGalaxia = findViewById(R.id.rvGalaxia);
        rvGalaxia.setLayoutManager(new LinearLayoutManager(this));

        query = Global.fFirestore.collection("Sistemas").document(String.valueOf(Global.sistemaSeleccionado)).collection("Planetas_Sistemas").orderBy("posicion");

        FirestoreRecyclerOptions<Planeta> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Planeta>().setQuery(query, Planeta.class).build();

        adaptadorGalaxia = new AdapterGalaxia(firestoreRecyclerOptions);
        adaptadorGalaxia.notifyDataSetChanged();
        rvGalaxia.setAdapter(adaptadorGalaxia);

        Global.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.volverGeneral(MainGalaxia.this);
            }
        });

        Global.mensajes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainGalaxia.this, MainMensajes.class);
                startActivity(intent);
            }
        });

        Global.settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.desplegarOpciones(MainGalaxia.this);
            }
        });

        Global.listPlanetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.desplegarPlanetas(MainGalaxia.this, new AlertDialog.Builder(MainGalaxia.this));
            }
        });

        imgAnteriorSistema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Integer anteriorSistema = Integer.parseInt(etSistema.getText().toString()) -1;
               Global.sistemaSeleccionado = anteriorSistema;
               //Toast.makeText(MainGalaxia.this, anteriorSistema, Toast.LENGTH_SHORT).show();
               intent = getIntent();
               intent.putExtra("anteriorActividad", "galaxia");
               intent.putExtra("sistema", anteriorSistema);
               finish();
               startActivity(intent);
            }
        });

        imgSiguienteSistema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer siguienteSistema = Integer.parseInt(etSistema.getText().toString()) +1;
                Global.sistemaSeleccionado = siguienteSistema;
                intent = getIntent();
                intent.putExtra("anteriorActividad", "galaxia");
                intent.putExtra("sistema", siguienteSistema);
                finish();
                startActivity(intent);
            }
        });

        btnViajar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer siguienteSistema = Integer.parseInt(etSistema.getText().toString());
                Global.sistemaSeleccionado = siguienteSistema;
                intent = getIntent();
                intent.putExtra("anteriorActividad", "galaxia");
                intent.putExtra("sistema", siguienteSistema);
                finish();
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        adaptadorGalaxia.startListening();

        //Mostrar el planeta seleccionado en el selecctor de planetas
        Global.mostrarPlanetaSeleccionado(Global.fFirestore.collection("Planetas").document(Global.fAuth.getCurrentUser().getUid()).collection("Planetas_Jugador").whereEqualTo("nombre",Global.planetaSeleccionado));


    }

    @Override
    protected void onStop(){
        super.onStop();
        adaptadorGalaxia.stopListening();
    }
}