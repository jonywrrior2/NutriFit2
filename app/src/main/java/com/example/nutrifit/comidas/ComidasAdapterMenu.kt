package com.example.nutrifit.comidas

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.nutrifit.R
import com.example.nutrifit.activities.AnhadirComidaActivity
import com.example.nutrifit.databases.DatabaseManagerMenu
import com.example.nutrifit.pojo.Menu
import java.time.LocalDate

class ComidasAdapterMenu(private val context: Context, private var menus: List<Menu>, private val anhadirComidaActivity: AnhadirComidaActivity) : RecyclerView.Adapter<ComidasAdapterMenu.ComidaViewHolder>() {

    inner class ComidaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewAlimentos: TextView = itemView.findViewById(R.id.textViewAlimentos)
        val textViewCantidad: TextView = itemView.findViewById(R.id.textViewCantidad)
        val textViewKcal: TextView = itemView.findViewById(R.id.textViewKcal)
        val textViewProteinas: TextView = itemView.findViewById(R.id.textViewProteinas)
        val eliminarButton : ImageButton = itemView.findViewById(R.id.borrarMenu)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComidaViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.menu_item_layout, parent, false)
        return ComidaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ComidaViewHolder,     position: Int) {
        val currentItem = menus[position]
        holder.textViewAlimentos.text = context.getString(R.string.alimentos_template, currentItem.alimentos)
        holder.textViewCantidad.text = context.getString    (R.string.cantidad_template, currentItem.cantidad)+ " ${currentItem.unidad}"
        holder.textViewKcal.text = context.getString(R.string.kcal_template, currentItem.kcal)+ "/kcal"
        holder.textViewProteinas.text = context.getString(R.string.proteinas_template, currentItem.proteinas) + " g"

        holder.eliminarButton.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("¿Desea eliminar este alimento?")
                .setMessage("Esta acción no se puede deshacer.")
                .setPositiveButton("Aceptar") { dialog, _ ->

                    menus[position].let { menu ->
                        DatabaseManagerMenu.eliminarMenu(menu,
                            onSuccess = {
                                Toast.makeText(context, "Alimento borrado con exito", Toast.LENGTH_SHORT).show()
                                notifyDataSetChanged()
                                val selectedDateStr = menus[position].fecha // Obtener la fecha del menú eliminado
                                val selectedDate = selectedDateStr?.let { LocalDate.parse(it) }
                               anhadirComidaActivity.obtenerMenusDelUsuarioActual(selectedDate)
                            },
                            onFailure = { exception ->
                                Toast.makeText(context, "Error al eliminar el alimento", Toast.LENGTH_SHORT).show()
                            })
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }




    }

    override fun getItemCount() = menus.size

    fun actualizarLista(nuevosMenus: List<Menu>) {
        menus = nuevosMenus
        notifyDataSetChanged()
    }


}
