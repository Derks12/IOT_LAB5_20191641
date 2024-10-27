package com.example.iot_lab5_20191641;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class PantallaPerfil extends AppCompatActivity {


    private ImageView back2;
    private TextInputEditText textFieldPeso, textFieldAltura, textFieldEdad;
    private RadioGroup radioGroupGender, radioGroupObjective;
    private Spinner spinnerActivityLevel;
    private Button buttonRegister;
    private TextView textViewCaloriesGoal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pantalla_perfil);
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
        //le consulté a chatgpt como sacar el text field para usarlo acá, el profe lo hizo de manera distinta en su app pero no cumplía con lo que se pide acá
        textFieldPeso = findViewById(R.id.textFieldPeso).findViewById(com.google.android.material.R.id.textinput_placeholder);
        textFieldAltura = findViewById(R.id.textFieldAltura).findViewById(com.google.android.material.R.id.textinput_placeholder);
        textFieldEdad = findViewById(R.id.textFieldEdad).findViewById(com.google.android.material.R.id.textinput_placeholder);
        radioGroupGender = findViewById(R.id.radioGroup);
        spinnerActivityLevel = findViewById(R.id.spinner);
        radioGroupObjective = findViewById(R.id.objetivo);
        buttonRegister = findViewById(R.id.buttonRegistrar);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularCaloriasTotal();
            }
        });
    }

    private void calcularCaloriasTotal() {
        String pesoStr = textFieldPeso.getText().toString();
        String alturaStr = textFieldAltura.getText().toString();
        String edadStr = textFieldEdad.getText().toString();

        int peso = Integer.parseInt(pesoStr);
        int altura = Integer.parseInt(alturaStr);
        int edad = Integer.parseInt(edadStr);

        int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
        RadioButton selectedGender = findViewById(selectedGenderId);
        String gender = selectedGender != null ? selectedGender.getText().toString() : "";

        double tmb;
        if (gender.equals("Hombre")) {
            tmb = (10 * peso) + (6.25 * altura) - (5 * edad) + 5;
        } else {
            tmb = (10 * peso) + (6.25 * altura) - (5 * edad) - 161;
        }

        String activityLevel = spinnerActivityLevel.getSelectedItem().toString();
        double factor;
        if (activityLevel.equals("Poco o ningun ejercicio")) {
            factor = 1.2;
        } else if (activityLevel.equals("Ejercicio ligero")) {
            factor = 1.375;
        } else if (activityLevel.equals("Ejercicio moderado")) {
            factor = 1.55;
        } else if (activityLevel.equals("Ejercicio fuerte")) {
            factor = 1.725;
        } else if (activityLevel.equals("Ejercicio muy fuerte")) {
            factor = 1.9;
        } else {
            factor = 1.0;
        }

        double caloriasNecesarias = tmb * factor;

        int selectedObjectiveId = radioGroupObjective.getCheckedRadioButtonId();
        RadioButton selectedObjective = findViewById(selectedObjectiveId);
        String objective = selectedObjective != null ? selectedObjective.getText().toString() : "";

        if (objective.equals("Subir Peso")) {
            caloriasNecesarias += 500;
        } else if (objective.equals("Bajar Peso")) {
            caloriasNecesarias -= 300;
        }else {
            caloriasNecesarias = caloriasNecesarias;
        }

        // acá le pregunté a chatgpt como mandar la data y me recomendó usar SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("calorias_diarias_necesarias", (float) caloriasNecesarias);
        editor.apply();
    }



}