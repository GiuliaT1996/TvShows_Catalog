package com.angiuprojects.gamecatalog.entities

import com.angiuprojects.gamecatalog.entities.implementation.Anime
import com.angiuprojects.gamecatalog.entities.implementation.Manga
import com.angiuprojects.gamecatalog.entities.implementation.TVShow

class User(
    var id: String,
    var animeList: MutableList<Anime>,
    var mangaList: MutableList<Manga>,
    var tvShowList: MutableList<TVShow>
) {

}