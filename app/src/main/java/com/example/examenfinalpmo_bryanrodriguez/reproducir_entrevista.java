package com.example.examenfinalpmo_bryanrodriguez;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.examenfinalpmo_bryanrodriguez.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import android.media.MediaPlayer;
import java.io.IOException;

public class reproducir_entrevista extends AppCompatActivity {
    FirebaseStorage storage;
    StorageReference storageReference;

    ImageView imageView;
    Button button;
    String descripcion, periodista;

    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private final Handler handler = new Handler();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproducir_entrevista);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        Intent intent = getIntent();
        periodista = intent.getStringExtra("Periodis");
        descripcion = intent.getStringExtra("Descrip");

        button = findViewById(R.id.button5);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(reproducir_entrevista.this, subir_entrevista.class);

                startActivity(intent);
            }
        });

        // Inicializar MediaPlayer
        mediaPlayer = new MediaPlayer();

        // Inicializar la SeekBar
        seekBar = findViewById(R.id.seekBar);

        // Configurar el Listener para la SeekBar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // No se necesita implementar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // No se necesita implementar
            }
        });
        cargarImagenDesdeFirebaseStorage();
        reproducirAudioDesdeFirebaseStorage();
        // Actualizar la posición de la SeekBar en intervalos regulares
        handler.postDelayed(updateSeekBarRunnable, 1000); // Actualiza cada segundo


    }

    // Runnable para actualizar la posición de la SeekBar
    private final Runnable updateSeekBarRunnable = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null) {
                int currentPosition = mediaPlayer.getCurrentPosition();
                seekBar.setProgress(currentPosition);
            }
            // Programar la actualización nuevamente después de un intervalo
            handler.postDelayed(this, 1000);  // Actualiza cada segundo
        }
    };

    public void cargarImagenDesdeFirebaseStorage() {
        // Tu código para cargar la imagen desde Firebase Storage
    }

    private void cargarImagenConGlide(Uri uri) {
        // Tu código para cargar la imagen con Glide
    }

    private void reproducirAudioDesdeFirebaseStorage() {

        storageReference = FirebaseStorage.getInstance().getReference().child("audios/" + "Entrevista TEDx de ingenieria" + ".mp3");

        // Descargar la URL del archivo de audio desde Firebase Storage
        storageReference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Una vez descargado, configura el MediaPlayer para reproducir el archivo
                        try {
                            mediaPlayer.setDataSource(uri.toString());
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(reproducir_entrevista.this, "Error al reproducir el audio", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(reproducir_entrevista.this, "Error al descargar el archivo de audio", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Detener la actualización de la SeekBar al destruir la actividad
        handler.removeCallbacks(updateSeekBarRunnable);

        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
