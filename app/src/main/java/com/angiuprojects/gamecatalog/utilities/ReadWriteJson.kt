package com.angiuprojects.gamecatalog.utilities

import android.content.Context
import android.util.Log
import com.angiuprojects.gamecatalog.entities.User
import com.google.gson.Gson
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import kotlin.math.floor

class ReadWriteJson {

    companion object {
        private lateinit var readWriteJson: ReadWriteJson

        fun initializeSingleton(): ReadWriteJson {
            readWriteJson = ReadWriteJson()
            return readWriteJson
        }

        fun getInstance(): ReadWriteJson {
            return readWriteJson
        }
    }

    private val fileName = "ShowsDatabase.txt"
    private val directory = "Shows Database"

    fun getUser(context: Context, resetUserDTO: Boolean) : User {
        val dir = getDirectory(context)
        val json = read(dir, context)
        Log.i(Constants.logger, "Json: $json")
        if(json != "" && !resetUserDTO) {
            try {
                return Gson().fromJson(json, User::class.java)
            } catch (e: Exception) {
                Log.e(Constants.logger, "Error converting file in json")
            }
        }
        return createNewUser()
    }

    private fun createNewUser() : User {
        val id = "" + floor(Math.random() * 9) + floor(Math.random() * 9) + floor(Math.random() * 9) + floor(Math.random() * 9) + floor(Math.random() * 9) + floor(Math.random() * 9)
        return User(id, mutableListOf(), mutableListOf(), mutableListOf())
    }

    private fun read(dir: File, context: Context) : String {
        try {
            val file = File(dir, fileName)
            if (!file.exists()) {
                Log.e(Constants.logger, "File does not exist")
                write(context, true)
                return ""
            }
            val reader = FileReader(file)
            return reader.readText()
        } catch (e: Exception) {
            Log.e(Constants.logger, "Error converting file in json")
        }
        return ""
    }

    fun write(context: Context, newUser: Boolean) {

        val dir = getDirectory(context)
        try {
            val file = File(dir, fileName)
            val writer = FileWriter(file)
            writer.append(if(newUser) "" else Gson().toJson(Constants.user))
            writer.flush()
            writer.close()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(Constants.logger, "Error reading or saving file")
        }
    }

    private fun getDirectory(context: Context) : File {
        val dir = File(context.filesDir, directory)
        if (!dir.exists()) {
            dir.mkdir()
        }
        return dir
    }
}