package com.carlos.stellarControl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.carlos.stellarControl.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainLogin extends AppCompatActivity {
    FirebaseAuth fAuth;
    FirebaseFirestore fFirestore;
    Query query;
    EditText etEmailLogin, etPasswordLogin;
    TextView tvForgotPass;
    Button btnLogin;
    Intent intent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fAuth = FirebaseAuth.getInstance();
        fFirestore = FirebaseFirestore.getInstance();

        etEmailLogin = (EditText)findViewById(R.id.etEmailLogin);
        etPasswordLogin = (EditText)findViewById(R.id.etPasswordLogin);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        tvForgotPass = (TextView) findViewById(R.id.tvForgotPass);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmailLogin.getText().toString().trim();
                String password = etPasswordLogin.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    etEmailLogin.setError(getString(R.string.emailRequired));
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    etPasswordLogin.setError(getString(R.string.passwordRequired));
                    return;
                }

                if(password.length() < 6){
                    etPasswordLogin.setError("Password Must be >= 6 Characters");
                }

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            intent = new Intent(MainLogin.this, MainGeneral.class);
                            intent.putExtra("anteriorActividad", "login");
                            //intent.putExtra("nombrePlaneta", Global.planetaSeleccionado);
                            //Toast.makeText(MainLogin.this, "Login correct", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainLogin.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


}