package com.example.appfutbol.viewmodels

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appfutbol.Partido
import com.example.appfutbol.R

class PartidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    // Si tus IDs son diferentes, cámbialos aquí:
    private val tvFecha: TextView = itemView.findViewById(R.id.tv_fecha)
    private val tvHora: TextView = itemView.findViewById(R.id.tv_hora)
    private val tvEquipoLocal: TextView = itemView.findViewById(R.id.tv_equipo_local)
    private val tvEquipoVisitante: TextView = itemView.findViewById(R.id.tv_equipo_visitante)
    private val tvResultado: TextView = itemView.findViewById(R.id.tv_resultado)

    fun bind(partido: Partido) {
        tvFecha.text = partido.fecha
        tvHora.text = partido.hora
        tvEquipoLocal.text = partido.equipoLocal
        tvEquipoVisitante.text = partido.equipoVisitante
        tvResultado.text = partido.resultado
    }
}