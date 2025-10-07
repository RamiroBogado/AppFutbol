package com.example.appfutbol

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import com.example.appfutbol.adapters.TablaPosicionesAdapter
import com.example.appfutbol.viewmodels.TablaPosicionesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TablaPosicionesActivity : AppCompatActivity() {

    private lateinit var rvTablaPosiciones: RecyclerView
    private lateinit var tablaPosicionesAdapter: TablaPosicionesAdapter
    private lateinit var btnVolver: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var toolbar: Toolbar
    private val viewModel: TablaPosicionesViewModel by viewModels()
    private var currentCompetition: String = "PL" // Por defecto Premier League

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tabla_posiciones)

        // Recibir la competencia seleccionada
        currentCompetition = intent.getStringExtra("COMPETITION") ?: "PL"

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setUpToolBar()
        setupRecyclerView()
        setupButtonVolver()
        setupObservers()

        // Cargar datos reales con la competencia seleccionada
        viewModel.cargarTablaPosiciones(currentCompetition)
    }

    private fun initViews() {
        rvTablaPosiciones = findViewById(R.id.rvTablaPosiciones)
        btnVolver = findViewById(R.id.btnVolver)
        toolbar = findViewById(R.id.toolbar)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun setUpToolBar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Tabla de Posiciones"
    }

    private fun setupRecyclerView() {
        rvTablaPosiciones.layoutManager = LinearLayoutManager(this)
        // Inicializa el adapter con lista vacía
        tablaPosicionesAdapter = TablaPosicionesAdapter(mutableListOf())
        rvTablaPosiciones.adapter = tablaPosicionesAdapter
    }

    private fun setupButtonVolver() {
        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun setupObservers() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.tablaPosicionesState.collect { state ->
                when (state) {
                    is TablaPosicionesViewModel.TablaPosicionesState.Loading -> {
                        progressBar.visibility = android.view.View.VISIBLE
                        rvTablaPosiciones.visibility = android.view.View.GONE
                    }
                    is TablaPosicionesViewModel.TablaPosicionesState.Success -> {
                        progressBar.visibility = android.view.View.GONE
                        rvTablaPosiciones.visibility = android.view.View.VISIBLE
                        tablaPosicionesAdapter.actualizarEquipos(state.equipos)
                    }
                    is TablaPosicionesViewModel.TablaPosicionesState.Error -> {
                        progressBar.visibility = android.view.View.GONE
                        rvTablaPosiciones.visibility = android.view.View.VISIBLE
                        Toast.makeText(this@TablaPosicionesActivity, "Error: ${state.mensaje}", Toast.LENGTH_LONG).show()
                        tablaPosicionesAdapter.actualizarEquipos(mutableListOf())
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_logout -> {
                val intent = Intent(this, MainActivity::class.java)
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
            R.id.item_listado_lista -> {
                val intent = Intent(this, ListaActivity::class.java).apply {
                    putExtra("COMPETITION", currentCompetition)
                }
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}