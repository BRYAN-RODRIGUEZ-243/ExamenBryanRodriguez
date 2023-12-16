package com.example.examenfinalpmo_bryanrodriguez;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1000);
                    Intent intent = new Intent(MainActivity.this, lista_de_entrevista.class);

                    // Iniciamos la ventana secundaria
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}