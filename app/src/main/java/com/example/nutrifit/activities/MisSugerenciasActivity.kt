package com.example.nutrifit.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nutrifit.R
import com.example.nutrifit.Tickets.TicketAdapter
import com.example.nutrifit.databases.DatabaseManagerTickets
import com.example.nutrifit.pojo.Ticket

class MisSugerenciasActivity: AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var ticketsAdapter: TicketAdapter
    private lateinit var ticketsList: MutableList<Ticket>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_sugerencias)

        recyclerView = findViewById(R.id.recyclerViewTickets)
        ticketsList = mutableListOf()
        ticketsAdapter = TicketAdapter(ticketsList)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MisSugerenciasActivity)
            adapter = ticketsAdapter
        }

        obtenerTicketsUsuarioActual()

        val btnSugerencias: Button = findViewById(R.id.btnSugerenciaM)
        val btnTusSugerencias: Button = findViewById(R.id.btnTusSugerenciasM)
        val btnVolver: Button = findViewById(R.id.volverMainActivityM)
        btnTusSugerencias.setBackgroundResource(R.color.grayOscuro)

        btnSugerencias.setOnClickListener {
            val intent = Intent(this, SugerenciasActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnVolver.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    //Obtener tickets del usuario logueado
    private fun obtenerTicketsUsuarioActual() {
        DatabaseManagerTickets.getUserTickets(
            onSuccess = { tickets ->
                ticketsList.clear()
                ticketsList.addAll(tickets)
                ticketsAdapter.notifyDataSetChanged()
            },
            onFailure = { exception ->
                Log.e("MisSugerenciasActivity", "Error al obtener los tickets del usuario actual: $exception")
            }
        )
    }

}