package com.carlos.stellarControl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.carlos.stellarControl.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private Button btnMainLogin, btnMainRegister;
    Intent intent;

    FirebaseAuth fAuth;
    FirebaseFirestore fFirestore;
    ImageView imgLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // usar whereArrayContains() para comprobar valores en un array

        fFirestore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        btnMainLogin = (Button) findViewById(R.id.btnMainLogin);
        btnMainRegister = (Button) findViewById(R.id.btnMainRegister);
        imgLogo = (ImageView) findViewById(R.id.imgLogo);

        btnMainLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(MainActivity.this, MainLogin.class);
                startActivity(intent);
            }
        });

        btnMainRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(MainActivity.this, MainRegistro.class);
                startActivity(intent);
            }
        });
    }
}