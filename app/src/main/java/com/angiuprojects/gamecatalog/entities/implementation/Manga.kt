package com.angiuprojects.gamecatalog.entities.implementation

import com.angiuprojects.gamecatalog.entities.MainItem
import com.angiuprojects.gamecatalog.enums.MangaStatusEnum

class Manga : MainItem {

    var status : MangaStatusEnum = MangaStatusEnum.IN_CORSO
    override var name : String = ""
    override var seasons : MutableList<Season> = mutableListOf()
    override var completed: Boolean = false

    constructor()

    constructor(name: String, seasons: MutableList<Season>, status: MangaStatusEnum) {
        this.name = name
        this.seasons = seasons
        this.status = status
        completed = isCompleted(seasons)
    }

    override fun isCompleted (items: MutableList<Season>) : Boolean {
        return !items.any { x -> !x.completed }
    }
}