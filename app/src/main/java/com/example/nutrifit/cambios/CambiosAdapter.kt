package com.example.nutrifit.cambios

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nutrifit.R
import com.example.nutrifit.pojo.Cambios

class CambiosAdapter(private val cambiosList: List<Cambios>) :
    RecyclerView.Adapter<CambiosAdapter.CambiosViewHolder>() {

    inner class CambiosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fechaTextView: TextView = itemView.findViewById(R.id.fechaCambio)
        val pesoTextView: TextView = itemView.findViewById(R.id.pesoCambio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CambiosViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cambio_usuario, parent, false)
        return CambiosViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CambiosViewHolder, position: Int) {
        val currentItem = cambiosList[position]

        holder.pesoTextView.text = currentItem.peso.toString()
        holder.fechaTextView.text = currentItem.fecha
    }

    override fun getItemCount() = cambiosList.size


}
