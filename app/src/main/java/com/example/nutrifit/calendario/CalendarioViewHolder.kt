package com.example.nutrifit.calendario
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nutrifit.R
import java.time.LocalDate

class CalendarioViewHolder(itemView: View, private val onItemListener: CalendarioAdapter.OnItemListener, private val days: ArrayList<LocalDate>) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
    val parentView: View = itemView.findViewById(R.id.parentView)
    val dayOfMonth: TextView = itemView.findViewById(R.id.cellDayText)

    init {
        itemView.setOnClickListener(this)

    }

    override fun onClick(view: View) {
        onItemListener.onItemClick(adapterPosition, days[adapterPosition])

    }


}