package com.angiuprojects.gamecatalog.enums

enum class ShowTypeEnum(val type: String) {
    MANGA("Manga"),
    ANIME("Anime"),
    TV_SHOW("Serie Tv");

    companion object {
        fun getShowTypeEnum (type: String) : ShowTypeEnum {
            return values().filter { it.type == type }[0]
        }
    }
}