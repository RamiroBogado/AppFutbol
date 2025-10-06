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

class LigasActivity : AppCompatActivity() {

    lateinit var btnLogo1: LinearLayout
    lateinit var btnLogo2: LinearLayout
    lateinit var btnLogo3: LinearLayout
    lateinit var btnLogo4: LinearLayout

    lateinit var btnVolver: Button
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.ligas_lista)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ligas)) { v, insets ->
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

        btnLogo1 = findViewById(R.id.btnLogo1)
        btnLogo2 = findViewById(R.id.btnLogo2)
        btnLogo3 = findViewById(R.id.btnLogo3)
        btnLogo4 = findViewById(R.id.btnLogo4)

        btnVolver = findViewById(R.id.btnVolver)

    }
    private fun setupButtonListeners() {

        btnLogo1.setOnClickListener {
            // Premier League
            val intent = Intent(this, ListaActivity::class.java).apply {
                putExtra("COMPETITION", "PL")
            }
            startActivity(intent)
        }

        btnLogo2.setOnClickListener {
            // Bundesliga
            val intent = Intent(this, ListaActivity::class.java).apply {
                putExtra("COMPETITION", "BL1")
            }
            startActivity(intent)
        }

        btnLogo3.setOnClickListener {
            // Serie A
            val intent = Intent(this, ListaActivity::class.java).apply {
                putExtra("COMPETITION", "SA")
            }
            startActivity(intent)
        }

        btnLogo4.setOnClickListener {
            // La Liga
            val intent = Intent(this, ListaActivity::class.java).apply {
                putExtra("COMPETITION", "PD")
            }
            startActivity(intent)
        }

        btnVolver.setOnClickListener {
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
        val listadoItem1 = menu.findItem(R.id.item_listado_ligas)
        listadoItem1?.isVisible = false
        val listadoItem2 = menu.findItem(R.id.item_listado_lista)
        listadoItem2?.isVisible = false
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