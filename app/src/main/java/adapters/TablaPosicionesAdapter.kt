package com.example.appfutbol.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appfutbol.R
import com.example.appfutbol.models.EquipoPosicion

class TablaPosicionesAdapter(
    private var equipos: MutableList<EquipoPosicion>
) : RecyclerView.Adapter<TablaPosicionesAdapter.EquipoViewHolder>() {

    // Método para actualizar los datos
    fun actualizarEquipos(nuevosEquipos: List<EquipoPosicion>) {
        equipos.clear()
        equipos.addAll(nuevosEquipos)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tabla_posicion, parent, false)
        return EquipoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EquipoViewHolder, position: Int) {
        holder.bind(equipos[position])
    }

    override fun getItemCount() = equipos.size

    class EquipoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvPosicion: TextView = itemView.findViewById(R.id.tvPosicion)
        private val tvEquipo: TextView = itemView.findViewById(R.id.tvEquipo)
        private val tvPJ: TextView = itemView.findViewById(R.id.tvPJ)
        private val tvG: TextView = itemView.findViewById(R.id.tvG)
        private val tvE: TextView = itemView.findViewById(R.id.tvE)
        private val tvP: TextView = itemView.findViewById(R.id.tvP)
        private val tvPuntos: TextView = itemView.findViewById(R.id.tvPuntos)

        fun bind(equipo: EquipoPosicion) {
            tvPosicion.text = equipo.posicion.toString()
            tvEquipo.text = equipo.nombreEquipo
            tvPJ.text = equipo.partidosJugados.toString()
            tvG.text = equipo.ganados.toString()
            tvE.text = equipo.empatados.toString()
            tvP.text = equipo.perdidos.toString()
            tvPuntos.text = equipo.puntos.toString()
        }
    }
}