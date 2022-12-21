package com.angiuprojects.gamecatalog.queries

import android.util.Log
import com.angiuprojects.gamecatalog.utilities.Constants
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

    fun <T> select(dbReference: String, path: String, tClass: Class<T>, itemList : MutableList<T>?) {
        myRef = Constants.getInstance().dbInstance.getReference(dbReference)
        myRef.orderByChild(path).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, prevChildKey: String?) {
                val item: T? = dataSnapshot.getValue(tClass)
                if (item != null) {
                    Log.i(Constants.getInstance().logger, "Inserted item $item of type ${tClass.name}")
                    itemList?.add(item)
                }
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {
                Log.e(Constants.getInstance().logger, error.message)
            }
        })
    }

    fun addUpdate(dbReference: String) {
        myRef = Constants.getInstance().dbInstance.getReference(dbReference)
        //TODO myRef.child(c.name).setValue(c)
    }

    fun delete(dbReference: String) {
        myRef = Constants.getInstance().dbInstance.getReference(dbReference)
        //TODO myRef.child(c.name).removeValue()
    }
}