package com.example.iot_lab5_20191641;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class Comidas extends AppCompatActivity {

    String channelId = "channelDefaultPri";
    private ImageView back2;
    private TextInputEditText textFieldNombreComida, textFieldCalorias;
    private CheckBox comida1, comida2, comida3, comida4, comida5;
    private Button buttonRegistrarComida, buttonRegistrarPredeterminada;
    private TextView totalCalorias, caloriasRecomendadas;
    private int totalCaloriasConsumidas = 0;
    private float caloriasNecesarias = 0;


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

        createNotificationChannel2();

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
        caloriasNecesarias = sharedPreferences.getFloat("calorias_diarias_necesarias", 0);
        caloriasRecomendadas.setText(String.valueOf(caloriasNecesarias));

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

    public void createNotificationChannel2() {
        //android.os.Build.VERSION_CODES.O == 26
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Canal notificaciones default",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Canal para notificaciones con prioridad default");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            askPermission();
        }
    }

    public void askPermission(){
        //android.os.Build.VERSION_CODES.TIRAMISU == 33
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) ==
                        PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(Comidas.this,
                    new String[]{POST_NOTIFICATIONS},
                    101);
        }
    }




    private void agregarComida() {
        String caloriasStr = textFieldCalorias.getText().toString();

        int calorias = Integer.parseInt(caloriasStr);
        totalCaloriasConsumidas += calorias;
        totalCalorias.setText(String.valueOf(totalCaloriasConsumidas));
        SharedPreferences sharedPreferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("total_calorias_consumidas", totalCaloriasConsumidas);
        editor.apply();

        if (totalCaloriasConsumidas > caloriasNecesarias) {
            showNotification((int) (totalCaloriasConsumidas - caloriasNecesarias));
        }
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

        SharedPreferences sharedPreferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("total_calorias_consumidas", totalCaloriasConsumidas);
        editor.apply();

        if (totalCaloriasConsumidas > caloriasNecesarias) {
            showNotification((int) (totalCaloriasConsumidas - caloriasNecesarias));
        }
    }

    private void showNotification(int calorias) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_logo)
                .setContentTitle("Alerta de calorías")
                .setContentText("Has registrado un exceso en la cantidad de calorías diarias de " + calorias + " calorías.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(1, builder.build());
        }
    }

}