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

    // Botones de opciones
    lateinit var btnPartidosRecientes: LinearLayout
    lateinit var btnProximosPartidos: LinearLayout
    lateinit var btnTabla: LinearLayout
    lateinit var btnGoleadores: LinearLayout
    // Botón Volver
    lateinit var btnVolver: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lista) // Asegúrate de que el nombre del layout coincida
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lista)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupButtonListeners()

    }
    private fun initViews() {

        // Botones de opciones
        btnPartidosRecientes = findViewById(R.id.btnPartidosRecientes)
        btnProximosPartidos= findViewById(R.id.btnProximosPartidos)
        btnTabla= findViewById(R.id.btnTabla)
        btnGoleadores= findViewById(R.id.btnGoleadores)
        // Botón Volver
        btnVolver= findViewById(R.id.btnVolver)

    }
    private fun setupButtonListeners() {

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