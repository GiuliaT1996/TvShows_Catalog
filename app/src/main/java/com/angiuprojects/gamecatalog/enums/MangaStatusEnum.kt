package com.angiuprojects.gamecatalog.enums

enum class MangaStatusEnum(val status: String) {
    COMPLETO("Completo"),
    INTERROTTO("Interrotto"),
    IN_CORSO("In Corso");

    companion object {
        fun getList() : List<String> {
            return values().map {
                it.status
            }
        }

        fun getMangaStatusEnum (status: String) : MangaStatusEnum {
            return values().filter { it.status == status }[0]
        }
    }
}

