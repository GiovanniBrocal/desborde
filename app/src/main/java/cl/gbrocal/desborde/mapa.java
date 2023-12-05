package cl.gbrocal.desborde;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class mapa extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    EditText txtLatitud, txtLongitud;
    GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button btnIrAMain = findViewById(R.id.btninicio);
        btnIrAMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear una intención para abrir la actividad principal
                Intent intent = new Intent(mapa.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Obtén la ubicación seleccionada de los extras
        String ubicacionSeleccionada = getIntent().getStringExtra("UBICACION_SELECCIONADA");

        // Ahora puedes usar la ubicación seleccionada como desees
        // Por ejemplo, mostrar un mensaje con la ubicación
        Toast.makeText(this, "Mostrando ubicación: " + ubicacionSeleccionada, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        this.mMap.setOnMapClickListener(this);
        this.mMap.setOnMapLongClickListener(this);

        // Obtén las coordenadas correspondientes a la ubicación seleccionada
        String ubicacionSeleccionada = getIntent().getStringExtra("UBICACION_SELECCIONADA");
        LatLng ubicacion = obtenerCoordenadas(ubicacionSeleccionada);

        // Muestra un marcador en la ubicación seleccionada
        mMap.addMarker(new MarkerOptions().position(ubicacion).title(ubicacionSeleccionada));

        // Haz zoom a la ubicación seleccionada
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15)); // Ajusta el nivel de zoom según sea necesario
    }

    // ...

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        // No realizar ninguna acción al hacer clic en el mapa
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        txtLatitud.setText(String.valueOf(latLng.latitude));
        txtLongitud.setText(String.valueOf(latLng.longitude));
    }

// ...


    // Esta función podría expandirse para manejar más ubicaciones según sea necesario
    private LatLng obtenerCoordenadas(String ubicacion) {
        switch (ubicacion) {
            case "Puente el Saque":
                return new LatLng(-36.6317548,-72.0822231);
            case "Puente Ñuble":
                return new LatLng(-36.5510437,-72.0931961);
            case "Puente el Ala":
                return new LatLng(-36.5753645,-72.2133832);
            default:
                // Ubicación por defecto
                return new LatLng(-36.6060317,-72.1048425);
        }
    }
}
