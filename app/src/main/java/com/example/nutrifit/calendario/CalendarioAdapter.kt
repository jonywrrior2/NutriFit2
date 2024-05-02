package com.example.nutrifit.calendario
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nutrifit.R
import java.time.LocalDate

class CalendarioAdapter(private val days: ArrayList<LocalDate>, private val onItemListener: OnItemListener) :
    RecyclerView.Adapter<CalendarioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarioViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.celda_calendario, parent, false)
        val layoutParams = view.layoutParams
        if (days.size > 15) {
            layoutParams.height = (parent.height * 0.166666666).toInt()
        } else { // week view
            layoutParams.height = parent.height
        }
        return CalendarioViewHolder(view, onItemListener, days)
    }

    override fun onBindViewHolder(holder: CalendarioViewHolder, position: Int) {
        val date = days[position]
        holder.dayOfMonth.text = date.dayOfMonth.toString()
        if (date == CalendarioUtils.selectedDate) {
            holder.parentView.setBackgroundColor(Color.LTGRAY)
        }
    }

    override fun getItemCount(): Int {
        return days.size
    }

    interface OnItemListener {
        fun onItemClick(position: Int, date: LocalDate)

    }
}
