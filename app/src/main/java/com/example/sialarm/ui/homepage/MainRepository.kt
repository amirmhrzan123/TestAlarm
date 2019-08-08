package com.example.sialarm.ui.homepage

import androidx.lifecycle.LiveData
import com.example.sialarm.data.firebase.Users
import com.example.sialarm.data.prefs.PrefsManager
import com.example.sialarm.data.room.NotificationCountTable
import com.example.sialarm.data.room.dao.NotificationDao
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainRepository constructor(private val notificationDao:NotificationDao,
                                 private val firebaseDatabase: FirebaseDatabase,
                                 private val prefsManager: PrefsManager,
                                 private val viewModelScope: CoroutineScope) {

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

    fun resetNotification() {
        viewModelScope.launch {
            withContext(IO) {
                notificationDao.updateNotificationCount(NotificationCountTable("1", 0))
            }
        }
    }




    fun getNotificationCount(): LiveData<NotificationCountTable> {
        return notificationDao.getNotificationCount("1")
    }
}