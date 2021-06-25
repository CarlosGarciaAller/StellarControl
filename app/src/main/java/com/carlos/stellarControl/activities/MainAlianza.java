package com.carlos.stellarControl.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.carlos.stellarControl.R;
import com.carlos.stellarControl.adapter.AdapterMiembros;
import com.carlos.stellarControl.model.Miembro;
import com.carlos.stellarControl.utils.Global;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainAlianza extends AppCompatActivity {
    private RecyclerView rvMiembros;
    private AdapterMiembros adaptadorMiembro;
    TableRow trCrearAlianza, trAlianza, trMiembros;
    TableLayout tlCrearAlianza, tlGestionAlianza;
    RelativeLayout rlMiembros;
    Button btnCrearAlianza, btnDisolverAlianza;
    EditText etNombreAlianza, etEtiquetaAlianza;
    TextView tvNombreAlianzaCreada;
    String idAlianza, alianzaActual;
    Intent intent;
    DocumentReference docRef;
    Query query;

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
        Global.imgPlanetaSeleccionado = (ImageView) findViewById(R.id.imgPlanetaSeleccionado);

        Global.tvMetal = (TextView) findViewById(R.id.tvMetal);
        Global.tvCristal = (TextView) findViewById(R.id.tvCristal);
        Global.tvDeuterio = (TextView) findViewById(R.id.tvDeuterio);
        Global.tvPlaneta = (TextView) findViewById(R.id.tvPlaneta);
        Global.tvCoordenadas = (TextView) findViewById(R.id.tvCoordenadas);
        Global.tvPlaneta = (TextView) findViewById(R.id.tvPlaneta);
        Global.tvCoordenadas = (TextView) findViewById(R.id.tvCoordenadas);

        //Fin variables globales

        //variables locales

        trCrearAlianza = (TableRow) findViewById(R.id.trCrearAlianza);
        trAlianza = (TableRow) findViewById(R.id.trAlianza);
        trMiembros = (TableRow) findViewById(R.id.trMiembros);
        tlCrearAlianza = (TableLayout) findViewById(R.id.tlCrearAlianza);
        tlGestionAlianza = (TableLayout) findViewById(R.id.tlGestionAlianza);
        rlMiembros = (RelativeLayout) findViewById(R.id.rlMiembros);
        btnCrearAlianza = (Button) findViewById(R.id.btnCrearAlianza);
        btnDisolverAlianza = (Button) findViewById(R.id.btnDisolverAlianza);
        etNombreAlianza = (EditText) findViewById(R.id.etNombreAlianza);
        etEtiquetaAlianza = (EditText) findViewById(R.id.etEtiquetaAlianza);
        tvNombreAlianzaCreada = (TextView) findViewById(R.id.tvNombreAlianzaCreada);

        //Fin variables locales

        rvMiembros = (RecyclerView) findViewById(R.id.rvMiembros);
        rvMiembros.setLayoutManager(new LinearLayoutManager(this));


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
                Global.cargarSelectPlanetas();
                Global.desplegarPlanetas(MainAlianza.this, new AlertDialog.Builder(MainAlianza.this));
            }
        });

        btnCrearAlianza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map mapAlianza = new HashMap<String,Object>();
                Map miembroAlianza = new HashMap<String,Object>();
                idAlianza = Global.fFirestore.collection("Alianzas").document().getId();
                mapAlianza.put("fundador",Global.fAuth.getCurrentUser().getUid());
                mapAlianza.put("nombre",etNombreAlianza.getText().toString());
                mapAlianza.put("etiqueta",etEtiquetaAlianza.getText().toString());
                Global.fFirestore.collection("Alianzas").document(idAlianza).set(mapAlianza);
                miembroAlianza.put("id",Global.fAuth.getCurrentUser().getUid());
                miembroAlianza.put("nombre",Global.usuarioActual);
                Global.fFirestore.collection("Alianzas").document(idAlianza).collection("Miembros_Alianza").document(Global.fAuth.getCurrentUser().getUid()).set(miembroAlianza);
                Global.fFirestore.collection("Usuarios").document(Global.fAuth.getCurrentUser().getUid()).update("alianza",idAlianza);
                intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        btnDisolverAlianza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainAlianza.this, "It works!", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainAlianza.this);
                mBuilder.setTitle("Disolver");
                mBuilder.setIcon(R.drawable.icon);
                mBuilder.setMessage("¿Está seguro de disolver la alianza?");

                mBuilder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        query = Global.fFirestore.collection("Usuarios").whereEqualTo("alianza",alianzaActual);
                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot document : task.getResult()) {
                                        if (document != null && document.exists()) {
                                            Log.d("Borrando planeta", document.getString("id"));
                                            Global.fFirestore.collection("Usuarios").document().update("alianza", "");
                                        } else {
                                            Log.d("Check", "No such document");
                                        }
                                    }
                                }

                            }
                        });
                        Global.fFirestore.collection("Alianzas").document(alianzaActual).delete();
                        intent = new Intent(MainAlianza.this, MainAlianza.class);
                        Toast.makeText(MainAlianza.this, "Alianza disuelta", Toast.LENGTH_SHORT).show();
                        v.getContext().startActivity(intent);
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

        docRef = Global.fFirestore.collection("Usuarios").document(Global.fAuth.getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        if (document.getString("alianza").isEmpty()){
                            trCrearAlianza.setVisibility(View.VISIBLE);
                            tlCrearAlianza.setVisibility(View.VISIBLE);
                            trAlianza.setVisibility(View.GONE);
                            tlGestionAlianza.setVisibility(View.GONE);
                            trMiembros.setVisibility(View.GONE);
                            rlMiembros.setVisibility(View.GONE);
                        }
                        else{
                            trCrearAlianza.setVisibility(View.GONE);
                            tlCrearAlianza.setVisibility(View.GONE);
                            trAlianza.setVisibility(View.VISIBLE);
                            tlGestionAlianza.setVisibility(View.VISIBLE);
                            trMiembros.setVisibility(View.VISIBLE);
                            rlMiembros.setVisibility(View.VISIBLE);
                            cargarAlianza(document.getString("alianza"));
                            alianzaActual = document.getString("alianza");
                            query = Global.fFirestore.collection("Alianzas").document(alianzaActual).collection("Miembros_Alianza");
                            FirestoreRecyclerOptions<Miembro> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Miembro>().setQuery(query, Miembro.class).build();

                            adaptadorMiembro = new AdapterMiembros(firestoreRecyclerOptions, alianzaActual);
                            adaptadorMiembro.notifyDataSetChanged();
                            rvMiembros.setAdapter(adaptadorMiembro);

                            adaptadorMiembro.startListening();
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
        if (alianzaActual!=null) {
            adaptadorMiembro.stopListening();
        }
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