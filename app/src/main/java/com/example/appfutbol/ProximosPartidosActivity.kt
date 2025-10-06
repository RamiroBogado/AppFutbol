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
import java.text.SimpleDateFormat
import java.util.*

class ProximosPartidosActivity : AppCompatActivity() {
    private lateinit var rvPartidos: RecyclerView
    private lateinit var partidoAdapter: PartidoAdapter
    private lateinit var btnVolver: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var toolbar: Toolbar
    private val viewModel: PartidosViewModel by viewModels()

    private var currentCompetition: String = "PL" // Por defecto Premier League

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_partidos_recientes) // Mismo layout

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
        viewModel.cargarProximosPartidos(currentCompetition)
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
        supportActionBar!!.title = "Próximos Partidos" // Cambiar título
    }

    private fun setupRecyclerView() {
        // Para próximos partidos, mostrar en orden normal (no reverso)
        rvPartidos.layoutManager = LinearLayoutManager(this)
        partidoAdapter = PartidoAdapter(mutableListOf(), this)
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

                        // DEBUG: SIN FILTRO - mostrar todos los partidos
                        println("DEBUG - Total partidos recibidos: ${state.partidos.size}")
                        state.partidos.forEachIndexed { index, match ->
                            println("DEBUG - Partido $index: ${match.homeTeam.name} vs ${match.awayTeam.name} - Fecha: ${match.utcDate}")
                        }

                        // SIN FILTRO: usar todos los partidos
                        val partidosConvertidos = convertirMatchesAPartidos(state.partidos)
                        partidoAdapter.actualizarPartidos(partidosConvertidos)

                        // Mostrar mensaje temporal
                        Toast.makeText(
                            this@ProximosPartidosActivity,
                            "Mostrando ${state.partidos.size} partidos (sin filtro)",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    is PartidosViewModel.PartidosState.Error -> {
                        progressBar.visibility = android.view.View.GONE
                        rvPartidos.visibility = android.view.View.VISIBLE
                        Toast.makeText(this@ProximosPartidosActivity, "Error: ${state.mensaje}", Toast.LENGTH_LONG).show()
                        partidoAdapter.actualizarPartidos(mutableListOf())
                    }
                }
            }
        }
    }

    // FUNCIÓN NUEVA: Filtrar solo partidos futuros
    private fun filtrarPartidosFuturos(matches: List<com.example.appfutbol.dtos.Match>): List<com.example.appfutbol.dtos.Match> {
        val ahora = Calendar.getInstance().time
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())

        return matches.filter { match ->
            try {
                val fechaPartido = inputFormat.parse(match.utcDate)
                fechaPartido != null && fechaPartido.after(ahora)
            } catch (e: Exception) {
                false
            }
        }.sortedBy { match -> // Ordenar por fecha más cercana
            inputFormat.parse(match.utcDate)
        }
    }

    // Función para convertir Match (DTO) a Partido
    private fun convertirMatchesAPartidos(matches: List<com.example.appfutbol.dtos.Match>): MutableList<Partido> {
        return matches.map { match ->
            Partido(
                id = match.id,
                fecha = formatearFecha(match.utcDate),
                hora = formatearHora(match.utcDate),
                equipoLocal = match.homeTeam.name,
                equipoVisitante = match.awayTeam.name,
                resultado = formatearResultado(match.score.fullTime) // Mostrar resultado real
            )
        }.toMutableList()
    }

    private fun formatearResultado(fullTime: com.example.appfutbol.dtos.FullTimeScore?): String {
        return if (fullTime?.home != null && fullTime.away != null) {
            "${fullTime.home} - ${fullTime.away}"
        } else {
            "VS"
        }
    }

    private fun formatearFecha(utcDate: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
            val date = inputFormat.parse(utcDate)
            date?.let { outputFormat.format(it) } ?: "Fecha no disponible"
        } catch (e: Exception) {
            "Fecha no disponible"
        }
    }

    private fun formatearHora(utcDate: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val date = inputFormat.parse(utcDate)
            date?.let { outputFormat.format(it) } ?: "Hora no disponible"
        } catch (e: Exception) {
            "Hora no disponible"
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
                val intent = Intent(this, PartidosRecientesActivity::class.java)
                startActivity(intent)
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}