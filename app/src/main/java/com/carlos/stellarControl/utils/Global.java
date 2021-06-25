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
import com.carlos.stellarControl.activities.MainConstruccion;
import com.carlos.stellarControl.activities.MainGeneral;
import com.carlos.stellarControl.activities.MainUsuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Global extends AppCompatActivity {
    private static Intent intent;
    public static FirebaseAuth fAuth;
    public static FirebaseFirestore fFirestore;
    public static FirebaseDatabase database;
    static Query query;
    public static ArrayList<String> selectPlanetas = new ArrayList<>();
    public static ArrayList<String> nombresPlanetas = new ArrayList<>();
    public static ImageView mensajes, settings, imgBack, imgPlanetaSeleccionado;
    public static TextView tvMetal, tvCristal, tvDeuterio, tvEnergia, tvPlaneta, tvCoordenadas;
    public static LinearLayout listPlanetas;
    public static String usuarioActual, planetaSeleccionado, idPlanetaSeleccionado;
    public static int incremento = 0;
    public static Integer sistemaSeleccionado, posicionSeleccionado, camposUsadosSeleccionado, camposTotalesSeleccionado,
            nivelMetalActual, nivelCristalActual, nivelDeuterioActual,
            cantidadMetal, cantidadCristal, cantidadDeuterio,
            capacidadMetal, capacidadCristal, capacidadDeuterio = 0;
    public static DocumentReference docPlaneta, docPlanetaSistema, docUser;
    public static boolean isStarted, metalRunning, cristalRunning, deuterioRunning = false;

    public static String[] defensas = {
            "Lanzamisiles",
            "Laser pequeño",
            "Laser grande",
            "Cañon gauss",
            "Cañon ionico",
            "Cañon de plasma"
    }, instalaciones = {
            "Fabrica de nanobots",
            "Hangar",
            "Laboratorio de investigacion",
            "Fabrica de robots",
            "Terraformer",
            "Sensor phalanx",
    }, investigaciones = {
            "Tecnologia de energia",
            "Tecnologia laser",
            "Tecnologia ionica",
            "Tecnologia de plasma",
            "Motor de combustion",
            "Motor de impulso",
            "Propulsor hiperespacial",
            "Colonizacion",
            "Graviton",
            "Tecnologia de ataque",
            "Tecnologia de defensa",
            "Tecnologia de blindaje"
    }, naves = {
            "Nave pequeña de carga",
            "Nave grande de carga",
            "Colonizador",
            "Cazador ligero",
            "Cazador pesado",
            "Crucero",
            "Nave de guerra",
            "Acorazado",
            "Bombardero",
            "Destructor",
            "Estrella de la muerte"
    }, recursos = {
            "Mina de metal",
            "Mina de cristal",
            "Sintetizador de deuterio",
            "Planta de energia solar",
            "Almacen de metal",
            "Almacen de cristal",
            "Almacen de deuterio",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.header);

    }

    public static void getUsuarioActual(String idUsuario){
        docUser = Global.fFirestore.collection("Usuarios").document(idUsuario);
        docUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        usuarioActual = document.getString("nombre");
                    }
                    else{
                        Log.d("Check", "No such document");
                    }
                }
            }
        });
    }

    public static void cargarSelectPlanetas(){
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

    public static void mostrarPlanetaSeleccionado(Query query){
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        idPlanetaSeleccionado = document.getString("id");
                        tvPlaneta.setText(document.getString("nombre"));
                        planetaSeleccionado = document.getString("nombre");
                        sistemaSeleccionado = document.getLong("sistema").intValue();
                        posicionSeleccionado = document.getLong("posicion").intValue();
                        tvCoordenadas.setText(String.valueOf(document.getLong("sistema").intValue())+":"+String.valueOf(document.getLong("posicion").intValue()));
                        DocumentReference docImagen = fFirestore.collection("Sistemas").document(String.valueOf(document.getLong("sistema").intValue())).collection("Planetas_Sistemas").document(String.valueOf(document.getLong("posicion").intValue()));
                        docImagen.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document != null && document.exists()) {
                                        Picasso.get().load(document.getString("imagen")).into(imgPlanetaSeleccionado);
                                    } else {
                                        Log.d("Check", "No such document");
                                    }
                                }
                            }
                        });
                        capacidadMetal = document.getLong("capacidadMetal").intValue();
                        capacidadCristal = document.getLong("capacidadCristal").intValue();
                        capacidadDeuterio = document.getLong("capacidadDeuterio").intValue();
                        camposUsadosSeleccionado = document.getLong("camposUsados").intValue();
                        camposTotalesSeleccionado = document.getLong("camposTotales").intValue();
                        Log.e("Check","Planeta actual: "+document.getString("nombre")+
                                " ID: "+document.getString("id")+
                                " Coordenadas: ["+document.getLong("sistema").intValue()+":"+document.getLong("posicion").intValue()+"]");
                        cargarRecursos(document.getString("nombre"));

                        // Guardar niveles de las minas del planeta
                        DocumentReference docMetal = fFirestore.collection("Recursos_Jugador").document(idPlanetaSeleccionado).collection("Recursos_Planeta").document("Mina de metal");
                        docMetal.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document != null && document.exists()) {
                                        nivelMetalActual = document.getLong("cantidad").intValue();
                                    } else {
                                        Log.d("Check", "No such document");
                                    }
                                }
                            }
                        });
                        DocumentReference docCristal = fFirestore.collection("Recursos_Jugador").document(idPlanetaSeleccionado).collection("Recursos_Planeta").document("Mina de cristal");
                        docCristal.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document != null && document.exists()) {
                                        nivelCristalActual = document.getLong("cantidad").intValue();
                                    } else {
                                        Log.d("Check", "No such document");
                                    }
                                }
                            }
                        });
                        DocumentReference docDeuterio = fFirestore.collection("Recursos_Jugador").document(idPlanetaSeleccionado).collection("Recursos_Planeta").document("Sintetizador de deuterio");
                        docDeuterio.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document != null && document.exists()) {
                                        nivelDeuterioActual = document.getLong("cantidad").intValue();
                                    } else {
                                        Log.d("Check", "No such document");
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
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

    //comprueba si los almacenes están llenos
    public static boolean espacioSuficiente(TextView recurso, int capacidad){
        boolean producir = false;
        Log.e("Check", "Cantidad Recurso: "+Integer.parseInt(String.valueOf(recurso.getText()))+" Capacidad: "+capacidad);
        if (Integer.parseInt(String.valueOf(recurso.getText())) <= capacidad){
            Log.e("Check", "produciendo");
            producir = true;
        }
        return producir;
    }

    //comprueba si hay suficientes recursos para construir
    public static boolean recursosSuficientes(String recurso, int coste){
        boolean disponible = false;
        Log.e("Check", "Cantidad Recurso: "+Integer.parseInt(String.valueOf(tvMetal.getText()))+" Coste: "+coste);
        if (recurso.equals("Metal")){
            if (Integer.parseInt(String.valueOf(tvMetal.getText())) >= coste){
                disponible = true;
            }
        }
        Log.e("Check", "Cantidad Recurso: "+Integer.parseInt(String.valueOf(tvCristal.getText()))+" Coste: "+coste);
        if (recurso.equals("Cristal")){
            if (Integer.parseInt(String.valueOf(tvCristal.getText())) >= coste){
                disponible = true;
            }
        }
        Log.e("Check", "Cantidad Recurso: "+Integer.parseInt(String.valueOf(tvDeuterio.getText()))+" Coste: "+coste);
        if (recurso.equals("Deuterio")){
            if (Integer.parseInt(String.valueOf(tvDeuterio.getText())) >= coste){
                disponible = true;
            }
        }
        return disponible;
    }

    //añade recursos a los contadores
    public static void producirRecursos(String recurso){
        Log.e("Check", recurso+"Metal actual: "+tvMetal.getText()+ " Cristal actual: "+tvCristal.getText()+ " Deuterio actual: "+tvDeuterio.getText());
        if (recurso.equals("metal") && nivelMetalActual >= 1){
            Log.e("Check", "Produciendo: "+recurso);
            incremento += nivelMetalActual * 6;
            int auxMetal = cantidadMetal + incremento;
            tvMetal.setText(""+auxMetal);
            //fFirestore.collection("Planetas").document(fAuth.getCurrentUser().getUid()).collection("Planetas_Jugador").document(planetaSeleccionado).update("metal", auxMetal);
        }

        if (recurso.equals("cristal") && nivelCristalActual >= 1){
            Log.e("Check", "Produciendo: "+recurso);
            incremento += nivelCristalActual * 2;
            int auxCristal = cantidadCristal + incremento;
            tvCristal.setText(""+auxCristal);
            //fFirestore.collection("Planetas").document(fAuth.getCurrentUser().getUid()).collection("Planetas_Jugador").document(planetaSeleccionado).update("deuterio", auxCristal);
        }
        if (recurso.equals("deuterio") && nivelDeuterioActual >= 1){
            Log.e("Check", "Produciendo: "+recurso);
            incremento += nivelDeuterioActual * 4;
            int auxDeuterio = cantidadDeuterio + incremento;
            tvDeuterio.setText(""+auxDeuterio);
            //fFirestore.collection("Planetas").document(fAuth.getCurrentUser().getUid()).collection("Planetas_Jugador").document(planetaSeleccionado).update("cristal", auxDeuterio);
        }

        /*docPlaneta = fFirestore.collection("Planetas").document(Global.fAuth.getCurrentUser().getUid()).collection("Planetas_Jugador").document(idPlanetaSeleccionado);
        docPlaneta.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
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
                            //fFirestore.collection("Planetas").document(fAuth.getCurrentUser().getUid()).collection("Planetas_Jugador").document(idPlanetaSeleccionado).update(recurso,0+incremento);
                        }
                        else{
                            //fFirestore.collection("Planetas").document(fAuth.getCurrentUser().getUid()).collection("Planetas_Jugador").document(idPlanetaSeleccionado).update(recurso,document.getLong(recurso).intValue()+incremento);
                        }
                    } else {
                        Log.d("Check", "No such document");
                    }
                }
            }
        });*/
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
                        posicionSeleccionado = document.getLong("posicion").intValue();
                        tvCoordenadas.setText(String.valueOf(document.getLong("sistema").intValue())+":"+String.valueOf(document.getLong("posicion").intValue()));
                        sistemaSeleccionado = document.getLong("sistema").intValue();
                        capacidadMetal = document.getLong("capacidadMetal").intValue();
                        capacidadCristal = document.getLong("capacidadCristal").intValue();
                        capacidadDeuterio = document.getLong("capacidadDeuterio").intValue();
                        camposUsadosSeleccionado = document.getLong("camposUsados").intValue();
                        camposTotalesSeleccionado = document.getLong("camposTotales").intValue();
                        Log.e("Check","Desplazando a: "+document.getString("nombre")+
                                " ID: "+document.getString("id")+
                                " Coordenadas: ["+document.getLong("sistema").intValue()+":"+document.getLong("posicion").intValue()+"]");
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
                planetaSeleccionado = nombresPlanetas.get(i);
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
        String[] listOpciones = new String[]{"Configuración", "Cerrar sesión"};
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

    public static void comprobarConstruccion(String construccion, Activity main){
        DocumentReference docHangar = Global.fFirestore.collection("Instalaciones_Jugador").document(Global.idPlanetaSeleccionado).collection("Instalaciones_Planeta").document(construccion);
        docHangar.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        if (document.getLong("cantidad") == 0){
                            Toast.makeText(main, "Primero debes construir un"+construccion, Toast.LENGTH_SHORT).show();
                        }
                        else{
                            intent = new Intent(main, MainConstruccion.class);
                            intent.putExtra("Construcción", "Naves");
                            main.startActivity(intent);
                        }
                    } else {
                        Log.d("Check", "No such document");
                    }
                }
            }
        });
    }

    public static void asignarPlaneta(FirebaseAuth user, FirebaseFirestore db, String idPlaneta, String nombrePlaneta, String usuario, int metal, int cristal, int deuterio, int sistema, int posicion, boolean esColonia){
        Random aleatorio = new Random();

        Map mapSistema = new HashMap<String,Object>();

        mapSistema.put("idPlanetaJugador", idPlaneta);
        mapSistema.put("nombre", nombrePlaneta);
        mapSistema.put("posicion", posicion);
        mapSistema.put("usuario", usuario);

        Log.d("idPlanetaJugador: ", String.valueOf(idPlaneta));
        Log.d("posicion: ", String.valueOf(posicion));
        Log.d("sistema: ", String.valueOf(sistema));
        switch(posicion){
            case 1:
                mapSistema.put("imagen", "https://firebasestorage.googleapis.com/v0/b/stellar-control.appspot.com/o/Planetas%2Fplanet1.png?alt=media&token=04b41e8c-4a78-464f-8f42-699da430d313");
                break;
            case 2:
                mapSistema.put("imagen", "https://firebasestorage.googleapis.com/v0/b/stellar-control.appspot.com/o/Planetas%2Fplanet2.png?alt=media&token=f8fe5d67-0e64-4681-a337-3975938310bb");
                break;
            case 3:
                mapSistema.put("imagen", "https://firebasestorage.googleapis.com/v0/b/stellar-control.appspot.com/o/Planetas%2Fplanet3.png?alt=media&token=4e52cf84-6b23-4630-a329-1ace76953c20");
                break;
            case 4:
                mapSistema.put("imagen", "https://firebasestorage.googleapis.com/v0/b/stellar-control.appspot.com/o/Planetas%2Fplanet4.png?alt=media&token=e3cd5236-1390-4759-8a38-71f2561ea990");
                break;
            case 5:
                mapSistema.put("imagen", "https://firebasestorage.googleapis.com/v0/b/stellar-control.appspot.com/o/Planetas%2Fplanet5.png?alt=media&token=5f2918b9-8a9e-458d-834e-f1fa0fa5ff3d");
                break;
            case 6:
                mapSistema.put("imagen", "https://firebasestorage.googleapis.com/v0/b/stellar-control.appspot.com/o/Planetas%2Fplanet6.png?alt=media&token=f2eb3d33-255d-48b6-ba3e-c1946aa13f11");
                break;
            case 7:
                mapSistema.put("imagen", "https://firebasestorage.googleapis.com/v0/b/stellar-control.appspot.com/o/Planetas%2Fplanet7.png?alt=media&token=a0df3b20-6c8d-4b9c-9fbf-4b553d416f2f");
                break;
            case 8:
                mapSistema.put("imagen", "https://firebasestorage.googleapis.com/v0/b/stellar-control.appspot.com/o/Planetas%2Fplanet8.png?alt=media&token=e75a3572-4eab-4839-9166-4b89d4d72c4e");
                break;
            case 9:
                mapSistema.put("imagen", "https://firebasestorage.googleapis.com/v0/b/stellar-control.appspot.com/o/Planetas%2Fplanet9.png?alt=media&token=0c64cb66-bad9-478a-9fa9-c99d38c26698");
                break;
        }

        db.collection("Sistemas").document(String.valueOf(sistema)).collection("Planetas_Sistemas").document(String.valueOf(posicion)).set(mapSistema);

        Map mapPlaneta = new HashMap<String,Object>();

        mapPlaneta.put("id",idPlaneta);
        mapPlaneta.put("nombre",nombrePlaneta);
        mapPlaneta.put("sistema", sistema);
        mapPlaneta.put("posicion", posicion);
        mapPlaneta.put("metal",metal);
        mapPlaneta.put("cristal",cristal);
        mapPlaneta.put("deuterio",deuterio);
        mapPlaneta.put("energia",0);
        mapPlaneta.put("capacidadMetal",10000);
        mapPlaneta.put("capacidadCristal",10000);
        mapPlaneta.put("capacidadDeuterio",10000);
        mapPlaneta.put("camposUsados",0);
        mapPlaneta.put("colonia", esColonia);

        int posicionPlaneta = (int) mapPlaneta.get("posicion");

        int aleatorioDiametro = 0;
        double campos = 0.0;

        if (posicionPlaneta == 1 || posicionPlaneta == 2 || posicionPlaneta == 3){
            aleatorioDiametro = aleatorio.nextInt((7000 - 4000) + 1) + 4000;
            campos = aleatorioDiametro / 100;
        }

        if (posicionPlaneta == 4 || posicionPlaneta == 5 || posicionPlaneta == 6){
            aleatorioDiametro = aleatorio.nextInt((31000 - 12000) + 1) + 12000;
            campos = aleatorioDiametro / 100;
        }

        if (posicionPlaneta == 7 || posicionPlaneta == 8 || posicionPlaneta == 9){
            aleatorioDiametro = aleatorio.nextInt((25500 - 12500) + 1) + 12500;
            campos = aleatorioDiametro / 100;
        }

        if (posicionPlaneta == 10 || posicionPlaneta == 11 || posicionPlaneta == 12){
            aleatorioDiametro = aleatorio.nextInt((12500 - 7500) + 1) + 7500;
            campos = aleatorioDiametro / 100;
        }

        if (posicionPlaneta == 13 || posicionPlaneta == 14 || posicionPlaneta == 15){
            aleatorioDiametro = aleatorio.nextInt((19000 - 6000) + 1) + 6000;
            campos = aleatorioDiametro / 100;
        }

        mapPlaneta.put("diametro",aleatorioDiametro);
        mapPlaneta.put("camposTotales",campos);
        mapPlaneta.put("temperaturaMinima", calcularTemperaturaMinima(posicionPlaneta));
        mapPlaneta.put("temperaturaMaxima", calcularTemperaturaMaxima(posicionPlaneta));

        db.collection("Planetas").document(user.getUid()).collection("Planetas_Jugador").document(idPlaneta).set(mapPlaneta);
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

    public static void inicializarRecursosPlaneta(FirebaseFirestore db, String idPlaneta){
        Map mapRecursos = new HashMap<String,Object>();



        Integer[] costeMetal = {60, 48, 225, 75, 1000, 1000, 1000};

        Integer[] costeCristal = {15, 24, 75, 75, 0, 500, 1000};

        Integer[] costeDeuterio = {60, 48, 225, 75, 0, 0, 0};

        Integer[] costeEnergia = {11, 11, 22, 0, 0, 0, 0};

        Integer[] cantidad = {0, 0, 0, 0, 0, 0, 0};

        for (int i = 0; i < recursos.length; i++){
            mapRecursos.put("nombre",recursos[i]);
            mapRecursos.put("costeMetal",costeMetal[i]);
            mapRecursos.put("costeCristal",costeCristal[i]);
            mapRecursos.put("costeDeuterio",costeDeuterio[i]);
            mapRecursos.put("costeEnergia",costeEnergia[i]);
            mapRecursos.put("cantidad",cantidad[i]);
            db.collection("Recursos_Jugador").document(idPlaneta).collection("Recursos_Planeta").document(recursos[i]).set(mapRecursos);
        }
    }

    public static void inicializarInstalacionesPlaneta(FirebaseFirestore db, String idPlaneta){
        Map mapInstalaciones = new HashMap<String,Object>();

        Integer[] costeMetal = {1000000, 400, 200, 400, 0, 20000};

        Integer[] costeCristal = {500000 ,200, 400, 120, 50000, 40000};

        Integer[] costeDeuterio = {100000 ,100, 200, 225, 100000, 20000};

        Integer[] costeEnergia = {0, 0, 0, 0, 0, 1000, 0};

        Integer[] nivel = {0, 0, 0, 0, 0, 0};

        for (int i = 0; i < instalaciones.length; i++){
            mapInstalaciones.put("nombre",instalaciones[i]);
            mapInstalaciones.put("costeMetal",costeMetal[i]);
            mapInstalaciones.put("costeCristal",costeCristal[i]);
            mapInstalaciones.put("costeDeuterio",costeDeuterio[i]);
            mapInstalaciones.put("costeEnergia",costeEnergia[i]);
            mapInstalaciones.put("cantidad",nivel[i]);
            db.collection("Instalaciones_Jugador").document(idPlaneta).collection("Instalaciones_Planeta").document(instalaciones[i]).set(mapInstalaciones);
        }
    }

    public static void inicializarInvestigacionesJugador(FirebaseAuth user, FirebaseFirestore db){
        Map mapInvestigaciones = new HashMap<String,Object>();

        Integer[] costeMetal = {0, 200, 1000, 2000, 400, 2000, 10000, 4000, 0, 800, 200, 1000};

        Integer[] costeCristal = {800, 100, 300, 4000, 0, 4000, 20000, 8000, 0, 200, 600, 0};

        Integer[] costeDeuterio = {400, 0, 100, 1000, 600, 600, 6000, 4000, 0, 0, 0, 0};

        Integer[] costeEnergia = {0, 0, 0, 0, 0, 0, 0, 0, 300000, 0, 0, 0};

        Integer[] nivel = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        for (int i = 0; i < investigaciones.length; i++){
            mapInvestigaciones.put("nombre",investigaciones[i]);
            mapInvestigaciones.put("costeMetal",costeMetal[i]);
            mapInvestigaciones.put("costeCristal",costeCristal[i]);
            mapInvestigaciones.put("costeDeuterio",costeDeuterio[i]);
            mapInvestigaciones.put("costeEnergia",costeEnergia[i]);
            mapInvestigaciones.put("cantidad",nivel[i]);
            db.collection("Investigaciones_Jugador").document(user.getUid()).collection("Investigaciones_Jugador").document(investigaciones[i]).set(mapInvestigaciones);
        }
    }

    public static void inicializarNavesPlaneta(FirebaseFirestore db, String idPlaneta){
        Map mapNaves = new HashMap<String,Object>();

        Integer[] costeMetal = {2000, 6000, 10000, 3000, 6000, 20000, 45000, 30000, 50000, 60000,  5000000};

        Integer[] costeCristal = {2000, 6000, 20000, 1000, 4000, 7000, 15000, 40000, 25000, 50000, 4000000};

        Integer[] costeDeuterio = {0, 0, 10000, 0, 0, 2000, 0, 15000, 15000, 15000, 1000000};

        Integer[] costeEnergia = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        Integer[] cantidad = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        Integer[] ataque = {5, 5, 50, 50, 150, 400, 1000, 700, 1000, 2000, 200000};

        Integer[] defensa = {4000, 12000, 30000, 4000, 10000, 27000, 60000, 70000, 75000, 110000, 9000000};

        Integer[] escudo = {10, 25, 100, 10, 25, 50, 200, 400, 500, 500, 50000};

        for (int i = 0; i < naves.length; i++){
            mapNaves.put("nombre",naves[i]);
            mapNaves.put("costeMetal",costeMetal[i]);
            mapNaves.put("costeCristal",costeCristal[i]);
            mapNaves.put("costeDeuterio",costeDeuterio[i]);
            mapNaves.put("costeEnergia",costeEnergia[i]);
            mapNaves.put("ataque",ataque[i]);
            mapNaves.put("defensa",defensa[i]);
            mapNaves.put("escudo",escudo[i]);
            mapNaves.put("cantidad",cantidad[i]);

            db.collection("Naves_Jugador").document(idPlaneta).collection("Naves_Planeta").document(naves[i]).set(mapNaves);
        }
    }

    public static void inicializarDefensasPlaneta(FirebaseFirestore db, String idPlaneta){
        Map mapDefensas = new HashMap<String,Object>();

        Integer[] costeMetal = {2000, 1500, 6000, 20000, 5000, 50000};

        Integer[] costeCristal = {0, 500, 2000, 15000, 3000, 50000};

        Integer[] costeDeuterio = {0, 0, 0, 2000, 0, 30000};

        Integer[] costeEnergia = {0, 0, 0, 0, 0, 0};

        Integer[] cantidad = {0, 0, 0, 0, 0, 0};

        Integer[] ataque = {80, 100, 250, 1100, 150, 3000};

        Integer[] defensa = {2000, 2000, 8000, 35000, 8000, 100000};

        Integer[] escudo = {20, 25, 100, 200, 500, 300};

        for (int i = 0; i < defensas.length; i++){
            mapDefensas.put("nombre",defensas[i]);
            mapDefensas.put("costeMetal",costeMetal[i]);
            mapDefensas.put("costeCristal",costeCristal[i]);
            mapDefensas.put("costeDeuterio",costeDeuterio[i]);
            mapDefensas.put("costeEnergia",costeEnergia[i]);
            mapDefensas.put("ataque",ataque[i]);
            mapDefensas.put("defensa",defensa[i]);
            mapDefensas.put("escudo",escudo[i]);
            mapDefensas.put("cantidad",cantidad[i]);
            mapDefensas.put("cantidad",cantidad[i]);

            db.collection("Defensas_Jugador").document(idPlaneta).collection("Defensas_Planeta").document(defensas[i]).set(mapDefensas);
        }
    }

    public static void abandonarPlaneta(String idPlaneta, int sistema, int posicion){
        CollectionReference docDefensas = fFirestore.collection("Defensas_Jugador").document(idPlaneta).collection("Defensas_Planeta");

        for (int i = 0; i < defensas.length; i++){
            docDefensas.document(defensas[i]).delete();
        }

        CollectionReference docInstalaciones = fFirestore.collection("Instalaciones_Jugador").document(idPlaneta).collection("Instalaciones_Planeta");

        for (int i = 0; i < instalaciones.length; i++){
            docInstalaciones.document(instalaciones[i]).delete();
        }

        CollectionReference docNaves = fFirestore.collection("Naves_Jugador").document(idPlaneta).collection("Naves_Planeta");

        for (int i = 0; i < naves.length; i++){
            docNaves.document(naves[i]).delete();
        }

        CollectionReference docRecursos = fFirestore.collection("Recursos_Jugador").document(idPlaneta).collection("Recursos_Planeta");

        for (int i = 0; i < recursos.length; i++){
            docRecursos.document(recursos[i]).delete();
        }

        fFirestore.collection("Recursos_Jugador").document(idPlaneta).delete();
        Log.d("Usuario borrando planeta", fAuth.getCurrentUser().getUid());
        fFirestore.collection("Planetas").document(fAuth.getCurrentUser().getUid()).collection("Planetas_Jugador").document(idPlaneta).delete();

        docPlanetaSistema = fFirestore.collection("Sistemas").document(String.valueOf(sistema)).collection("Planetas_Sistemas").document(String.valueOf(posicion));
        docPlanetaSistema.update("idPlanetaJugador", " ");
        docPlanetaSistema.update("nombrePlaneta", " ");
        docPlanetaSistema.update("usuario", " ");
    }

    public static void crearMensaje(FirebaseFirestore db, String idUsuario, String asunto, String categoria, String contenido, String remitente){

        Map mapMensaje = new HashMap<String,Object>();
        String idMensaje = db.collection("Mensajes").document(idUsuario).collection("Mensajes_Jugador").document().getId();

        mapMensaje.put("asunto", asunto);
        mapMensaje.put("categoria", categoria);
        mapMensaje.put("contenido", contenido);
        mapMensaje.put("remitente",remitente);

        db.collection("Mensajes").document(idUsuario).collection("Mensajes_Jugador").document(idMensaje).set(mapMensaje);

    }

    public static void espiar(){
        //crearMensaje(String idUsuario, String asunto, String categoria, String contenido, String remitente);

    }

    @Override
    protected void onStart(){
        super.onStart();
    }
}