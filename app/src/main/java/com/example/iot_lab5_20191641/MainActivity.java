package com.example.iot_lab5_20191641;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button usuarios,comidas;
    private ImageView notis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        usuarios = findViewById(R.id.usuarios);
        comidas = findViewById(R.id.comidas);
        notis = findViewById(R.id.notis);

        usuarios.setOnClickListener(v -> {
            Intent reportMesIntent = new Intent(MainActivity.this, PantallaPerfil.class);
            startActivity(reportMesIntent);
        });

        comidas.setOnClickListener(v -> {
            Intent reportPlatoIntent = new Intent(MainActivity.this, Comidas.class);
            startActivity(reportPlatoIntent);
        });

        notis.setOnClickListener(v -> {
            Intent adminIntent = new Intent(MainActivity.this, Notificaciones.class);
            startActivity(adminIntent);
        });


    }
}