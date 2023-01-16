package com.angiuprojects.gamecatalog.utilities

import com.angiuprojects.gamecatalog.entities.implementation.Anime
import com.angiuprojects.gamecatalog.entities.implementation.Manga
import com.angiuprojects.gamecatalog.entities.implementation.TVShow
import com.google.firebase.database.FirebaseDatabase

class Constants {

    companion object {

        private lateinit var constantsInstance: Constants

        fun initializeConstantSingleton(): Constants {
            constantsInstance = Constants()
            return constantsInstance
        }

        fun getInstance(): Constants {
            return constantsInstance
        }
    }

    val dbInstance: FirebaseDatabase =
        FirebaseDatabase.getInstance("https://game-catalog-cde1d-default-rtdb.europe-west1.firebasedatabase.app")

    var logger: String = "CatalogAppLogger"

    /**
     * TV SHOW CONSTANTS -- START
     */

    var tvShowDbReference: String = "TvShows"
    var tvShowPath: String = "name"

    private var tvShows: MutableList<TVShow>? = null

    fun getInstanceTvShows(): MutableList<TVShow>? {
        if (tvShows == null) {
            tvShows = ArrayList()
        }
        return tvShows
    }

    /**
     * TV SHOW CONSTANTS -- END
     */

    /**
     * ANIME CONSTANTS -- START
     */

    var animeDbReference: String = "Anime"
    var animePath: String = "name"

    var animes: MutableList<Anime>? = null

    fun getInstanceAnime(): MutableList<Anime>? {
        if (animes == null) {
            animes = ArrayList()
        }
        return animes
    }

    /**
     * ANIME CONSTANTS -- END
     */

    /**
     * MANGA CONSTANTS -- START
     */

    var mangaDbReference: String = "Manga"
    var mangaPath: String = "name"

    var mangas: MutableList<Manga>? = null

    fun getInstanceManga(): MutableList<Manga>? {
        if (mangas == null) {
            mangas = ArrayList()
        }
        return mangas
    }

    /**
     * MANGA CONSTANTS -- END
     */

    /**
     * POP UP ACTIONS
     */

    val deleteShow = "Delete Show"
    val deleteSeason = "Delete Season"
    val completePreviousSeasons = "Complete Previous Seasons"
}