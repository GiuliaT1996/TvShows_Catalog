package com.angiuprojects.gamecatalog.utilities

enum class MangaStatusEnum(status: String) {
    COMPLETO("Completo"),
    INTERROTTO("Interrotto"),
    IN_CORSO("In Corso");

    companion object {
        fun getList() : List<String> {
            return values().map {
                it.toString()
            }
        }

        fun getMangaStatusEnum (status: String) : MangaStatusEnum {
            return values().filter { it.toString() == status }[0]
        }
    }
}

