package cl.gbrocal.desborde;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final long TIEMPO_ENTRE_ACTUALIZACIONES = 3000; // 3 segundos
    private Handler handler;
    private DatabaseReference databaseReference;
    private TextView txtDistancia;
    private Spinner spinnerUbicaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar Firebase
        FirebaseApp.initializeApp(this);

        // Obtener referencia a la base de datos de Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("distancia");

        txtDistancia = findViewById(R.id.txtdistancia);
        spinnerUbicaciones = findViewById(R.id.spinnerUbicacion);

        // Configurar el adaptador para el spinner con las ubicaciones
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.ubicaciones_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUbicaciones.setAdapter(adapter);

        // Manejar la selección del spinner
        spinnerUbicaciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Cuando se selecciona una ubicación, inicia la actualización periódica
                iniciarActualizacionPeriodica();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Hacer algo aquí si nada está seleccionado (opcional)
            }
        });

        Button btnMapa = findViewById(R.id.btnmapa);
        btnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtén la ubicación seleccionada
                String ubicacionSeleccionada = spinnerUbicaciones.getSelectedItem().toString();

                // Crear una intención para abrir la actividad con el diseño "activity_mapa.xml"
                Intent intent = new Intent(MainActivity.this, mapa.class);

                // Pasar la ubicación seleccionada a la actividad del mapa
                intent.putExtra("UBICACION_SELECCIONADA", ubicacionSeleccionada);

                startActivity(intent);
            }
        });
    }

    private void iniciarActualizacionPeriodica() {
        // Detener actualizaciones anteriores si las hay
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }

        // Inicializar el handler para la actualización periódica
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                cambiarNumero();
                handler.postDelayed(this, TIEMPO_ENTRE_ACTUALIZACIONES);
            }
        }, TIEMPO_ENTRE_ACTUALIZACIONES);
    }

    // Crea una clase para almacenar los datos que deseas enviar a Firebase
    public class DatosFirebase {
        private String distancia;
        private String ubicacion;

        public DatosFirebase(String distancia, String ubicacion) {
            this.distancia = distancia;
            this.ubicacion = ubicacion;
        }

        public String getDistancia() {
            return distancia;
        }

        public String getUbicacion() {
            return ubicacion;
        }
    }


    private void cambiarNumero() {
        // Generar un número decimal aleatorio entre 1 y 5
        double numeroAleatorio = 1 + (new Random().nextDouble() * 4);

        // Formatear el número como texto y agregar "m" al final
        String distancia = String.format("%.2f", numeroAleatorio) + "m";

        // Obtén la ubicación seleccionada
        String ubicacionSeleccionada = spinnerUbicaciones.getSelectedItem().toString();

        // Crear un objeto DatosFirebase con distancia y ubicación
        DatosFirebase datosFirebase = new DatosFirebase(distancia, ubicacionSeleccionada);
        // Mostrar el número en el TextView
        txtDistancia.setText(distancia);

        // Enviar los datos a la base de datos de Firebase
        databaseReference.setValue(datosFirebase);
    }

}
