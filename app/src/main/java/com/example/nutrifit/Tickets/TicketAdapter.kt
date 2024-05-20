package com.example.nutrifit.Tickets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nutrifit.R
import com.example.nutrifit.pojo.Ticket

class TicketAdapter(private val ticketsList: List<Ticket>) :
    RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {

    inner class TicketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tipoTicketTextView: TextView = itemView.findViewById(R.id.tipoTicket)
        val comentarioTicketTextView: TextView = itemView.findViewById(R.id.comentarioTicket)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ticket, parent, false)
        return TicketViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val currentItem = ticketsList[position]

        holder.tipoTicketTextView.text = currentItem.tipoComentario
        holder.comentarioTicketTextView.text = currentItem.comentario
    }
    override fun getItemCount() = ticketsList.size
}
