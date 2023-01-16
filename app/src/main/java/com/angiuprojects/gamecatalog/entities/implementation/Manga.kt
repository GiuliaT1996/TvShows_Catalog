package com.angiuprojects.gamecatalog.entities.implementation

import com.angiuprojects.gamecatalog.entities.SecondItem
import com.angiuprojects.gamecatalog.utilities.MangaStatusEnum

class Manga : SecondItem<Volume> {

    var status : MangaStatusEnum = MangaStatusEnum.IN_CORSO
    override var name : String = ""
    override var volumes : MutableList<Volume> = mutableListOf()
    override var completed: Boolean = false

    constructor()

    constructor(name: String, volumes: MutableList<Volume>, status: MangaStatusEnum) {
        this.name = name
        this.volumes = volumes
        this.status = status
        completed = isCompleted(volumes)
    }

    override fun isCompleted (items: MutableList<Volume>) : Boolean {
        return !items.any { x -> !x.completed }
    }
}