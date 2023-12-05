package cl.gbrocal.desborde;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    // Duración del splash screen en milisegundos
    private static final int DURACION_SPLASH = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Utiliza un Handler para ejecutar una tarea después del tiempo especificado
        new Handler().postDelayed(new Runnable() {
            public void run() {
                // Se ejecutará una vez transcurridos 1.5 segundos (1500 milisegundos)
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Cierra la actividad actual para que el usuario no pueda volver al splash
            }

            ;
        }, DURACION_SPLASH);
    }
}
