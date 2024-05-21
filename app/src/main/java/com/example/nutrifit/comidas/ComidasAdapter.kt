package com.example.nutrifit.comidas

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nutrifit.R
import com.example.nutrifit.activities.AnhadirComidaActivity
import com.example.nutrifit.activities.NutrientesActivity
import com.example.nutrifit.pojo.Alimento

class ComidasAdapter(private val context: Context, private val itemClickListener: (Alimento) -> Unit) : RecyclerView.Adapter<ComidasAdapter.ComidaViewHolder>() {

    private var comidas: List<Alimento> = ArrayList()

    inner class ComidaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreComidaTextView: TextView = itemView.findViewById(R.id.nombreComidaTextView)

        init {
            itemView.setOnClickListener {
                val comidaSeleccionada = comidas[adapterPosition]
                itemClickListener(comidaSeleccionada)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComidaViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comida, parent, false)
        return ComidaViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ComidaViewHolder, position: Int) {
        val comida = comidas[position]
        val texto = "${comida.nombre}\nKcal: ${comida.kcal} Proteínas: ${comida.proteinas}g Cantidad: ${comida.cantidad} ${comida.unidad}"
        holder.nombreComidaTextView.text = texto
        holder.itemView.setOnClickListener {
            val intent = Intent(context, NutrientesActivity::class.java).apply {
                putExtra("nombre", comida.nombre)
                putExtra("kcal", comida.kcal)
                putExtra("proteinas", comida.proteinas)
                putExtra("cantidad", comida.cantidad)
                putExtra("unidad", comida.unidad)
                putExtra("tipo", (context as AnhadirComidaActivity).tipo)
                putExtra("fechaSeleccionada", (context as AnhadirComidaActivity).selectedLongClickDateStr)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return comidas.size
    }

    fun actualizarLista(nuevaLista: List<Alimento>) {
        comidas = nuevaLista
        notifyDataSetChanged()
    }
}
