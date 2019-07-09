package com.example.sialarm.utils

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import java.util.*

/**
 * Created by Amir on 10/13/2018.
 */

class FirebaseData(val reference: DatabaseReference) : ChildEventListener {

    val snapshots = ArrayList<DataSnapshot>()

    init {
        reference.addChildEventListener(this)
    }

    private fun getIndexForKey(key: String): Int {
        return snapshots.indices.filter { snapshots[it].key == key }.last()
    }

    override fun onChildChanged(p0: DataSnapshot, p1: String?) {
        val index = getIndexForKey(p0.key!!)
        snapshots[index] = p0
    }

    override fun onChildAdded(p0: DataSnapshot, p1: String?) {
        val index = 0
        snapshots.add(index, p0)
    }

    override fun onChildRemoved(p0: DataSnapshot) {
        val index = getIndexForKey(p0.key!!)
        snapshots.removeAt(index)
    }

    override fun onChildMoved(p0: DataSnapshot, p1: String?) {
        val oldIndex = getIndexForKey(p0.key!!)
        snapshots.removeAt(oldIndex)
        val newIndex = if (p1 == null) 0 else getIndexForKey(p1) + 1
        snapshots.add(newIndex, p0)
    }

    override fun onCancelled(p0: DatabaseError) {
    }

}