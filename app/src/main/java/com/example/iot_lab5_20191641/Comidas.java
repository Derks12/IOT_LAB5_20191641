package com.example.iot_lab5_20191641;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class Comidas extends AppCompatActivity {

    private ImageView back2;
    private TextInputEditText textFieldNombreComida, textFieldCalorias;
    private CheckBox comida1, comida2, comida3, comida4, comida5;
    private Button buttonRegistrarComida, buttonRegistrarPredeterminada;
    private TextView totalCalorias, caloriasRecomendadas;
    private int totalCaloriasConsumidas = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_comidas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        back2 = findViewById(R.id.back2);
        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //misma situación que en pantalla perfil con el textinput
        textFieldNombreComida = findViewById(R.id.textFieldPeso).findViewById(com.google.android.material.R.id.textinput_placeholder);
        textFieldCalorias = findViewById(R.id.textFieldAltura).findViewById(com.google.android.material.R.id.textinput_placeholder);

        comida1 = findViewById(R.id.comida1);
        comida2 = findViewById(R.id.comida2);
        comida3 = findViewById(R.id.comida3);
        comida4 = findViewById(R.id.comida4);
        comida5 = findViewById(R.id.comida5);
        buttonRegistrarComida = findViewById(R.id.buttonRegistrarComida);
        buttonRegistrarPredeterminada = findViewById(R.id.buttonRegistrarPredeterminada);
        totalCalorias = findViewById(R.id.totalCalorias);
        caloriasRecomendadas = findViewById(R.id.caloriasRecomendadas);

        SharedPreferences sharedPreferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
        float dailyCaloricNeeds = sharedPreferences.getFloat("calorias_diarias_necesarias", 0);
        caloriasRecomendadas.setText(String.valueOf(dailyCaloricNeeds));

        buttonRegistrarComida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarComida();
            }
        });

        buttonRegistrarPredeterminada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarComidaHarcodeada();
            }
        });
    }

    private void agregarComida() {
        String caloriasStr = textFieldCalorias.getText().toString();

        int calorias = Integer.parseInt(caloriasStr);
        totalCaloriasConsumidas += calorias;
        totalCalorias.setText(String.valueOf(totalCaloriasConsumidas));
    }

    private void agregarComidaHarcodeada() {
        if (comida1.isChecked()) {
            totalCaloriasConsumidas += 100;
        }
        if (comida2.isChecked()) {
            totalCaloriasConsumidas += 200;
        }
        if (comida3.isChecked()) {
            totalCaloriasConsumidas += 240;
        }
        if (comida4.isChecked()) {
            totalCaloriasConsumidas += 130;
        }
        if (comida5.isChecked()) {
            totalCaloriasConsumidas += 155;
        }
        totalCalorias.setText(String.valueOf(totalCaloriasConsumidas));
    }

}