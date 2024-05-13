    package com.example.nutrifit.databases

    import com.example.nutrifit.pojo.Alimento
    import com.google.firebase.firestore.FirebaseFirestore

    class DatabaseManager {

        private val db = FirebaseFirestore.getInstance()
        private val alimentosCollection = db.collection("alimentos")

        fun buscarAlimentos(palabraClave: String, callback: (List<Alimento>) -> Unit) {
            val resultados = mutableListOf<Alimento>()

            alimentosCollection
                .whereGreaterThanOrEqualTo("nombre", palabraClave)
                .whereLessThanOrEqualTo("nombre", palabraClave + "\uf8ff")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val nombre = document.getString("nombre")
                        val calorias = document.getLong("kcal")?.toDouble() ?: 0.0
                        val proteinas = document.getLong("proteinas")?.toDouble() ?: 0.0
                        val cantidad = document.getLong("cantidad")?.toDouble() ?: 0.0
                        val unidad = document.getString("unidad")
                            val alimento = Alimento(nombre, calorias, proteinas, cantidad, unidad)
                            resultados.add(alimento)

                    }
                    callback(resultados)
                }
                .addOnFailureListener { exception ->
                    callback(emptyList())
                }
        }
    }

