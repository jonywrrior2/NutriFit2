    package com.example.nutrifit.databases

    import android.util.Log
    import com.example.nutrifit.pojo.Alimento
    import com.example.nutrifit.pojo.Cambios
    import com.example.nutrifit.pojo.Menu
    import com.example.nutrifit.pojo.Ticket
    import com.google.firebase.firestore.FirebaseFirestore

    object DatabaseManager {

        private val db = FirebaseFirestore.getInstance()
        private val alimentosCollection = db.collection("alimentos")
        fun buscarAlimentos(palabraClave: String, callback: (List<Alimento>) -> Unit) {
            val resultados = mutableListOf<Alimento>()
            val palabraClaveLower = palabraClave.toLowerCase()

            alimentosCollection
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val nombre = document.getString("nombre") ?: ""
                        if (nombre.toLowerCase().contains(palabraClaveLower)) {
                            val calorias = document.getDouble("kcal") ?: 0.0
                            val proteinas = document.getDouble("proteinas") ?: 0.0
                            val cantidad = document.getLong("cantidad")?.toDouble() ?: 0.0
                            val unidad = document.getString("unidad")
                            val alimento = Alimento(nombre, calorias, proteinas, cantidad, unidad)
                            resultados.add(alimento)
                        }
                    }
                    callback(resultados)
                }
                .addOnFailureListener { exception ->
                    callback(emptyList())
                }
        }

        fun addAlimento(
            alimento: Alimento,
            onSuccess: () -> Unit,
            onFailure: (Exception) -> Unit
        ) {
            DatabaseManager.alimentosCollection.add(alimento)
                .addOnSuccessListener {
                    onSuccess.invoke()
                }
                .addOnFailureListener { exception ->
                    onFailure.invoke(exception)
                }
        }
    }

