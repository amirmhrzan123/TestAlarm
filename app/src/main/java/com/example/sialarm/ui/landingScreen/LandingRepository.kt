package com.example.sialarm.ui.landingScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sialarm.data.firebase.Friends
import com.example.sialarm.data.firebase.PhoneNumber
import com.example.sialarm.data.firebase.Users
import com.example.sialarm.data.prefs.PrefsManager
import com.example.sialarm.utils.FireKey
import com.example.sialarm.utils.FirebaseData
import com.example.sialarm.utils.Resource
import com.google.firebase.database.*
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LandingRepository constructor(private val viewModelScope:CoroutineScope,
                                    private val prefsManager: PrefsManager,
                                    private val userDatabase: FirebaseDatabase) {

    fun insertUsers(userName:String, number:String, token:String):LiveData<Resource<String>>{
        val insertResponse = MutableLiveData<Resource<String>>()
        userDatabase.getReference(FireKey.USERS).orderByChild("phone_number").equalTo(number)
            .addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    Log.d("error","error")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if(!p0.exists()){
                        viewModelScope.launch {
                            withContext(IO){
                                try{

                                    val key = userDatabase.getReference(FireKey.USERS).push().key .toString()
                                    val user = Users(active = true,
                                        address = "",
                                        latitude = "",
                                        longitude = "",
                                        email = "",
                                        phone_number = number,
                                        username = userName,
                                        id = key,
                                        notification_token = token,
                                        device = "")

                                    userDatabase.getReference(FireKey.USERS).child(key).setValue(user).addOnCompleteListener {
                                        prefsManager.setLoginStatus(true)
                                        prefsManager.setUserId(key)
                                        prefsManager.setPhoneNumber(number)
                                        insertResponse.postValue(Resource.success("","",""))

                                    }

                                }catch(e:Throwable){

                                }
                            }
                        }

                    }else{
                        Log.d("childrenCount",p0.childrenCount.toString())
                        var user:Users?=null
                        for(data in p0.children){
                            user = data.getValue(Users::class.java)
                        }

                        userDatabase.getReference(FireKey.USERS).child(user!!.id)
                            .setValue(Users(user.active,user.address,
                                user.email,user.id,
                                user.latitude,user.longitude,
                                token,user.phone_number,
                                userName,user.device,user.timeStamp)).addOnCompleteListener {
                                prefsManager.setLoginStatus(true)
                                prefsManager.setUserId(user.id)
                                prefsManager.setUserName(user.username)
                                prefsManager.setPhoneNumber(number)
                                insertResponse.postValue(Resource.success("","",""))

                            }
                    }
                }

            })

        return insertResponse
    }

}