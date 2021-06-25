package com.carlos.stellarControl.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.carlos.stellarControl.R;
import com.carlos.stellarControl.utils.Global;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainLoading extends AppCompatActivity {
    Intent intent;
    private AlertDialog dialog;
    String anteriorActividad, idPlaneta;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);

        //Variables extras

        Bundle extras = getIntent().getExtras();
        anteriorActividad = extras.getString("anteriorActividad");

        // Fin variables extras

        Global.fAuth = FirebaseAuth.getInstance();
        Global.fFirestore = FirebaseFirestore.getInstance();

        Global.imgPlanetaSeleccionado = (ImageView) findViewById(R.id.imgPlanetaSeleccionado);

        Global.tvMetal = (TextView) findViewById(R.id.tvMetal);
        Global.tvCristal = (TextView) findViewById(R.id.tvCristal);
        Global.tvDeuterio = (TextView) findViewById(R.id.tvDeuterio);
        Global.tvPlaneta = (TextView) findViewById(R.id.tvPlaneta);
        Global.tvCoordenadas = (TextView) findViewById(R.id.tvCoordenadas);
        Global.tvPlaneta = (TextView) findViewById(R.id.tvPlaneta);
        Global.tvCoordenadas = (TextView) findViewById(R.id.tvCoordenadas);

        startLoadingDialog();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (anteriorActividad.equals("baja")){
                    Global.fAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainLoading.this, ":( Gracias por jugar y hasta la proxima.", Toast.LENGTH_SHORT).show();
                                intent = new Intent(MainLoading.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                    Global.fAuth.signOut();
                }
                else{
                    dismissDialog();
                    intent = new Intent(MainLoading.this, MainGeneral.class);
                    intent.putExtra("anteriorActividad", "login");
                    intent.putExtra("nombrePlaneta", Global.planetaSeleccionado);
                    startActivity(intent);
                }
            }
        },3000);
    }

    @Override
    protected void onStart(){
        super.onStart();

        //Cargar datos del usuario logeado
        Global.getUsuarioActual(Global.fAuth.getCurrentUser().getUid());

        //Mostrar el planeta seleccionado en el selecctor de planetas
        Global.mostrarPlanetaSeleccionado(Global.fFirestore.collection("Planetas").document(Global.fAuth.getCurrentUser().getUid()).collection("Planetas_Jugador").whereEqualTo("colonia",false));

        //Cargar los planetas del jugador en el selector
        Global.cargarSelectPlanetas();
    }

    void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

    void dismissDialog(){
        dialog.dismiss();
    }
}
