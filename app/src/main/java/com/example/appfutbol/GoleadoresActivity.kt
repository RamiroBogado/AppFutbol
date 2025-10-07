package com.example.appfutbol

import adapters.GoleadoresAdapter
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
import com.example.appfutbol.viewmodels.GoleadoresViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GoleadoresActivity : AppCompatActivity() {

    private lateinit var rvGoleadores: RecyclerView
    private lateinit var goleadoresAdapter: GoleadoresAdapter
    private lateinit var btnVolver: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var toolbar: Toolbar
    private val viewModel: GoleadoresViewModel by viewModels()

    private var currentCompetition: String = "PL" // Por defecto Premier League

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_goleadores)

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
        viewModel.cargarGoleadores(currentCompetition)
    }

    private fun initViews() {
        rvGoleadores = findViewById(R.id.rvGoleadores)
        btnVolver = findViewById(R.id.btnVolver)
        toolbar = findViewById(R.id.toolbar)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun setUpToolBar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Tabla de Goleadores"
    }

    private fun setupRecyclerView() {
        rvGoleadores.layoutManager = LinearLayoutManager(this)
        // Inicializa el adapter con lista vacía
        goleadoresAdapter = GoleadoresAdapter(mutableListOf())
        rvGoleadores.adapter = goleadoresAdapter
    }

    private fun setupButtonVolver() {
        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun setupObservers() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.goleadoresState.collect { state ->
                when (state) {
                    is GoleadoresViewModel.GoleadoresState.Loading -> {
                        progressBar.visibility = android.view.View.VISIBLE
                        rvGoleadores.visibility = android.view.View.GONE
                    }
                    is GoleadoresViewModel.GoleadoresState.Success -> {
                        progressBar.visibility = android.view.View.GONE
                        rvGoleadores.visibility = android.view.View.VISIBLE
                        goleadoresAdapter.actualizarGoleadores(state.goleadores)
                    }
                    is GoleadoresViewModel.GoleadoresState.Error -> {
                        progressBar.visibility = android.view.View.GONE
                        rvGoleadores.visibility = android.view.View.VISIBLE
                        Toast.makeText(this@GoleadoresActivity, "Error: ${state.mensaje}", Toast.LENGTH_LONG).show()
                        goleadoresAdapter.actualizarGoleadores(mutableListOf())
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