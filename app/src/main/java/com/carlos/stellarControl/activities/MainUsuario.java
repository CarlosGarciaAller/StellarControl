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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MainUsuario extends AppCompatActivity {
    private TextView tvNombre, tvEmail;
    private EditText etRenombrarUsuario, etCambiarContraseña;
    private Button btnAplicar, btnBaja;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

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

        //variables locales

        tvNombre = (TextView) findViewById(R.id.tvNombre);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        btnAplicar = (Button) findViewById(R.id.btnAplicar);
        btnBaja = (Button) findViewById(R.id.btnBaja);

        etRenombrarUsuario = (EditText) findViewById(R.id.etRenombrarUsuario);
        etCambiarContraseña = (EditText) findViewById(R.id.etCambiarContraseña);

        //Fin variables locales
        //String pass = FirebaseAuth.getInstance().getCurrentUser().get;

        tvNombre.setText(Global.usuarioActual);
        tvEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        Toast.makeText(MainUsuario.this, Global.idPlanetaSeleccionado, Toast.LENGTH_SHORT).show();

        Global.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.volverGeneral(MainUsuario.this);
            }
        });

        Global.mensajes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainUsuario.this, MainMensajes.class);
                intent.putExtra("categoria", "Consulta");
                startActivity(intent);
            }
        });

        Global.settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.desplegarOpciones(MainUsuario.this);
            }
        });

        Global.listPlanetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.desplegarPlanetas(MainUsuario.this, new AlertDialog.Builder(MainUsuario.this));
                //global.mostrarPlanetaSeleccionado(global.mFirestore.collection("Planetas").document(global.mAuth.getCurrentUser().getUid()).collection("Planetas_Jugador").whereEqualTo("nombre",global.planetaSeleccionado));
            }
        });

        btnAplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etRenombrarUsuario.getText().toString().isEmpty()) {
                    Global.fFirestore.collection("Usuarios").document(Global.fAuth.getCurrentUser().getUid()).update("nombre", etRenombrarUsuario.getText().toString());
                    intent = new Intent(MainUsuario.this, MainUsuario.class);
                    Toast.makeText(MainUsuario.this, "Cambio de nombre realizado correctamente", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainUsuario.this);

                FirebaseAuth.getInstance().getCurrentUser().updatePassword(etCambiarContraseña.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            intent = new Intent(MainUsuario.this, MainLogin.class);
                            Toast.makeText(MainUsuario.this, "Password update", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainUsuario.this, "Password could not be changed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        btnBaja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainUsuario.this);
                boolean eliminado = false;

                mBuilder.setTitle("¿Está seguro de querer darse de baja");
                mBuilder.setMessage("Todos sus datos serán eliminados sin posibilidad de recuperarse");
                mBuilder.setIcon(R.drawable.icon);
                mBuilder.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CollectionReference docInvestigaciones = Global.fFirestore.collection("Investigaciones_Jugador").document(Global.fAuth.getCurrentUser().getUid()).collection("Investigaciones_Jugador");
                        for (int i = 0; i < Global.investigaciones.length; i++){
                            docInvestigaciones.document(Global.investigaciones[i]).delete();
                        }
                        CollectionReference docPlanetas = Global.fFirestore.collection("Planetas").document(Global.fAuth.getCurrentUser().getUid()).collection("Planetas_Jugador");
                        docPlanetas.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot document : task.getResult()) {
                                        if (document != null && document.exists()) {
                                            Log.d("Borrando planeta", document.getString("id"));
                                            Global.abandonarPlaneta(document.getString("id"), document.getLong("sistema").intValue(), document.getLong("posicion").intValue());
                                        } else {
                                            Log.d("Check", "No such document");
                                        }
                                    }
                                }

                            }
                        });
                        Global.fFirestore.collection("Mensajes").document(Global.fAuth.getCurrentUser().getUid()).delete();
                        Global.fFirestore.collection("Usuarios").document(Global.fAuth.getCurrentUser().getUid()).delete();

                        intent = new Intent(MainUsuario.this, MainLoading.class);
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

        //Cargar los planetas del jugador en el selector
        Global.cargarSelectPlanetas();

    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    public void eliminarUsuario(){
        Global.fAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
                public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Usuario eliminado :( Gracias por jugar y hasta la proxima.");
                }
            }
        });
        //Global.fAuth.signOut();
        intent = new Intent(MainUsuario.this, MainLoading.class);
        startActivity(intent);
    }
}