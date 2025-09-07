package com.example.appfutbol

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.Button

class DetallePartidoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_partido)

        val id = intent.getIntExtra("id", 0)
        val fecha = intent.getStringExtra("fecha") ?: ""
        val hora = intent.getStringExtra("hora") ?: ""
        val equipoLocal = intent.getStringExtra("equipoLocal") ?: ""
        val equipoVisitante = intent.getStringExtra("equipoVisitante") ?: ""
        val resultado = intent.getStringExtra("resultado") ?: ""

        val partido = Partido(id, fecha, hora, equipoLocal, equipoVisitante, resultado)

        findViewById<TextView>(R.id.tvTitulo).text = "${partido.equipoLocal} vs ${partido.equipoVisitante}"
        findViewById<TextView>(R.id.tvResultado).text = partido.resultado
        findViewById<TextView>(R.id.tvFechaHora).text = "${partido.fecha} ${partido.hora}"

        val goles = obtenerGoles(partido.id)
        val golesText = StringBuilder()
        goles.forEach { gol ->
            golesText.append("${gol.minuto}' ${gol.equipo} - ${gol.jugador}\n")
        }
        findViewById<TextView>(R.id.tvGoles).text = golesText.toString()

        val tarjetas = obtenerTarjetas(partido.id)
        val tarjetasText = StringBuilder()
        tarjetas.forEach { tarjeta ->
            val emoji = if (tarjeta.color == "Roja") "🟥" else "🟨"
            tarjetasText.append("${tarjeta.minuto}' ${tarjeta.equipo} - ${tarjeta.jugador} $emoji\n")
        }
        findViewById<TextView>(R.id.tvTarjetas).text = tarjetasText.toString()

        findViewById<Button>(R.id.btnVolver).setOnClickListener {
            finish()
        }
    }

    private fun obtenerEstadisticas(partidoId: Int): String {
        return when (partidoId) {
            1 -> "Posesión: 58% - 42%\nTiros: 18 - 9\nTiros a puerta: 8 - 3\nFaltas: 10 - 14\nSaques de esquina: 7 - 4"
            2 -> "Posesión: 62% - 38%\nTiros: 15 - 5\nTiros a puerta: 7 - 1\nFaltas: 8 - 12\nSaques de esquina: 6 - 2"
            3 -> "Posesión: 45% - 55%\nTiros: 11 - 16\nTiros a puerta: 4 - 6\nFaltas: 13 - 9\nSaques de esquina: 3 - 8"
            4 -> "Posesión: 48% - 52%\nTiros: 13 - 15\nTiros a puerta: 5 - 7\nFaltas: 11 - 10\nSaques de esquina: 4 - 5"
            5 -> "Posesión: 54% - 46%\nTiros: 16 - 12\nTiros a puerta: 9 - 5\nFaltas: 9 - 13\nSaques de esquina: 7 - 3"
            6 -> "Posesión: 51% - 49%\nTiros: 10 - 11\nTiros a puerta: 3 - 4\nFaltas: 14 - 11\nSaques de esquina: 5 - 6"
            7 -> "Posesión: 47% - 53%\nTiros: 8 - 13\nTiros a puerta: 2 - 5\nFaltas: 12 - 8\nSaques de esquina: 2 - 7"
            8 -> "Posesión: 56% - 44%\nTiros: 14 - 8\nTiros a puerta: 6 - 3\nFaltas: 10 - 15\nSaques de esquina: 8 - 2"
            9 -> "Posesión: 60% - 40%\nTiros: 17 - 7\nTiros a puerta: 8 - 2\nFaltas: 7 - 16\nSaques de esquina: 9 - 1"
            10 -> "Posesión: 49% - 51%\nTiros: 12 - 14\nTiros a puerta: 5 - 6\nFaltas: 13 - 12\nSaques de esquina: 4 - 7"
            else -> "Estadísticas no disponibles"
        }
    }

    private fun obtenerGoles(partidoId: Int): List<Gol> {
        return when (partidoId) {
            1 -> listOf(
                Gol(23, "Manchester City", "Erling Haaland", 1),
                Gol(57, "Liverpool", "Mohamed Salah", 1),
                Gol(78, "Manchester City", "Kevin De Bruyne", 2)
            )
            2 -> listOf(
                Gol(34, "Arsenal", "Bukayo Saka", 1),
                Gol(61, "Arsenal", "Gabriel Jesus", 2),
                Gol(83, "Arsenal", "Martin Ødegaard", 3)
            )
            3 -> listOf(
                Gol(42, "Tottenham", "Son Heung-min", 1),
                Gol(67, "Manchester United", "Bruno Fernandes", 1)
            )
            4 -> listOf(
                Gol(18, "Aston Villa", "Ollie Watkins", 1),
                Gol(39, "Newcastle", "Alexander Isak", 1),
                Gol(52, "Aston Villa", "Leon Bailey", 2),
                Gol(64, "Newcastle", "Callum Wilson", 2),
                Gol(89, "Aston Villa", "John McGinn", 3)
            )
            5 -> listOf(
                Gol(12, "Brighton", "Evan Ferguson", 1),
                Gol(28, "West Ham", "Jarrod Bowen", 1),
                Gol(45, "Brighton", "João Pedro", 2),
                Gol(63, "West Ham", "Michail Antonio", 2),
                Gol(71, "Brighton", "Kaoru Mitoma", 3),
                Gol(86, "Brighton", "Pascal Groß", 4)
            )
            6 -> listOf(
                // Empate 0-0, no hay goles
            )
            7 -> listOf(
                Gol(76, "Crystal Palace", "Eberechi Eze", 1)
            )
            8 -> listOf(
                Gol(32, "Everton", "Dominic Calvert-Lewin", 1),
                Gol(58, "Nottingham Forest", "Taiwo Awoniyi", 1),
                Gol(81, "Everton", "Abdoulaye Doucouré", 2)
            )
            9 -> listOf(
                Gol(24, "Burnley", "Lyle Foster", 1),
                Gol(51, "Sheffield United", "Oliver McBurnie", 1),
                Gol(67, "Burnley", "Zeki Amdouni", 2),
                Gol(79, "Burnley", "Josh Brownhill", 3)
            )
            10 -> listOf(
                Gol(15, "Bournemouth", "Dominic Solanke", 1),
                Gol(38, "Luton Town", "Carlton Morris", 1),
                Gol(62, "Bournemouth", "Philip Billing", 2),
                Gol(88, "Luton Town", "Elijah Adebayo", 2)
            )
            else -> emptyList()
        }
    }

    private fun obtenerTarjetas(partidoId: Int): List<Tarjeta> {
        return when (partidoId) {
            1 -> listOf(
                Tarjeta(45, "Liverpool", "Virgil van Dijk", "Amarilla"),
                Tarjeta(72, "Manchester City", "Rodri", "Amarilla")
            )
            2 -> listOf(
                Tarjeta(28, "Chelsea", "Nicolas Jackson", "Amarilla"),
                Tarjeta(55, "Chelsea", "Moisés Caicedo", "Amarilla"),
                Tarjeta(77, "Chelsea", "Reece James", "Roja")
            )
            3 -> listOf(
                Tarjeta(34, "Tottenham", "Cristian Romero", "Amarilla"),
                Tarjeta(68, "Manchester United", "Casemiro", "Amarilla")
            )
            4 -> listOf(
                Tarjeta(42, "Newcastle", "Bruno Guimarães", "Amarilla"),
                Tarjeta(61, "Aston Villa", "Douglas Luiz", "Amarilla")
            )
            5 -> listOf(
                Tarjeta(25, "West Ham", "Edson Álvarez", "Amarilla"),
                Tarjeta(59, "Brighton", "Lewis Dunk", "Amarilla"),
                Tarjeta(83, "West Ham", "Lucas Paquetá", "Amarilla")
            )
            6 -> listOf(
                Tarjeta(47, "Brentford", "Christian Nørgaard", "Amarilla"),
                Tarjeta(71, "Wolverhampton", "Mario Lemina", "Amarilla")
            )
            7 -> listOf(
                Tarjeta(63, "Fulham", "João Palhinha", "Amarilla"),
                Tarjeta(85, "Crystal Palace", "Joachim Andersen", "Amarilla")
            )
            8 -> listOf(
                Tarjeta(29, "Nottingham Forest", "Orel Mangala", "Amarilla"),
                Tarjeta(74, "Everton", "James Tarkowski", "Amarilla")
            )
            9 -> listOf(
                Tarjeta(38, "Sheffield United", "Vinicius Souza", "Amarilla"),
                Tarjeta(65, "Burnley", "Josh Cullen", "Amarilla")
            )
            10 -> listOf(
                Tarjeta(52, "Bournemouth", "Adam Smith", "Amarilla"),
                Tarjeta(79, "Luton Town", "Ross Barkley", "Amarilla")
            )
            else -> emptyList()
        }
    }
}