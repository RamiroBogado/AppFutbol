package com.example.appfutbol

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import androidx.appcompat.widget.Toolbar

class PartidosRecientesActivity : AppCompatActivity() {

    private lateinit var rvPartidos: RecyclerView
    private lateinit var partidoAdapter: PartidoAdapter
    private lateinit var btnVolver: Button

    lateinit var toolbar : Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_partidos_recientes)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setUpToolBar()
        setupRecyclerView()
        setupButtonVolver()
    }

    private fun setUpToolBar(){

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = resources.getString(R.string.titulo)

    }

    private fun setupRecyclerView() {
        rvPartidos = findViewById(R.id.rvPartidos)
        rvPartidos.layoutManager = LinearLayoutManager(this)

        val partidos = getPartidosPremierLeague()
        partidoAdapter = PartidoAdapter(partidos, this)
        rvPartidos.adapter = partidoAdapter
    }

    private fun setupButtonVolver() {
        btnVolver = findViewById(R.id.btnVolver)
        btnVolver.setOnClickListener {
            finish()
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

            R.id.item_listado_lista -> {
                val intent = Intent(this, ListaActivity::class.java)
                startActivity(intent)
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getPartidosPremierLeague(): MutableList<Partido> {
        return mutableListOf(
            Partido(1, "2025/09/28", "15:00", "Manchester City", "Liverpool", "2 - 1"),
            Partido(2, "2025/09/28", "12:30", "Arsenal", "Chelsea", "3 - 0"),
            Partido(3, "2025/09/27", "15:00", "Manchester United", "Tottenham", "1 - 1"),
            Partido(4, "2025/09/27", "12:30", "Newcastle", "Aston Villa", "2 - 3"),
            Partido(5, "2025/09/26", "20:00", "Brighton", "West Ham", "4 - 2"),
            Partido(6, "2025/09/26", "17:30", "Brentford", "Wolverhampton", "0 - 0"),
            Partido(7, "2025/09/25", "19:45", "Crystal Palace", "Fulham", "1 - 0"),
            Partido(8, "2025/09/25", "17:00", "Everton", "Nottingham Forest", "2 - 1"),
            Partido(9, "2025/09/24", "20:00", "Burnley", "Sheffield United", "3 - 1"),
            Partido(10, "2025/09/24", "18:00", "Bournemouth", "Luton Town", "2 - 2")
        )
    }
}