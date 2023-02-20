package com.angiuprojects.gamecatalog.utilities

import com.angiuprojects.gamecatalog.entities.User

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
}