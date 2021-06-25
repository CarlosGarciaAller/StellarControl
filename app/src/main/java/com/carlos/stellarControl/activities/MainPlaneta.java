package com.carlos.stellarControl.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import static android.view.View.VISIBLE;

public class MainPlaneta extends AppCompatActivity {
    TextView tvNamePlanet, tvDiametro , tvCampos, tvPosicion, tvTemperatura;
    ImageView imgVisionPlaneta;
    Button btnRenombrar, btnAbandonar, btnCambiar;
    EditText etRenombrar;
    TableRow trCambiarNombre;
    Intent intent;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planetas);

        //Variables globales

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
        Global.tvPlaneta = (TextView) findViewById(R.id.tvPlaneta);
        Global.tvCoordenadas = (TextView) findViewById(R.id.tvCoordenadas);
        Global.listPlanetas = (LinearLayout) findViewById(R.id.listPlanetas);

        //Fin variables globales

        //variables locales

        tvNamePlanet = (TextView) findViewById(R.id.tvVisionPlaneta);
        tvDiametro = (TextView) findViewById(R.id.tvVisionDiametro);
        tvCampos = (TextView) findViewById(R.id.tvVisionCampos);
        tvPosicion = (TextView) findViewById(R.id.tvVisionPosicion);
        tvTemperatura = (TextView) findViewById(R.id.tvVisionTemperatura);
        btnRenombrar = (Button) findViewById(R.id.btnRenombrar);
        btnAbandonar = (Button) findViewById(R.id.btnAbandonar);
        btnCambiar = (Button) findViewById(R.id.btnCambiar);
        trCambiarNombre = (TableRow) findViewById(R.id.trCambiarNombre);
        etRenombrar = (EditText) findViewById(R.id.etRenombrarPlaneta);
        imgVisionPlaneta = (ImageView) findViewById(R.id.imgVisionPlaneta);

        //Fin variables locales

        //Toast.makeText(MainPlaneta.this, Global.idPlanetaSeleccionado, Toast.LENGTH_SHORT).show();

        /*metal.start();

        cristal.start();

        deuterio.start();*/

        Global.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.volverGeneral(MainPlaneta.this);
            }
        });

        Global.mensajes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainPlaneta.this, MainMensajes.class);
                intent.putExtra("categoria", "Consulta");
                startActivity(intent);
            }
        });

        Global.settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.desplegarOpciones(MainPlaneta.this);
            }
        });

        Global.listPlanetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.desplegarPlanetas(MainPlaneta.this, new AlertDialog.Builder(MainPlaneta.this));
                //global.mostrarPlanetaSeleccionado(global.mFirestore.collection("Planetas").document(global.mAuth.getCurrentUser().getUid()).collection("Planetas_Jugador").whereEqualTo("nombre",global.planetaSeleccionado));
            }
        });

        btnRenombrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trCambiarNombre.setVisibility(VISIBLE);
            }
        });

        btnCambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.planetaSeleccionado = etRenombrar.getText().toString();
                Global.fFirestore.collection("Planetas").document(Global.fAuth.getCurrentUser().getUid()).collection("Planetas_Jugador").document(Global.idPlanetaSeleccionado).update("nombre", etRenombrar.getText().toString());
                Global.fFirestore.collection("Sistemas").document(String.valueOf(Global.sistemaSeleccionado)).collection("Planetas_Sistemas").document(String.valueOf(Global.posicionSeleccionado)).update("nombrePlaneta", etRenombrar.getText().toString());
                intent = new Intent(MainPlaneta.this, MainPlaneta.class);
                Toast.makeText(MainPlaneta.this, "Planeta renombrado", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        btnAbandonar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainPlaneta.this);

                mBuilder.setTitle("¿Está seguro de querer abandonar "+Global.planetaSeleccionado+" ?");
                mBuilder.setMessage("Se perderan para siempre todos los recursos, construcciones, naves y defensas que tenga en ese planeta");
                mBuilder.setIcon(R.drawable.icon);
                mBuilder.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Global.abandonarPlaneta(Global.idPlanetaSeleccionado, Global.sistemaSeleccionado, Global.posicionSeleccionado);
                        intent = new Intent(MainPlaneta.this, MainGeneral.class);
                        intent.putExtra("anteriorActividad", "abandonar");
                        Toast.makeText(MainPlaneta.this, "Planeta abandonado", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                });
                mBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();

        //Mostrar el planeta seleccionado en el selecctor de planetas
        Global.mostrarPlanetaSeleccionado(Global.fFirestore.collection("Planetas").document(Global.fAuth.getCurrentUser().getUid()).collection("Planetas_Jugador").whereEqualTo("nombre",Global.planetaSeleccionado));

        //Cargar los planetas en el selector
        Global.cargarSelectPlanetas();

        //trCambiarNombre.setVisibility(View.Gone);

        DocumentReference docImagen = Global.fFirestore.collection("Sistemas").document(String.valueOf(Global.sistemaSeleccionado)).collection("Planetas_Sistemas").document(String.valueOf(Global.posicionSeleccionado));
        docImagen.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        Picasso.get().load(document.getString("imagen")).into(imgVisionPlaneta);
                    } else {
                        Log.d("Check", "No such document");
                    }
                }
            }
        });

        DocumentReference docRef = Global.fFirestore.collection("Instalaciones_Jugador").document(Global.idPlanetaSeleccionado).collection("Instalaciones_Planeta").document("Fabrica de nanobots");

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {

                    } else {
                        Log.d("Check", "No such document");
                    }
                }
            }
        });

        //Mostrar datos del planeta

        query = Global.fFirestore.collection("Planetas").document(Global.fAuth.getCurrentUser().getUid()).collection("Planetas_Jugador").whereEqualTo("nombre",Global.planetaSeleccionado);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        tvNamePlanet.setText(document.getString("nombre"));
                        tvDiametro.setText(String.valueOf(document.getLong("diametro").intValue())+" Km");
                        tvCampos.setText(String.valueOf("("+document.getLong("camposUsados").intValue())+"/"+String.valueOf(document.getLong("camposTotales").intValue())+")");
                        tvPosicion.setText(String.valueOf("["+document.getLong("sistema").intValue())+":"+String.valueOf(document.getLong("posicion").intValue())+"]");
                        tvTemperatura.setText("De "+String.valueOf(document.getLong("temperaturaMinima").intValue())+" Cº a "+String.valueOf(document.getLong("temperaturaMaxima").intValue())+" Cº");
                        if (!document.getBoolean("colonia")){
                            btnAbandonar.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onStop(){
        super.onStop();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy(){
        super.onDestroy();

    }
}