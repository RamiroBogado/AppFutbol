package com.example.appfutbol

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.LinearLayout
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar

class ListaActivity : AppCompatActivity() {

    // Botones de opciones
    lateinit var btnPartidosRecientes: LinearLayout
    lateinit var btnProximosPartidos: LinearLayout
    lateinit var btnTabla: LinearLayout
    lateinit var btnGoleadores: LinearLayout
    // Botón Volver
    lateinit var btnVolver: Button
    //toolbar
    lateinit var toolbar : Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lista) // Asegúrate de que el nombre del layout coincida
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lista)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setUpToolBar()
        initViews()
        setupButtonListeners()

    }

    private fun setUpToolBar(){

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = resources.getString(R.string.titulo)

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
            val intent = Intent(this, ProximosPartidosActivity::class.java)
            startActivity(intent)
        }

        btnTabla.setOnClickListener {
            // Navegar a Tabla de Posiciones
            val intent = Intent(this, TablaPosicionesActivity::class.java)
            startActivity(intent)
        }

        btnGoleadores.setOnClickListener {
            // Navegar a Goleadores
            val intent = Intent(this, GoleadoresActivity::class.java)
            startActivity(intent)
        }

        btnVolver.setOnClickListener {
            // Volver a la actividad anterior
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        super.onPrepareOptionsMenu(menu)
        // Ocultar ítem
        val listadoItem = menu.findItem(R.id.item_listado_lista)
        listadoItem?.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.item_logout -> {
                val intent = Intent(this, MainActivity::class.java)
                // Limpia el stack de actividades
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
                true
            }

            R.id.item_listado_ligas -> {
                val intent = Intent(this, LigasActivity::class.java)
                startActivity(intent)
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}