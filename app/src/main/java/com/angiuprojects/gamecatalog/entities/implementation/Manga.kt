package com.angiuprojects.gamecatalog.entities.implementation

import com.angiuprojects.gamecatalog.entities.MainItem
import com.angiuprojects.gamecatalog.enums.MangaStatusEnum

class Manga(
    override var name: String,
    override var seasons: MutableList<Season>,
    var status: MangaStatusEnum
) : MainItem {

    override var completed: Boolean = false

    init {
        completed = isCompleted(seasons)
    }

    override fun isCompleted (items: MutableList<Season>) : Boolean {
        return !items.any { x -> !x.completed }
    }
}