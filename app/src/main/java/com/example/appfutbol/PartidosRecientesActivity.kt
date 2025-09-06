package com.example.appfutbol

import Partido
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView

class PartidosRecientesActivity : AppCompatActivity() {
    lateinit var rvPartidos: RecyclerView
    lateinit var partidosAdapter: PartidoAdapter
    lateinit var btnVolver: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_listado_partidos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.listado)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rvPartidos = findViewById(R.id.rvPartidos)
        btnVolver = findViewById(R.id.btnVolver)

        partidosAdapter = PartidoAdapter(getPartidos())
        rvPartidos.adapter = partidosAdapter

        // Configurar el botón volver
        btnVolver.setOnClickListener {
            finish() // Cierra esta actividad y vuelve a la anterior
        }
    }

    private fun getPartidos(): MutableList<Partido> {
        val partidos: MutableList<Partido> = ArrayList()

        partidos.add(Partido("2025/09/31", "15:00", "Aston Villa FC", "Crystal Palace FC", "0 - 3"));
        partidos.add(Partido("2025/09/31", "12:30", "Liverpool FC", "Arsenal FC", "1 - 0"));
        partidos.add(Partido("2025/09/31", "10:00", "Bigiston & Home A", "Manchester City F", "2 - 1"));
        partidos.add(Partido("2025/09/31", "10:00", "Nottingham Forest", "West Ham United", "0 - 3"));
        partidos.add(Partido("2025/09/30", "13:30", "Leeds United FC", "Newcastle United", "0 - 0"));
        partidos.add(Partido("2025/09/30", "11:00", "Sunderland AFG", "Brentford FC", "2 - 1"));
        partidos.add(Partido("2025/09/30", "11:00", "Wolverhampton W.", "Everton FC", "2 - 2"));
        partidos.add(Partido("2025/09/30", "11:00", "Tottenham Hotspur", "AFG Boumenswill", "0 - 1"));
        partidos.add(Partido("2025/09/30", "11:00", "Marciceider United", "Burnley FC", "3 - 2"));
        partidos.add(Partido("2025/09/29", "16:00", "Chelsea FC", "Fulham FC", "2 - 0"));
        partidos.add(Partido("2025/09/29", "14:00", "Brighton & Hove A.", "Southampton FC", "1 - 1"));
        partidos.add(Partido("2025/09/29", "12:00", "Leicester City", "Watford FC", "3 - 1"));
        partidos.add(Partido("2025/09/28", "15:30", "Manchester United", "Leeds United", "2 - 0"));
        partidos.add(Partido("2025/09/28", "13:00", "Newcastle United", "Sheffield United", "1 - 0"));
        partidos.add(Partido("2025/09/28", "10:30", "Norwich City", "Aston Villa", "0 - 2"));


        return partidos
    }
}