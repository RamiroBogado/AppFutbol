package com.example.appfutbol

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PartidoAdapter(
    private val partidosList: MutableList<Partido>,
    private val context: Context
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

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetallePartidoActivity::class.java).apply {
                // Pasar los datos individualmente en lugar del objeto completo
                putExtra("id", partido.id)
                putExtra("fecha", partido.fecha)
                putExtra("hora", partido.hora)
                putExtra("equipoLocal", partido.equipoLocal)
                putExtra("equipoVisitante", partido.equipoVisitante)
                putExtra("resultado", partido.resultado)
            }
            context.startActivity(intent)
        }
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