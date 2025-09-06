package com.example.appfutbol

import Partido
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PartidoAdapter(
    private val partidosList: MutableList<Partido>
) : RecyclerView.Adapter<PartidoAdapter.PartidoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartidoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_partido, parent, false)
        return PartidoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PartidoViewHolder, position: Int) {
        val partido = partidosList[position]

        holder.tvFecha.text = partido.fecha
        holder.tvHora.text = partido.hora
        holder.tvEquipoLocal.text = partido.equipoLocal
        holder.tvEquipoVisitante.text = partido.equipoVisitante
        holder.tvResultado.text = partido.resultado
    }

    override fun getItemCount(): Int = partidosList.size

    class PartidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvFecha: TextView = itemView.findViewById(R.id.tv_fecha)
        val tvHora: TextView = itemView.findViewById(R.id.tv_hora)
        val tvEquipoLocal: TextView = itemView.findViewById(R.id.tv_equipo_local)
        val tvEquipoVisitante: TextView = itemView.findViewById(R.id.tv_equipo_visitante)
        val tvResultado: TextView = itemView.findViewById(R.id.tv_resultado)
    }
}