package com.example.appfutbol

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.LinearLayout
import android.content.Intent

class LigasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.ligas_lista)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ligas)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurar los listeners de los botones
        setupButtonListeners()
    }

    private fun setupButtonListeners() {
        // Botones de logos
        val btnLogo1: LinearLayout = findViewById(R.id.btnLogo1)
        val btnLogo2: LinearLayout = findViewById(R.id.btnLogo2)
        val btnLogo3: LinearLayout = findViewById(R.id.btnLogo3)
        val btnLogo4: LinearLayout = findViewById(R.id.btnLogo4)

        // Botón Volver
        val btnVolver: Button = findViewById(R.id.btnVolver)

        btnLogo1.setOnClickListener {
            // Acción para el primer logo (Partidos Recientes)
            val intent = Intent(this, ListaActivity::class.java)
            startActivity(intent)
        }

        btnLogo2.setOnClickListener {
            // Acción para el segundo logo (Próximos Partidos)
            // Cambia por la actividad correspondiente
            // val intent = Intent(this, ProximosPartidosActivity::class.java)
            // startActivity(intent)
        }

        btnLogo3.setOnClickListener {
            // Acción para el tercer logo (Tabla de Posiciones)
            // Cambia por la actividad correspondiente
            // val intent = Intent(this, TablaActivity::class.java)
            // startActivity(intent)
        }

        btnLogo4.setOnClickListener {
            // Acción para el cuarto logo (Goleadores)
            // Cambia por la actividad correspondiente
            // val intent = Intent(this, GoleadoresActivity::class.java)
            // startActivity(intent)
        }

        btnVolver.setOnClickListener {
            // Volver a la actividad anterior
            finish()
        }
    }
}