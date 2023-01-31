package com.angiuprojects.gamecatalog.queries

import android.content.Context
import android.util.Log
import com.angiuprojects.gamecatalog.utilities.Constants
import com.angiuprojects.gamecatalog.utilities.ReadWriteJson
import com.google.firebase.database.*

class Queries {
    private lateinit var myRef: DatabaseReference

    companion object {

        private lateinit var singleton: Queries

        fun initializeQueriesSingleton(): Queries {
            singleton = Queries()
            return singleton
        }

        fun getInstance(): Queries {
            return singleton
        }
    }

    fun select(context: Context) {
        myRef = Constants.getInstance().dbInstance.getReference(Constants.getInstance().dbReference)
        myRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, prevChildKey: String?) {
                val item: String? = dataSnapshot.getValue(String::class.java)
                if (item != null) {
                    Log.i(Constants.logger, "Json: $item")
                    ReadWriteJson.getInstance().getUser(context, false)
                }
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {
                Log.e(Constants.logger, error.message)
            }
        })
    }

    fun addUpdate(json: String) {
        myRef = Constants.getInstance().dbInstance.getReference(Constants.getInstance().dbReference)
        myRef.setValue(json)
    }

    fun delete(json: String) {
        myRef = Constants.getInstance().dbInstance.getReference(Constants.getInstance().dbReference)
        myRef.child(json).removeValue()
    }
}