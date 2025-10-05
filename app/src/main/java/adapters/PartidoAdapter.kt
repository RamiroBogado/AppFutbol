package com.example.appfutbol.adapters

import com.example.appfutbol.Partido
import com.example.appfutbol.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PartidoAdapter(
    private var partidos: MutableList<Partido>,
    private val context: Context
) : RecyclerView.Adapter<PartidoAdapter.PartidoViewHolder>() {

    // Método para actualizar los datos
    fun actualizarPartidos(nuevosPartidos: MutableList<Partido>) {
        partidos.clear()
        partidos.addAll(nuevosPartidos)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartidoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_partido, parent, false)
        return PartidoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PartidoViewHolder, position: Int) {
        holder.bind(partidos[position])
    }

    override fun getItemCount() = partidos.size

    class PartidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
}