package com.si.sipanicalarm.ui.myProfile

import androidx.lifecycle.MutableLiveData
import com.si.sipanicalarm.data.firebase.Users
import com.si.sipanicalarm.data.prefs.PrefsManager
import com.si.sipanicalarm.utils.Resource
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.Exception

class MyProfileRepository constructor(private val viewModelScope:CoroutineScope,
                                      private val firebaseDatabase: FirebaseDatabase,
                                      private val prefsManager: PrefsManager) {



    fun updateProfile(user: Users):MutableLiveData<Resource<String>>{
        val response = MutableLiveData<Resource<String>>()
        viewModelScope.launch {

            try{
                response.postValue(Resource.loading(""))
                firebaseDatabase.getReference("users/"+prefsManager.getUserId()+"/email").setValue(user.email)
                firebaseDatabase.getReference("users/"+prefsManager.getUserId()+"/state").setValue(user.state)
                firebaseDatabase.getReference("users/"+prefsManager.getUserId()+"/district").setValue(user.district)
                firebaseDatabase.getReference("users/"+prefsManager.getUserId()+"/tole").setValue(user.tole)
                firebaseDatabase.getReference("users/"+prefsManager.getUserId()+"/ward").setValue(user.ward)
                firebaseDatabase.getReference("users/"+prefsManager.getUserId()+"/username").setValue(user.username)
                firebaseDatabase.getReference("users/"+prefsManager.getUserId()+"/device").setValue(user.device)
                firebaseDatabase.getReference("users/"+prefsManager.getUserId()+"/isProfileComplete").setValue(true)
                firebaseDatabase.getReference("users/"+prefsManager.getUserId()+"/deviceName").setValue(user.deviceName)
                prefsManager.setEmail(user.email)
                prefsManager.setState(user.state)
                prefsManager.setDistrict(user.district)
                prefsManager.setWardNo(user.ward.toString())
                prefsManager.setTole(user.tole)
                prefsManager.setDeviceName(user.deviceName!!)
                prefsManager.setDeviceId(user.device!!)
                prefsManager.setAdmin(false)
                prefsManager.setLoginStatus(true)
                prefsManager.setProfileComplete(true)
                response.postValue(Resource.success("","",null))
            }catch(e:Exception){
                response.postValue(Resource.error("","",null))
            }
        }
        return response

    }



}