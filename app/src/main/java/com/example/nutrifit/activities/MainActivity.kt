package com.example.nutrifit.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nutrifit.R
import com.example.nutrifit.calendario.CalendarioAdapter
import com.example.nutrifit.calendario.CalendarioUtils
import com.example.nutrifit.databases.DatabaseManagerMenu
import com.example.nutrifit.databases.DatabaseManagerUser
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate

class MainActivity : AppCompatActivity(), CalendarioAdapter.OnItemListener {
    private lateinit var monthYearText: TextView
    private lateinit var calendarRecyclerView: RecyclerView
    private var selectedLongClickDate: LocalDate? = LocalDate.now()
    private lateinit var cDesayunoTextView: TextView
    private lateinit var pDesayunoTextView: TextView
    private lateinit var cAlmuerzoTextView: TextView
    private lateinit var pAlmuerzoTextView: TextView
    private lateinit var cMeriendaTextView: TextView
    private lateinit var pMeriendaTextView: TextView
    private lateinit var cCenaTextView: TextView
    private lateinit var pCenaTextView: TextView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var txtCaloriasNecesarias : TextView
    private lateinit var txtCaloriasConsumidas : TextView
    private lateinit var barraProgeso : ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
        initWidgets()
        val cerrarSesionButton: ImageButton = findViewById(R.id.cerrarSesionButton)
        val ticketButton: ImageButton = findViewById(R.id.sugerenciasButton)
        val perfilButton: ImageButton = findViewById(R.id.perfilButton)

        cerrarSesionButton.setOnClickListener {
            mAuth.signOut()

            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)

            finish()
        }

        ticketButton.setOnClickListener {
            val intent = Intent(this@MainActivity, SugerenciasActivity::class.java)
            startActivity(intent)

            finish()
        }

        perfilButton.setOnClickListener {
            val intent = Intent(this@MainActivity, PerfilActivity::class.java)
            startActivity(intent)

            finish()
        }


        //Aqui cogemos la fecha de vuelta del Activity de añadir comida
        selectedLongClickDate =
            intent.getStringExtra("fechaSeleccionada")?.let { LocalDate.parse(it) }
        if (CalendarioUtils.selectedDate == null) {
            CalendarioUtils.selectedDate = LocalDate.now()
        }

        if (selectedLongClickDate == null) {
            selectedLongClickDate = LocalDate.now()
        }

        updateCaloriesAndProteins(selectedLongClickDate)
        setWeekView()


        updateCaloriesProgress(selectedLongClickDate)
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

        txtCaloriasNecesarias = findViewById(R.id.txtCaloriasNecesarias)
        txtCaloriasConsumidas = findViewById(R.id.txtCaloriasConsumidas)

        val caloriasNecesariasUsuario = obtenerCaloriasNecesariasDelUsuario()

        txtCaloriasNecesarias.text = "Calorías necesarias: $caloriasNecesariasUsuario"

        barraProgeso = findViewById(R.id.barraProgeso)



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

        updateCaloriesAndProteins(selectedLongClickDate)
        updateCaloriesProgress(selectedLongClickDate)


    }

    private fun updateCaloriesAndProteins(date: LocalDate?) {
        val formattedDate = date?.toString()

        if (formattedDate != null) {
            DatabaseManagerMenu.getUserMenusByDate(formattedDate, onSuccess = { menus ->
                // Calcular calorías y proteínas totales para cada tipo de comida
                val desayunoMenus = menus.filter { it.tipo == "Desayuno" }
                val almuerzoMenus = menus.filter { it.tipo == "Almuerzo" }
                val meriendaMenus = menus.filter { it.tipo == "Merienda" }
                val cenaMenus = menus.filter { it.tipo == "Cena" }

                // Calcular calorías consumidas en ese dia
                val totalCaloriasConsumidas = menus.sumByDouble { it.kcal }
                txtCaloriasConsumidas.text = "Calorías consumidas: $totalCaloriasConsumidas"



                val totalCalDesayuno = desayunoMenus.sumByDouble { it.kcal }
                val totalProtDesayuno = desayunoMenus.sumByDouble { it.proteinas }

                val totalCalAlmuerzo = almuerzoMenus.sumByDouble { it.kcal }
                val totalProtAlmuerzo = almuerzoMenus.sumByDouble { it.proteinas }

                val totalCalMerienda = meriendaMenus.sumByDouble { it.kcal }
                val totalProtMerienda = meriendaMenus.sumByDouble { it.proteinas }

                val totalCalCena = cenaMenus.sumByDouble { it.kcal }
                val totalProtCena = cenaMenus.sumByDouble { it.proteinas }

                // Mostrar calorías y proteínas totales en los TextViews correspondientes
                cDesayunoTextView.text = "Calorias: " + totalCalDesayuno.toString()
                pDesayunoTextView.text = "Proteinas: " + totalProtDesayuno.toString() + "g"

                cAlmuerzoTextView.text = "Calorias: " + totalCalAlmuerzo.toString()
                pAlmuerzoTextView.text = "Proteinas: " + totalProtAlmuerzo.toString() + "g"

                cMeriendaTextView.text = "Calorias: " + totalCalMerienda.toString()
                pMeriendaTextView.text = "Proteinas: " + totalProtMerienda.toString() + "g"

                cCenaTextView.text = "Calorias: " + totalCalCena.toString()
                pCenaTextView.text = "Proteinas: " + totalProtCena.toString() + "g"

            }, onFailure = {
            })
        }

    }

    private fun obtenerCaloriasNecesariasDelUsuario() {
        val userEmail = mAuth.currentUser?.email
        if (userEmail != null) {
            DatabaseManagerUser.getUserByEmail(userEmail,
                onSuccess = { user ->
                    if (user != null) {
                        val caloriasNecesarias = user.calorias

                        txtCaloriasNecesarias.text = "Calorías necesarias: $caloriasNecesarias"
                    } else {
                    }
                },
                onFailure = { exception ->

                }
            )
        }
    }


    private fun updateCaloriesProgress(date: LocalDate?) {
        val userEmail = mAuth.currentUser?.email
        if (userEmail != null) {
            DatabaseManagerUser.getUserByEmail(userEmail,
                onSuccess = { user ->
                    if (user != null) {
                        val caloriasNecesarias = user.calorias.toDouble()
                        val formattedDate = date?.toString()

                        if (formattedDate != null) {
                            DatabaseManagerMenu.getUserMenusByDate(formattedDate, onSuccess = { menus ->
                                val caloriasConsumidas = menus.sumByDouble { it.kcal }
                                updateProgress(caloriasConsumidas, caloriasNecesarias)
                            }, onFailure = {

                            })
                        }
                    } else {

                    }
                },
                onFailure = { exception ->

                }
            )
        }
    }

    private fun updateProgress(caloriasConsumidas: Double, caloriasNecesarias: Double) {
        if (caloriasNecesarias > 0) {
            val progress = ((caloriasConsumidas / caloriasNecesarias) * 100).toInt()
            barraProgeso.progress = progress
        } else {
            barraProgeso.progress = 0
        }
    }


}
