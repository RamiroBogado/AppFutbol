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
import androidx.lifecycle.lifecycleScope
import com.example.appfutbol.adapters.PartidoAdapter
import com.example.appfutbol.viewmodels.PartidosViewModel
import kotlinx.coroutines.launch

class PartidosRecientesActivity : AppCompatActivity() {
    private lateinit var rvPartidos: RecyclerView
    private lateinit var partidoAdapter: PartidoAdapter
    private lateinit var btnVolver: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var toolbar: Toolbar
    private val viewModel: PartidosViewModel by viewModels()

    private var currentCompetition: String = "PL"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_partidos)

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
        viewModel.cargarPartidosRecientes(currentCompetition)
    }

    private fun initViews() {
        rvPartidos = findViewById(R.id.rvPartidos)
        btnVolver = findViewById(R.id.btnVolver)
        toolbar = findViewById(R.id.toolbar)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun setUpToolBar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = intent.getStringExtra("NOMBRE")
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true

        rvPartidos.layoutManager = layoutManager
        partidoAdapter = PartidoAdapter(mutableListOf())
        rvPartidos.adapter = partidoAdapter
    }

    private fun setupButtonVolver() {
        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.partidosState.collect { state ->
                when (state) {
                    is PartidosViewModel.PartidosState.Loading -> {
                        progressBar.visibility = android.view.View.VISIBLE
                        rvPartidos.visibility = android.view.View.GONE
                    }
                    is PartidosViewModel.PartidosState.Success -> {
                        progressBar.visibility = android.view.View.GONE
                        rvPartidos.visibility = android.view.View.VISIBLE
                        val partidosConvertidos = viewModel.convertirMatchesAPartidos(state.partidos)
                        partidoAdapter.actualizarPartidos(partidosConvertidos)
                    }
                    is PartidosViewModel.PartidosState.Error -> {
                        progressBar.visibility = android.view.View.GONE
                        rvPartidos.visibility = android.view.View.VISIBLE
                        Toast.makeText(this@PartidosRecientesActivity, "Error: ${state.mensaje}", Toast.LENGTH_LONG).show()
                        partidoAdapter.actualizarPartidos(mutableListOf())
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
                    putExtra("COMPETITION", currentCompetition) // Pasar la competencia actual
                }
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}