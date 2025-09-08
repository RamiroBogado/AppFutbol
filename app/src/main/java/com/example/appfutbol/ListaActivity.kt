package com.example.appfutbol

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.LinearLayout
import android.content.Intent

class ListaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lista) // Asegúrate de que el nombre del layout coincida
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lista)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurar los listeners de los botones
        setupButtonListeners()
    }

    private fun setupButtonListeners() {
        // Botones de opciones
        val btnPartidosRecientes: LinearLayout = findViewById(R.id.btnPartidosRecientes)
        val btnProximosPartidos: LinearLayout = findViewById(R.id.btnProximosPartidos)
        val btnTabla: LinearLayout = findViewById(R.id.btnTabla)
        val btnGoleadores: LinearLayout = findViewById(R.id.btnGoleadores)

        // Botón Volver
        val btnVolver: Button = findViewById(R.id.btnVolver)

        btnPartidosRecientes.setOnClickListener {
            // Navegar a Partidos Recientes
            val intent = Intent(this, PartidosRecientesActivity::class.java)
            startActivity(intent)
        }

        btnProximosPartidos.setOnClickListener {
            // Navegar a Próximos Partidos
            // val intent = Intent(this, ProximosPartidosActivity::class.java)
            // startActivity(intent)
        }

        btnTabla.setOnClickListener {
            // Navegar a Tabla de Posiciones
            // val intent = Intent(this, TablaActivity::class.java)
            // startActivity(intent)
        }

        btnGoleadores.setOnClickListener {
            // Navegar a Goleadores
            // val intent = Intent(this, GoleadoresActivity::class.java)
            // startActivity(intent)
        }

        btnVolver.setOnClickListener {
            // Volver a la actividad anterior
            finish()
        }
    }
}