package com.angiuprojects.gamecatalog.utilities

import com.angiuprojects.gamecatalog.entities.User
import com.google.firebase.database.FirebaseDatabase

class Constants {

    companion object {

        var logger: String = "CatalogAppLogger"

        private lateinit var constantsInstance: Constants

        fun initializeConstantSingleton(): Constants {
            constantsInstance = Constants()
            return constantsInstance
        }

        fun getInstance(): Constants {
            return constantsInstance
        }

        var user: User? = null
    }

    val dbInstance: FirebaseDatabase =
        FirebaseDatabase.getInstance("https://game-catalog-cde1d-default-rtdb.europe-west1.firebasedatabase.app")

    val dbReference: String = "User"
}