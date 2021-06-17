package com.carlos.stellarControl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.carlos.stellarControl.R;
import com.carlos.stellarControl.utils.Global;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainRegistro extends AppCompatActivity {
    Intent intent;
    FirebaseAuth fAuth;
    FirebaseFirestore fFirestore;
    EditText etUserRegister, etEmailRegister, etPassRegister;
    Button btnRegister;
    String usuario, email, password, idPlaneta;
    //Global global = new Global();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fAuth = FirebaseAuth.getInstance();

        fFirestore = FirebaseFirestore.getInstance();

        etUserRegister =(EditText)findViewById(R.id.etUserRegister);
        etEmailRegister =(EditText)findViewById(R.id.etEmailRegister);
        etPassRegister =(EditText)findViewById(R.id.etPassRegister);
        btnRegister =(Button)findViewById(R.id.btnRegister);

        /*if (fAuth.getCurrentUser()!= null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }*/

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario = etUserRegister.getText().toString().trim();
                email = etEmailRegister.getText().toString().trim();
                password = etPassRegister.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    etEmailRegister.setError(getString(R.string.emailRequired));
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    etPassRegister.setError(getString(R.string.passwordRequired));
                    return;
                }

                if(password.length() < 6){
                    etPassRegister.setError("Password Must be >= 6 Characters");
                }

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            idPlaneta = fFirestore.collection("Planetas").document(fAuth.getUid()).collection("Planetas_Jugador").document().getId();
                            crearUsuario(fFirestore);
                            asignarPlaneta(fFirestore);
                            inicializarRecursosPlaneta(fFirestore);
                            inicializarInstalacionesPlaneta(fFirestore);
                            inicializarInvestigacionesJugador(fFirestore);
                            inicializarNavesPlaneta(fFirestore);
                            inicializarDefensasPlaneta(fFirestore);
                            Global.mostrarPlanetaSeleccionado(fFirestore.collection("Planetas").document(fAuth.getUid()).collection("Planetas_Jugador").whereEqualTo("colonia",false));
                            intent = new Intent(MainRegistro.this, MainGeneral.class);
                            intent.putExtra("login", "login");
                            intent.putExtra("anteriorActividad", "registrar");
                            Toast.makeText(MainRegistro.this, "Bienvenido!", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainRegistro.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void crearUsuario(FirebaseFirestore db){
        Map mapUsuario = new HashMap<String,Object>();
        mapUsuario.put("nombre",usuario);
        mapUsuario.put("email",email);
        mapUsuario.put("alianza","");
        db.collection("Usuarios").document(fAuth.getUid()).set(mapUsuario);
    }

    public void asignarPlaneta(FirebaseFirestore db){
        Random aleatorio = new Random();

        Map mapSistema = new HashMap<String,Object>();

        int sistemaPlaneta = aleatorio.nextInt(100);
        int posicionSistema = aleatorio.nextInt(10);

        mapSistema.put("id",idPlaneta);
        mapSistema.put("nombre","Planeta principal");
        mapSistema.put("posicion",posicionSistema);
        mapSistema.put("usuario",usuario);

        db.collection("Sistemas").document(String.valueOf(sistemaPlaneta)).collection("Planetas_Sistemas").document(String.valueOf(posicionSistema)).set(mapSistema);

        Map mapPlaneta = new HashMap<String,Object>();

        mapPlaneta.put("nombre","Planeta Principal");
        mapPlaneta.put("id",idPlaneta);
        mapPlaneta.put("metal",200);
        mapPlaneta.put("cristal",100);
        mapPlaneta.put("deuterio",50);
        mapPlaneta.put("energia",0);
        mapPlaneta.put("capacidadMetal",10000);
        mapPlaneta.put("capacidadCristal",10000);
        mapPlaneta.put("capacidadDeuterio",10000);
        mapPlaneta.put("sistema", sistemaPlaneta);
        mapPlaneta.put("posicion",posicionSistema);
        mapPlaneta.put("diametro",12000);
        mapPlaneta.put("camposUsados",0);
        mapPlaneta.put("camposTotales",120);
        mapPlaneta.put("temperaturaMinima",0);
        mapPlaneta.put("temperaturaMaxima",40);
        mapPlaneta.put("colonia", false);

        db.collection("Planetas").document(fAuth.getUid()).collection("Planetas_Jugador").document(idPlaneta).set(mapPlaneta);
    }

    public void inicializarRecursosPlaneta(FirebaseFirestore db){
        Map mapRecursos = new HashMap<String,Object>();

        String[] recursos = {
                "Mina de metal",
                "Mina de cristal",
                "Sintetizador de deuterio",
                "Planta de energia solar",
                "Almacen de metal",
                "Almacen de cristal",
                "Almacen de deuterio",
        };

        Integer[] costeMetal = {60, 48, 225, 75, 1000, 0, 0};

        Integer[] costeCristal = {15, 24, 75, 75, 1000, 500, 0};

        Integer[] costeDeuterio = {60, 48, 225, 75, 1000, 0, 0};

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

    public void inicializarInstalacionesPlaneta(FirebaseFirestore db){
        Map mapInstalaciones = new HashMap<String,Object>();

        String[] instalaciones = {
                "Fabrica de nanobots",
                "Hangar",
                "Laboratorio de investigacion",
                "Fabrica de robots",
                "Terraformer",
                "Sensor phalanx",
        };

        Integer[] costeMetal = {1000000, 400, 200, 400, 0, 20000};

        Integer[] costeCristal = {500000 ,200, 400, 120, 50000, 40000};

        Integer[] costeDeuterio = {100000 ,100, 200, 225, 100000, 20000};

        Integer[] costeEnergia = {0, 0, 0, 0, 0, 0, 1000};

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

    public void inicializarInvestigacionesJugador(FirebaseFirestore db){
        Map mapInvestigaciones = new HashMap<String,Object>();

        String[] investigaciones = {
                "Tecnologia de energia",
                "Tecnologia laser",
                "Tecnologia ionica",
                "Tecnologia de plasma",
                "Motor de combustion",
                "Motor de impulso",
                "Motor hiperespacial",
                "Colonizacion",
                "Graviton",
                "Tecnologia de ataque",
                "Tecnologia de defensa",
                "Tecnologia de blindaje"
        };

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
            db.collection("Investigaciones_Jugador").document(fAuth.getUid()).collection("Investigaciones_Jugador").document(investigaciones[i]).set(mapInvestigaciones);
        }
    }

    public void inicializarNavesPlaneta(FirebaseFirestore db){
        Map mapNaves = new HashMap<String,Object>();

        String[] naves = {
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
        };

        Integer[] costeMetal = {2000, 6000, 10000, 3000, 6000, 20000, 45000, 30000, 60000, 10000, 4000, 800, 5000000};

        Integer[] costeCristal = {2000, 6000, 20000, 1000, 4000, 7000, 15000, 40000, 50000, 20000, 8000, 4000000};

        Integer[] costeDeuterio = {0, 0, 10000, 0, 0, 2000, 0, 15000, 15000, 15000, 1000000};

        Integer[] costeEnergia = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        Integer[] cantidad = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};


        for (int i = 0; i < naves.length; i++){
            mapNaves.put("nombre",naves[i]);
            mapNaves.put("costeMetal",costeMetal[i]);
            mapNaves.put("costeCristal",costeCristal[i]);
            mapNaves.put("costeDeuterio",costeDeuterio[i]);
            mapNaves.put("costeEnergia",costeEnergia[i]);
            mapNaves.put("cantidad",cantidad[i]);

            db.collection("Naves_Jugador").document(idPlaneta).collection("Naves_Planeta").document(naves[i]).set(mapNaves);
        }
    }

    public void inicializarDefensasPlaneta(FirebaseFirestore db){
        Map mapDefensas = new HashMap<String,Object>();

        String[] defensas = {
                "Lanzamisiles",
                "Laser pequeño",
                "Laser grande",
                "Cañon gauss",
                "Cañon ionico",
                "Cañon de plasma"
        };

        Integer[] costeMetal = {2000, 1500, 6000, 20000, 5000, 50000};

        Integer[] costeCristal = {0, 500, 2000, 15000, 3000, 50000};

        Integer[] costeDeuterio = {0, 0, 0, 2000, 0, 30000};

        Integer[] costeEnergia = {0, 0, 0, 0, 0, 0};

        Integer[] cantidad = {0, 0, 0, 0, 0, 0};

        for (int i = 0; i < defensas.length; i++){
            mapDefensas.put("nombre",defensas[i]);
            mapDefensas.put("costeMetal",costeMetal[i]);
            mapDefensas.put("costeCristal",costeCristal[i]);
            mapDefensas.put("costeDeuterio",costeDeuterio[i]);
            mapDefensas.put("costeEnergia",costeEnergia[i]);
            mapDefensas.put("cantidad",cantidad[i]);

            db.collection("Defensas_Jugador").document(idPlaneta).collection("Defensas_Planeta").document(defensas[i]).set(mapDefensas);
        }
    }
}