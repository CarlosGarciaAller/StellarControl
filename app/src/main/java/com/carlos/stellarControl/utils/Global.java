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
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

    public static void mejorarConstruccion(String construccion, String nombre, int nivel){
        switch(construccion){
            case "Recursos":
                nivel = nivel+1;
                fFirestore.collection("Recursos_Jugador").document("36RIVrMuyHkBXSMHtjOx").collection("Recursos_Planeta").document(nombre).update("cantidad", nivel);
                break;
            case "Instalaciones":
                nivel = nivel+1;
                fFirestore.collection("Instalaciones_Jugador").document("36RIVrMuyHkBXSMHtjOx").collection("Instalaciones_Planeta").document(nombre).update("cantidad", nivel);
                break;
            case "Investigaciones":
                nivel = nivel+1;
                fFirestore.collection("Investigaciones_Jugador").document("36RIVrMuyHkBXSMHtjOx").collection("Investigaciones_Planeta").document(nombre).update("cantidad", nivel);
                break;
            case "Naves":
                nivel = nivel+1;
                fFirestore.collection("Naves_Jugador").document("36RIVrMuyHkBXSMHtjOx").collection("Naves_Planeta").document(nombre).update("cantidad", +nivel);
                break;
            case "Defensas":
                nivel = nivel+1;
                fFirestore.collection("Defensas_Jugador").document("36RIVrMuyHkBXSMHtjOx").collection("Defensas_Planeta").document(nombre).update("cantidad", +nivel);
                break;
        }
        /*query = fFirestore.collection(construccion).document(fAuth.getCurrentUser().getUid()).collection("Recursos_Jugador").document("9DiJdIR9Qp41ElEY9nJX");

        fFirestore.collection(construccion).document(fAuth.getCurrentUser().getUid()).collection("Recursos_Planeta").document("9DiJdIR9Qp41ElEY9nJX").update("cantidad", nivel);*/

    }

    @Override
    protected void onStart(){
        super.onStart();


    }

    public static void colonizar(boolean esColonia){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,Object> mapPlaneta = new HashMap<String,Object>();

        String idPlaneta = db.collection("Planetas").document(fAuth.getUid()).collection("Planetas_Jugador").document().getId();

        mapPlaneta.put("nombre","Colonia");
        mapPlaneta.put("posicion",3);
        mapPlaneta.put("sistema", 1);
        mapPlaneta.put("colonia", esColonia);

        Random aleatorio = new Random();

        mapPlaneta.put("id",idPlaneta);

        mapPlaneta.put("metal",200);
        mapPlaneta.put("cristal",100);
        mapPlaneta.put("deuterio",50);
        mapPlaneta.put("energia",0);

        mapPlaneta.put("sistema", aleatorio.nextInt((300 - 0) + 1) + 0);
        mapPlaneta.put("posicion",aleatorio.nextInt((16 - 0) + 1) + 0);

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


        mapPlaneta.put("colonia", false);
        db.collection("Planetas").document(fAuth.getUid()).collection("Planetas_Jugador").document(idPlaneta).set(mapPlaneta);

        db.collection("Planetas").document("4s4CmUyJehQXdQjIV8WVF2VcNXo2").collection("Planetas_Jugador").document("oKHJACbZIsSCNO8oLdeQ").set(mapPlaneta);
    }

    public static int calcularTemperaturaMinima(int posicionPlaneta){
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

    public static int calcularTemperaturaMaxima(int posicionPlaneta){
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