package com.example.nutrifit.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nutrifit.R
import com.example.nutrifit.calendario.CalendarioAdapter
import com.example.nutrifit.calendario.CalendarioUtils
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate

class MainActivity : AppCompatActivity(), CalendarioAdapter.OnItemListener {
    private lateinit var monthYearText: TextView
    private lateinit var calendarRecyclerView: RecyclerView
    private var selectedLongClickDate: LocalDate? =  LocalDate.now()
    private lateinit var cDesayunoTextView: TextView
    private lateinit var pDesayunoTextView: TextView
    private lateinit var cAlmuerzoTextView: TextView
    private lateinit var pAlmuerzoTextView: TextView
    private lateinit var cMeriendaTextView: TextView
    private lateinit var pMeriendaTextView: TextView
    private lateinit var cCenaTextView: TextView
    private lateinit var pCenaTextView: TextView
    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initWidgets()
        mAuth = FirebaseAuth.getInstance()
        val cerrarSesionButton: ImageButton = findViewById(R.id.cerrarSesionButton)
        cerrarSesionButton.setOnClickListener {
            mAuth.signOut()

            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)

            finish()
        }
        //Aqui cogemos la fecha de vuelta del Activity de añadir comida
        selectedLongClickDate = intent.getStringExtra("fechaSeleccionada")?.let { LocalDate.parse(it) }
        if (CalendarioUtils.selectedDate == null) {
            CalendarioUtils.selectedDate = LocalDate.now()
        }
        setWeekView()
    }

    private fun initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView)
        monthYearText = findViewById(R.id.monthYearTV)

        val addFoodTextView1: TextView = findViewById(R.id.anhadircomida)
        val addFoodTextView2: TextView = findViewById(R.id.anhadircomida2)
        val addFoodTextView3: TextView = findViewById(R.id.anhadircomida3)
        val addFoodTextView4: TextView = findViewById(R.id.anhadircomida4)

        cDesayunoTextView = findViewById(R.id.cDesayuno)
        pDesayunoTextView = findViewById(R.id.pDesayuno)
        cAlmuerzoTextView = findViewById(R.id.cAlmuerzo)
        pAlmuerzoTextView = findViewById(R.id.pAlmuerzo)
        cMeriendaTextView = findViewById(R.id.cMerienda)
        pMeriendaTextView = findViewById(R.id.pMerienda)
        cCenaTextView = findViewById(R.id.cCena)
        pCenaTextView = findViewById(R.id.pCena)

        addFoodTextView1.setOnClickListener {
            val intent = Intent(this, AnhadirComidaActivity::class.java)
            intent.putExtra("tipo", "Desayuno")
            intent.putExtra("fechaSeleccionada", selectedLongClickDate?.toString())
            startActivity(intent)
        }

        addFoodTextView2.setOnClickListener {
            val intent = Intent(this, AnhadirComidaActivity::class.java)
            intent.putExtra("tipo", "Almuerzo")
            intent.putExtra("fechaSeleccionada", selectedLongClickDate?.toString())
            startActivity(intent)
        }

        addFoodTextView3.setOnClickListener {
            val intent = Intent(this, AnhadirComidaActivity::class.java)
            intent.putExtra("tipo", "Merienda")
            intent.putExtra("fechaSeleccionada", selectedLongClickDate?.toString())
            startActivity(intent)
        }

        addFoodTextView4.setOnClickListener {
            val intent = Intent(this, AnhadirComidaActivity::class.java)
            intent.putExtra("tipo", "Cena")
            intent.putExtra("fechaSeleccionada", selectedLongClickDate?.toString())
            startActivity(intent)
        }
    }

    private fun setWeekView() {
        val currentDate = LocalDate.now()
        val startOfWeek = CalendarioUtils.mondayForDate(CalendarioUtils.selectedDate ?: currentDate)
        val endOfWeek = startOfWeek?.plusDays(6)
        val startMonthYear = CalendarioUtils.monthYearFromDate(startOfWeek ?: currentDate, "es")
        val endMonthYear = CalendarioUtils.monthYearFromDate(endOfWeek ?: currentDate, "es")
        val monthYearTextString = if (startMonthYear != endMonthYear) {
            "$startMonthYear / $endMonthYear"
        } else {
            startMonthYear

        }

        monthYearText.text = monthYearTextString

        val days = CalendarioUtils.daysInWeekArray(CalendarioUtils.selectedDate ?: currentDate)

        val calendarAdapter = CalendarioAdapter(days, this)
        val layoutManager = GridLayoutManager(applicationContext, 7)
        calendarRecyclerView.layoutManager = layoutManager
        calendarRecyclerView.adapter = calendarAdapter


    }

    fun previousWeekAction(view: View?) {
        CalendarioUtils.selectedDate = CalendarioUtils.selectedDate?.minusWeeks(1)
        setWeekView()
    }

    fun nextWeekAction(view: View?) {
        CalendarioUtils.selectedDate = CalendarioUtils.selectedDate?.plusWeeks(1)
        setWeekView()
    }

    override fun onItemClick(position: Int, date: LocalDate) {
        CalendarioUtils.selectedDate = date
        setWeekView()
        selectedLongClickDate = date
    }



}
