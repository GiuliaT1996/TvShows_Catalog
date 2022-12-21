package com.angiuprojects.gamecatalog.entities.implementation

import com.angiuprojects.gamecatalog.entities.SingleItem

class Chapter : SingleItem {

    override var number : Int = 0
    override var name : String = ""
    override var seenOrRead : Boolean = false

    constructor(number: Int, name: String, read: Boolean) {
        this.name = name
        this.number = number
        this.seenOrRead = read
    }

}