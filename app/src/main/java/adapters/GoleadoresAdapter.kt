package adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appfutbol.R
import models.Goleador

class GoleadoresAdapter(
    private var goleadores: MutableList<Goleador>
) : RecyclerView.Adapter<GoleadoresAdapter.GoleadorViewHolder>() {

    // Método para actualizar los datos
    fun actualizarGoleadores(nuevosGoleadores: List<Goleador>) {
        val oldSize = goleadores.size
        goleadores.clear()
        notifyItemRangeRemoved(0, oldSize)
        goleadores.addAll(nuevosGoleadores)
        notifyItemRangeInserted(0, nuevosGoleadores.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoleadorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_goleador, parent, false)
        return GoleadorViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoleadorViewHolder, position: Int) {
        holder.bind(goleadores[position], position + 1)
    }

    override fun getItemCount() = goleadores.size

    class GoleadorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvPosicion: TextView = itemView.findViewById(R.id.tvPosicion)
        private val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        private val tvEquipo: TextView = itemView.findViewById(R.id.tvEquipo)
        private val tvGoles: TextView = itemView.findViewById(R.id.tvGoles)
        private val tvAsistencias: TextView = itemView.findViewById(R.id.tvAsistencias)

        fun bind(goleador: Goleador, posicion: Int) {
            tvPosicion.text = posicion.toString()
            tvNombre.text = goleador.nombre
            tvEquipo.text = goleador.equipo
            tvGoles.text = itemView.context.getString(R.string.goles_count, goleador.goles)

            // Mostrar asistencias si están disponibles
            if (goleador.asistencias != null) {
                tvAsistencias.text = itemView.context.getString(R.string.asistencias_count, goleador.asistencias)
                tvAsistencias.visibility = View.VISIBLE
            } else {
                tvAsistencias.visibility = View.GONE
            }
        }
    }
}