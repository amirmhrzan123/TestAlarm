package com.si.sipanicalarm.ui.landingScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.si.sipanicalarm.data.firebase.Friends
import com.si.sipanicalarm.data.firebase.PhoneNumber
import com.si.sipanicalarm.data.firebase.Users
import com.si.sipanicalarm.data.prefs.PrefsManager
import com.si.sipanicalarm.data.room.NotificationCountTable
import com.si.sipanicalarm.data.room.dao.NotificationDao
import com.si.sipanicalarm.utils.FireKey
import com.si.sipanicalarm.utils.FirebaseData
import com.si.sipanicalarm.utils.Resource
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
                                        district = "",
                                        firsttime = false,
                                            isProfileComplete = false)

                                    userDatabase.getReference(FireKey.USERS).child(number).setValue(user).addOnCompleteListener {
                                        prefsManager.setLoginStatus(true)
                                        prefsManager.setUserId(number)
                                        prefsManager.setPhoneNumber(number)
                                        prefsManager.setUserName(userName)
                                        insertResponse.postValue(Resource.success("","","2"))

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
                        var firstime= true
                        if(user?.firsttime!=null){
                            firstime = user.firsttime!!
                        }

                        userDatabase.getReference(FireKey.USERS).child(number)
                            .setValue(Users(active = true,tole = user!!.tole,
                                email = user.email,id = user.id,
                                latitude = user.latitude,longitude = user.longitude,
                                notification_token = token,phone_number = user.phone_number,
                                username = userName,  timeStamp = user.timeStamp,device = user.device,
                                image = user.image, district = user.district,state = user.state,
                                firsttime = false,
                                ward = user.ward)).addOnCompleteListener {
                                prefsManager.setUserId(number)
                                prefsManager.setUserName(user.username)
                                prefsManager.setPhoneNumber(number)
                                    prefsManager.setLoginStatus(true)
                                prefsManager.setUserImage(user.image)
                                if(firstime){
                                    insertResponse.postValue(Resource.success("","","2"))
                                }else{
                                    if(user.isProfileComplete!!){
                                        insertResponse.postValue(Resource.success("","","3"))
                                    }else{
                                        insertResponse.postValue(Resource.success("","","4"))
                                    }
                                }

                            }
                    }
                }

            })

        return insertResponse
    }

}