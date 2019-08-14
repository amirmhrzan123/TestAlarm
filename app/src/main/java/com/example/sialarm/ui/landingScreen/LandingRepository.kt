package com.example.sialarm.ui.landingScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sialarm.data.firebase.Friends
import com.example.sialarm.data.firebase.PhoneNumber
import com.example.sialarm.data.firebase.Users
import com.example.sialarm.data.prefs.PrefsManager
import com.example.sialarm.data.room.NotificationCountTable
import com.example.sialarm.data.room.dao.NotificationDao
import com.example.sialarm.utils.FireKey
import com.example.sialarm.utils.FirebaseData
import com.example.sialarm.utils.Resource
import com.google.firebase.database.*
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LandingRepository constructor(private val viewModelScope:CoroutineScope,
                                    private val prefsManager: PrefsManager,
                                    private val userDatabase: FirebaseDatabase,
                                    private val notificationDao:NotificationDao) {

    fun insertUsers(userName:String, number:String, token:String):LiveData<Resource<String>>{
        viewModelScope.launch {
            withContext(IO){
                notificationDao.setNotificationCount(NotificationCountTable("1",0))
            }
        }
        val insertResponse = MutableLiveData<Resource<String>>()
        userDatabase.getReference(FireKey.USERS).orderByChild("phone_number").equalTo(number)
            .addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    Log.d("error","error")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if(!p0.exists()){
                        viewModelScope.launch {
                                try{

                                    val user = Users(active = true,
                                        tole = "",
                                        latitude = "",
                                        longitude = "",
                                        email = "",
                                        phone_number = number,
                                        username = userName,
                                        id = number,
                                        notification_token = token,
                                        device = "",
                                        image = "",
                                        state = "",
                                        ward = 0,
                                        district = "")

                                    userDatabase.getReference(FireKey.USERS).child(number).setValue(user).addOnCompleteListener {
                                        prefsManager.setLoginStatus(true)
                                        prefsManager.setUserId(number)
                                        prefsManager.setPhoneNumber(number)
                                        prefsManager.setUserName(userName)
                                        insertResponse.postValue(Resource.success("","",""))

                                    }

                                }catch(e:Throwable){
                                    insertResponse.postValue(Resource.error("","",null))
                                }
                            }


                    }else{
                        Log.d("childrenCount",p0.childrenCount.toString())
                        var user:Users?=null
                        for(data in p0.children){
                            user = data.getValue(Users::class.java)
                        }

                        userDatabase.getReference(FireKey.USERS).child(number)
                            .setValue(Users(active = true,tole = user!!.tole,
                                email = user.email,id = user.id,
                                latitude = user.latitude,longitude = user.longitude,
                                notification_token = token,phone_number = user.phone_number,
                                username = userName,  timeStamp = user.timeStamp,device = user.device,
                                image = user.image, district = user.district,state = user.state,
                                ward = user.ward)).addOnCompleteListener {
                                prefsManager.setLoginStatus(true)
                                prefsManager.setUserId(number)
                                prefsManager.setUserName(user.username)
                                prefsManager.setPhoneNumber(number)
                                prefsManager.setUserImage(user.image)
                                insertResponse.postValue(Resource.success("","",""))

                            }
                    }
                }

            })

        return insertResponse
    }

}