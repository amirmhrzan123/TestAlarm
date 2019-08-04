package com.example.sialarm.ui.homepage

import com.example.sialarm.data.firebase.Users
import com.example.sialarm.data.prefs.PrefsManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope

class MainRepository constructor(private val viewModelScope:CoroutineScope,
                                 private val firebaseDatabase: FirebaseDatabase,
                                 private val prefsManager: PrefsManager) {

    fun saveUsers(){
        firebaseDatabase.getReference("users").child(prefsManager.getUserId()).addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    val user = p0.getValue(Users::class.java)
                    prefsManager.setUserName(user!!.username)
                    prefsManager.setUserImage(user.image)
                    prefsManager.setEmail(user.email)
                    prefsManager.setTole(user.tole)
                    prefsManager.setPhoneNumber(user.phone_number)
                    prefsManager.setState(user.state)
                    prefsManager.setWardNo(user.ward.toString())
                    prefsManager.setDeviceId("Device 1")
                    prefsManager.setDistrict(user.district)
                }
            }

        })
    }
}