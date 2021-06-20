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

        etUserRegister = (EditText) findViewById(R.id.etUserRegister);
        etEmailRegister = (EditText) findViewById(R.id.etEmailRegister);
        etPassRegister = (EditText) findViewById(R.id.etPassRegister);
        btnRegister = (Button) findViewById(R.id.btnRegister);

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
                            Random aleatorio = new Random();
                            int sistemaPlaneta = aleatorio.nextInt(99 - 1 + 1) + 1;
                            int posicionSistema = aleatorio.nextInt(9 - 1 + 1) + 1;
                            idPlaneta = fFirestore.collection("Planetas").document(fAuth.getUid()).collection("Planetas_Jugador").document().getId();
                            crearUsuario();
                            Global.asignarPlaneta(fAuth, fFirestore, idPlaneta, "Planeta principal", 200, 100, 50, sistemaPlaneta, posicionSistema, false);
                            Global.inicializarRecursosPlaneta(fFirestore, idPlaneta);
                            Global.inicializarInstalacionesPlaneta(fFirestore, idPlaneta);
                            Global.inicializarInvestigacionesJugador(fAuth, fFirestore);
                            Global.inicializarNavesPlaneta(fFirestore, idPlaneta);
                            Global.inicializarDefensasPlaneta(fFirestore, idPlaneta);
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

    public void crearUsuario(){
        Map mapUsuario = new HashMap<String,Object>();
        mapUsuario.put("nombre",usuario);
        mapUsuario.put("email",email);
        mapUsuario.put("alianza","");
        fFirestore.collection("Usuarios").document(fAuth.getUid()).set(mapUsuario);
    }
}