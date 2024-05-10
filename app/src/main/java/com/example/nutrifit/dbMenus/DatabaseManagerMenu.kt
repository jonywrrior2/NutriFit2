    package com.example.nutrifit.dbMenus

    import android.util.Log
    import com.example.nutrifit.pojo.Menu
    import com.google.firebase.auth.FirebaseAuth
    import com.google.firebase.firestore.FirebaseFirestore
    import java.text.SimpleDateFormat
    import java.util.Locale


    object DatabaseManagerMenu {

        private val db = FirebaseFirestore.getInstance()
        private val menusCollection = db.collection("menus")
        private val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email


        fun addMenu(
            menu: Menu,
            onSuccess: () -> Unit,
            onFailure: (Exception) -> Unit
        ) {
            db.collection("menus")
                .add(menu)
                .addOnSuccessListener { documentReference ->
                    Log.d("DatabaseManagerMenu", "Menu added with ID: ${documentReference.id}")
                    onSuccess()
                }
                .addOnFailureListener { e ->
                    Log.e("DatabaseManagerMenu", "Error adding menu", e)
                    onFailure(e)
                }
        }

        fun getUserMenus(
            onSuccess: (List<Menu>) -> Unit,
            onFailure: (Exception) -> Unit
        ) {
            val email = currentUserEmail
            if (email != null) {
                menusCollection.whereEqualTo("usuario", email)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        val menus = mutableListOf<Menu>()
                        for (document in querySnapshot.documents) {
                            val alimentos = document.getString("alimentos") ?: ""
                            val cantidad = (document.getLong("cantidad") ?: 0).toInt()
                            val kcal = (document.getDouble("kcal") ?: 0).toDouble()
                            val proteinas = (document.getDouble("proteinas") ?: 0).toDouble()
                            val tipo = document.getString("tipo") ?: ""
                            val unidad = document.getString("unidad") ?: ""
                            val fechaStr = document.getString("fecha") ?: ""
                            val usuario = email
                            val menu = Menu(alimentos, cantidad, kcal, proteinas, unidad, usuario, tipo, fechaStr)

                            menus.add(menu)
                        }
                        onSuccess(menus)
                    }
                    .addOnFailureListener { e -> onFailure(e) }
            } else {
                onFailure(Exception("Usuario no logueado"))
            }
        }


        fun eliminarMenu(
            menu: Menu,
            onSuccess: () -> Unit,
            onFailure: (Exception) -> Unit
        ) {

            val menuDocument = menusCollection
                .whereEqualTo("usuario", menu.usuario)
                .whereEqualTo("alimentos", menu.alimentos)
                .whereEqualTo("cantidad", menu.cantidad)
                .whereEqualTo("kcal", menu.kcal)
                .whereEqualTo("proteinas", menu.proteinas)
                .whereEqualTo("unidad", menu.unidad)
                .whereEqualTo("tipo", menu.tipo)
                .whereEqualTo("fecha", menu.fecha)


            menuDocument.get()
                .addOnSuccessListener { documents ->

                    if (!documents.isEmpty) {
                        val document = documents.documents[0]
                        document.reference.delete()
                            .addOnSuccessListener {
                                Log.d("DatabaseManagerMenu", "Menu eliminado correctamente")
                                onSuccess()
                            }
                            .addOnFailureListener { e ->
                                Log.e("DatabaseManagerMenu", "Error al eliminar el menú", e)
                                onFailure(e)
                            }
                    } else {

                        onFailure(Exception("Menu no encontrado"))
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("DatabaseManagerMenu", "Error fetching menu document", e)
                    onFailure(e)
                }
        }

        fun getUserMenusByDate(
            date: String,
            onSuccess: (List<Menu>) -> Unit,
            onFailure: (Exception) -> Unit
        ) {
            val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email

            if (currentUserEmail != null) {
                val db = FirebaseFirestore.getInstance()
                val menusCollection = db.collection("menus")


                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val dateObj = sdf.parse(date)
                val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(dateObj)

                menusCollection.whereEqualTo("usuario", currentUserEmail)
                    .whereEqualTo("fecha", formattedDate)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        val menus = mutableListOf<Menu>()
                        for (document in querySnapshot.documents) {
                            val alimentos = document.getString("alimentos") ?: ""
                            val cantidad = (document.getLong("cantidad") ?: 0).toInt()
                            val kcal = (document.getDouble("kcal") ?: 0.0).toDouble()
                            val proteinas = (document.getDouble("proteinas") ?: 0.0).toDouble()
                            val tipo = document.getString("tipo") ?: ""
                            val unidad = document.getString("unidad") ?: ""
                            val fechaStr = document.getString("fecha") ?: ""
                            val usuario = currentUserEmail
                            val menu = Menu(alimentos, cantidad, kcal, proteinas, unidad, usuario, tipo, fechaStr)

                            menus.add(menu)
                        }
                        onSuccess(menus)
                    }
                    .addOnFailureListener { e ->
                        onFailure(e)
                    }
            } else {
                onFailure(Exception("Usuario no logueado"))
            }
        }


    }
