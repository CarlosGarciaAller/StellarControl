package com.carlos.stellarControl.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.carlos.stellarControl.R;
import com.carlos.stellarControl.activities.MainActivity;
import com.carlos.stellarControl.activities.MainGeneral;
import com.carlos.stellarControl.activities.MainUsuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Global extends AppCompatActivity {
    private static Intent intent;
    public static FirebaseAuth fAuth;
    public static FirebaseFirestore fFirestore;
    public static DocumentReference docRef, docNivel;
    static Query query;
    public static ArrayList<String> selectPlanetas = new ArrayList<>();
    public static ArrayList<String> nombresPlanetas = new ArrayList<>();
    public static ImageView mensajes, settings, imgBack;
    public static TextView tvMetal, tvCristal, tvDeuterio, tvPlaneta, tvCoordenadas;
    public static LinearLayout listPlanetas;
    public static String jugador, planetaSeleccionado, idPlanetaSeleccionado;
    public static Integer sistemaSeleccionado, capacidad, incremento, nivel,
            cantidadMetal, cantidadCristal, cantidadDeuterio,
            capacidadMetal, capacidadCristal, capacidadDeuterio = 0;
    public static boolean cambiar = false;
    private static boolean isStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.header);
    }
    public static void producirRecursos(String mina, String recurso){

        docNivel = fFirestore.collection("Recursos_Jugador").document(idPlanetaSeleccionado).collection("Recursos_Planeta").document(mina);
        docNivel.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                            nivel = document.getLong("cantidad").intValue();
                    } else {
                        Log.d("Check", "No such document");
                    }
                }
            }
        });

        docRef = fFirestore.collection("Planetas").document(fAuth.getCurrentUser().getUid()).collection("Planetas_Jugador").document(idPlanetaSeleccionado);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        if ("metal".equals(recurso)){
                            incremento = nivel * 3;
                        }
                        if ("cristal".equals(recurso)){
                            incremento = nivel * 2;
                        }
                        if ("metal".equals(recurso)){
                            incremento = nivel * 1;
                        }
                        if (document.getLong(recurso) == 0){
                            fFirestore.collection("Planetas").document(fAuth.getCurrentUser().getUid()).collection("Planetas_Jugador").document(idPlanetaSeleccionado).update(
                                    recurso,0+incremento);
                        }
                        else{
                            fFirestore.collection("Planetas").document(fAuth.getCurrentUser().getUid()).collection("Planetas_Jugador").document(idPlanetaSeleccionado).update(
                                    recurso,document.getLong(recurso).intValue()+incremento);
                        }
                    } else {
                        Log.d("Check", "No such document");
                    }
                }
            }
        });
    }

    public static void cargarPlanetas(){
        fFirestore.collection("Planetas").document(fAuth.getCurrentUser().getUid()).collection("Planetas_Jugador").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException error) {
                selectPlanetas.clear();
                nombresPlanetas.clear();

                for (DocumentSnapshot snapshot : documentSnapshots){
                    selectPlanetas.add(snapshot.getString("nombre")+" -> "+snapshot.getLong("sistema")+":"+snapshot.getLong("posicion"));
                    nombresPlanetas.add(snapshot.getString("nombre"));
                }
            }
        });
        cargarRecursos(planetaSeleccionado);
    }

    public static void cargarRecursos(String planetaSeleccionado){
        query = fFirestore.collection("Planetas").document(fAuth.getCurrentUser().getUid()).collection("Planetas_Jugador").whereEqualTo("nombre",planetaSeleccionado);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        cantidadMetal = document.getLong("metal").intValue();
                        tvMetal.setText(String.valueOf(document.getLong("metal").intValue()));
                        if (Integer.parseInt(String.valueOf(tvMetal.getText())) >= capacidadMetal){
                            tvMetal.setTextColor(Color.parseColor("#FF0000"));
                        }
                        cantidadCristal = document.getLong("cristal").intValue();
                        tvCristal.setText(String.valueOf(document.getLong("cristal").intValue()));
                        if (Integer.parseInt(String.valueOf(tvCristal.getText())) >= capacidadCristal){
                            tvCristal.setTextColor(Color.parseColor("#FF0000"));
                        }
                        cantidadDeuterio = document.getLong("deuterio").intValue();
                        tvDeuterio.setText(String.valueOf(document.getLong("deuterio").intValue()));
                        if (Integer.parseInt(String.valueOf(tvDeuterio.getText())) >= capacidadDeuterio){
                            tvDeuterio.setTextColor(Color.parseColor("#FF0000"));
                        }
                    }
                }
            }
        });
    }

    public static void mostrarPlanetaSeleccionado(Query query){
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.e("Check","It works");
                        idPlanetaSeleccionado = document.getString("id");
                        tvPlaneta.setText(document.getString("nombre"));
                        planetaSeleccionado = document.getString("nombre");
                        sistemaSeleccionado = document.getLong("sistema").intValue();
                        tvCoordenadas.setText(String.valueOf(document.getLong("sistema").intValue())+":"+String.valueOf(document.getLong("posicion").intValue()));
                        capacidadMetal = document.getLong("capacidadMetal").intValue();
                        capacidadCristal = document.getLong("capacidadCristal").intValue();
                        capacidadDeuterio = document.getLong("capacidadDeuterio").intValue();
                        cargarRecursos(document.getString("nombre"));
                    }
                }
            }
        });
    }

    public static Integer obtenerCapacidad(Query query, String capacidadRecurso){
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        capacidad = document.getLong(capacidadRecurso).intValue();
                    }
                }
            }
        });
        return capacidad;
    }

    public static void cambiarPlaneta(Activity main, String nombrePlaneta){
        intent = new Intent(main, MainGeneral.class);
        //Toast.makeText(main, nombrePlaneta, Toast.LENGTH_SHORT).show();
        query = fFirestore.collection("Planetas").document(fAuth.getCurrentUser().getUid()).collection("Planetas_Jugador").whereEqualTo("nombre",nombrePlaneta);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        idPlanetaSeleccionado = document.getString("id");
                        tvPlaneta.setText(document.getString("nombre"));
                        planetaSeleccionado = document.getString("nombre");
                        sistemaSeleccionado = document.getLong("sistema").intValue();
                        tvCoordenadas.setText(String.valueOf(document.getLong("sistema").intValue())+":"+String.valueOf(document.getLong("posicion").intValue()));
                        sistemaSeleccionado = document.getLong("sistema").intValue();
                        capacidadMetal = document.getLong("capacidadMetal").intValue();
                        capacidadCristal = document.getLong("capacidadCristal").intValue();
                        capacidadDeuterio = document.getLong("capacidadDeuterio").intValue();
                        cargarRecursos(document.getString("nombre"));
                    }
                }
            }
        });
    }

    public static void desplegarPlanetas(Activity main, AlertDialog.Builder mBuilder){
        mBuilder.setTitle("Seleccione un planeta");
        mBuilder.setIcon(R.drawable.icon);
        mBuilder.setItems(selectPlanetas.toArray(new String[0]), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                intent = new Intent(main, MainGeneral.class);
                //planetaSeleccionado = nombresPlanetas.get(i);
                //Toast.makeText(main, nombresPlanetas.get(i)+":"+planetaSeleccionado, Toast.LENGTH_SHORT).show();
                cambiarPlaneta(main, nombresPlanetas.get(i));
                intent.putExtra("anteriorActividad", "seleccion");
                main.startActivity(intent);
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

    public static void desplegarOpciones(Activity main){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(main);
        String[] listOpciones = new String[]{"Configuración","Cerrar sesión"};
        mBuilder.setTitle("Opciones");
        mBuilder.setIcon(R.drawable.icon);
        mBuilder.setItems(listOpciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                switch(listOpciones[i]){
                    case "Configuración":
                        intent = new Intent(main, MainUsuario.class);
                        main.startActivity(intent);
                        break;
                    case "Cerrar sesión":
                        fAuth.signOut();
                        intent = new Intent(main, MainActivity.class);
                        main.startActivity(intent);
                }
                Toast.makeText(main, listOpciones[i], Toast.LENGTH_SHORT).show();
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

    public static void volverGeneral(Activity main){
        intent = new Intent(main, MainGeneral.class);
        intent.putExtra("anteriorActividad", "atras");
        main.startActivity(intent);
    }

    @Override
    protected void onStart(){
        super.onStart();
    }
}