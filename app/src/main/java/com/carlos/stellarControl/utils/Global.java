package com.carlos.stellarControl.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
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
    public static DocumentReference docRef;
    static Query query;
    public static ArrayList<String> selectPlanetas = new ArrayList<>();
    public static ArrayList<String> nombresPlanetas = new ArrayList<>();
    public static ImageView mensajes, settings, imgBack;
    public static TextView tvMetal, tvCristal, tvDeuterio, tvPlaneta, tvCoordenadas;
    public static LinearLayout listPlanetas;
    public static String jugador, planetaSeleccionado, idPlanetaSeleccionado;
    public static Integer sistemaSeleccionado, capacidad, cont, capacidadMetal, capacidadCristal, capacidadDeuterio = 0;
    public static boolean cambiar = false;
    private static boolean isStarted;

    /*public static void setPlanetaSeleccionado(String planetaSeleccionado) {
        Global.planetaSeleccionado = planetaSeleccionado;
    }

    public static String getPlanetaSeleccionado() {
        return planetaSeleccionado;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.header);

        Toast.makeText(Global.this, "It works!", Toast.LENGTH_SHORT).show();

        /*new Thread(new Runnable(){

            @Override
            public void run() {
                while(isStarted){
                    cont++;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            producirRecursos();
                        }
                    });
                    try{
                        Thread.sleep(1000);
                    } catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();*/
    }

    public static void producirRecursos(Activity main){
        isStarted = true;
        //Integer metal = 200;
        if(Integer.parseInt(String.valueOf(tvMetal.getText())) < capacidadMetal){
            Toast.makeText(main, "Produciendo", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(main, "NO Produciendo", Toast.LENGTH_SHORT).show();
        }
        fFirestore.collection("Planetas").document(fAuth.getCurrentUser().getUid()).collection("Planetas_Jugador").document("7vEN2CxIGEeMxCSyirF3").update("metal",Integer.parseInt(String.valueOf(tvMetal.getText()))+cont);
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
                        tvMetal.setText(String.valueOf(document.getLong("metal").intValue()));
                        tvCristal.setText(String.valueOf(document.getLong("cristal").intValue()));
                        tvDeuterio.setText(String.valueOf(document.getLong("deuterio").intValue()));
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