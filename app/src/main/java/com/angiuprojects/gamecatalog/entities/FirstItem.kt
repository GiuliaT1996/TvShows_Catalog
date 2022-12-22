package com.angiuprojects.gamecatalog.entities

interface FirstItem {

    var number : Int
    var name : String
    var seenEpisodes : Int
    var totalEpisodes: Int
    var completed : Boolean

    fun isCompleted(seen: Int, total: Int) : Boolean
}