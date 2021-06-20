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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class MainUsuario extends AppCompatActivity {
    private TextView tvNombre, tvEmail;
    private EditText etNombreUsuario, etPassword;
    private Button btnCambiarNombre, btnCambiarContraseña, btnBaja;
    Intent intent;
    Query query;
    Global global = new Global();

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

        Global.tvMetal = (TextView) findViewById(R.id.tvMetal);
        Global.tvCristal = (TextView) findViewById(R.id.tvCristal);
        Global.tvDeuterio = (TextView) findViewById(R.id.tvDeuterio);
        Global.tvPlaneta = (TextView) findViewById(R.id.tvPlaneta);
        Global.tvCoordenadas = (TextView) findViewById(R.id.tvCoordenadas);
        Global.listPlanetas = (LinearLayout) findViewById(R.id.listPlanetas);

        //Fin variables globales

        //variables locales

        tvNombre = (TextView) findViewById(R.id.tvNombre);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        btnCambiarNombre = (Button) findViewById(R.id.btnCambiarNombre);
        btnCambiarContraseña = (Button) findViewById(R.id.btnCambiarContraseña);
        btnBaja = (Button) findViewById(R.id.btnBaja);

        etNombreUsuario = new EditText(MainUsuario.this);
        etPassword = new EditText(MainUsuario.this);

        //Fin variables locales
        //String pass = FirebaseAuth.getInstance().getCurrentUser().get;

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

        btnCambiarNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainUsuario.this);

                mBuilder.setTitle("Introduzca un nuevo nombre de usuario");
                mBuilder.setIcon(R.drawable.icon);
                mBuilder.setView(etNombreUsuario);
                mBuilder.setPositiveButton("Cambiar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        global.fFirestore.collection("Usuarios").document(Global.fAuth.getCurrentUser().getUid()).update("nombre", etNombreUsuario.getText().toString());
                        dialog.cancel();
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

        btnCambiarContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainUsuario.this);

                mBuilder.setTitle("Introduzca una contraseña nueva");
                mBuilder.setMessage("Volvera a la pantalla de inicio de sesión para que los cambios se apliquen");
                mBuilder.setIcon(R.drawable.icon);
                mBuilder.setView(etPassword);
                mBuilder.setPositiveButton("Cambiar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().getCurrentUser().updatePassword(etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                        Global.fFirestore.collection("Usuarios").document(Global.fAuth.getCurrentUser().getUid()).delete();
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
        Global.cargarPlanetas();
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
        intent = new Intent(MainUsuario.this, MainActivity.class);
        startActivity(intent);
    }
}