    package com.example.nutrifit.dbMenus

    import android.util.Log
    import com.example.nutrifit.pojo.Menu
    import com.google.firebase.auth.FirebaseAuth
    import com.google.firebase.firestore.FirebaseFirestore


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
                            val menuData = document.data?.toMutableMap()
                            val alimentos = menuData?.get("alimento") as String
                            val cantidad = (menuData?.get("cantidad") as Long).toInt()
                            val kcal = (menuData?.get("kcal") as Long).toDouble()
                            val proteinas = (menuData?.get("proteinas") as Long).toDouble()
                            val tipo = menuData?.get("tipo") as String
                            val unidad = menuData?.get("unidad") as String
                            val fechaStr = menuData?.get("fecha") as String
                            val usuario = email
                            val menu = Menu(alimentos, cantidad, kcal, proteinas, unidad, usuario, tipo, fechaStr)

                            menus.add(menu)
                        }
                        onSuccess(menus)
                    }
                    .addOnFailureListener { e -> onFailure(e) }
            } else {
                onFailure(Exception("User not logged in"))
            }
        }

        fun eliminarMenu(
            menu: Menu,
            onSuccess: () -> Unit,
            onFailure: (Exception) -> Unit
        ) {

            val menuDocument = menusCollection
                .whereEqualTo("usuario", menu.usuario)
                .whereEqualTo("alimento", menu.alimentos)
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
                                Log.d("DatabaseManagerMenu", "Menu deleted successfully")
                                onSuccess()
                            }
                            .addOnFailureListener { e ->
                                Log.e("DatabaseManagerMenu", "Error deleting menu", e)
                                onFailure(e)
                            }
                    } else {

                        onFailure(Exception("Menu not found"))
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("DatabaseManagerMenu", "Error fetching menu document", e)
                    onFailure(e)
                }
        }

    }
