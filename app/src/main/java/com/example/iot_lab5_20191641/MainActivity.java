package com.example.iot_lab5_20191641;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private Button usuarios,comidas,buttonRegistrarActividad;
    private ImageView notis;
    private ProgressBar progressBar;
    private TextView caloriasRestantes, textView6;
    private TextInputEditText textInputEditTextCalorias, textInputEditTextActividad;

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
        progressBar = findViewById(R.id.progressBar);
        caloriasRestantes = findViewById(R.id.caloriasRestantes);
        textInputEditTextCalorias = findViewById(R.id.textFieldAltura).findViewById(com.google.android.material.R.id.textinput_placeholder);
        textInputEditTextActividad = findViewById(R.id.textFieldPeso).findViewById(com.google.android.material.R.id.textinput_placeholder);
        buttonRegistrarActividad = findViewById(R.id.buttonRegistrarActividad);

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

        // Igualmente le pregunté a chatgpt sobre el uso de sharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
        final float caloriasNecesarias = sharedPreferences.getFloat("calorias_diarias_necesarias", 0);
        final int totalCaloriasConsumidas = sharedPreferences.getInt("total_calorias_consumidas", 0);
        final int totalCaloriasQuemadas = sharedPreferences.getInt("total_calorias_quemadas", 0);

        int caloriasRestantesValue = Math.round(caloriasNecesarias - totalCaloriasConsumidas);
        caloriasRestantes.setText("Calorías Restantes: " + caloriasRestantesValue);
        textView6.setText(String.valueOf(caloriasRestantesValue));

        int progreso = (int) ((totalCaloriasConsumidas / caloriasNecesarias) * 100);
        progressBar.setProgress(progreso);

        //le pregunté a chatgpt para ver cual era el error y era ponerle el final y hacer unos cambios, como poniendo el newTotalCaloriasQuemadas
        buttonRegistrarActividad.setOnClickListener(v -> {
            String caloriasQuemadasStr = textInputEditTextCalorias.getText().toString();
            int caloriasQuemadas = Integer.parseInt(caloriasQuemadasStr);
            int newTotalCaloriasQuemadas = totalCaloriasQuemadas + caloriasQuemadas;

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("total_calorias_quemadas", newTotalCaloriasQuemadas);
            editor.apply();

            // Recalculate remaining calories and update UI
            int newCaloriasRestantesValue = Math.round(caloriasNecesarias - (totalCaloriasConsumidas - newTotalCaloriasQuemadas));
            caloriasRestantes.setText("Calorías Restantes: " + newCaloriasRestantesValue);
            textView6.setText(String.valueOf(newCaloriasRestantesValue));

            int newProgreso = 0;
            if (caloriasNecesarias > 0) {
                newProgreso = (int) (((totalCaloriasConsumidas - newTotalCaloriasQuemadas) / caloriasNecesarias) * 100);
            }
            progressBar.setProgress(newProgreso);

        });

    }
}