package com.example.sialarm.ui.myProfile

import androidx.lifecycle.MutableLiveData
import com.example.sialarm.data.firebase.Users
import com.example.sialarm.data.prefs.PrefsManager
import com.example.sialarm.utils.Resource
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
                prefsManager.setEmail(user.email)
                prefsManager.setState(user.state)
                prefsManager.setDistrict(user.district)
                prefsManager.setWardNo(user.ward.toString())
                prefsManager.setTole(user.tole)
                response.postValue(Resource.success("","",null))
            }catch(e:Exception){
                response.postValue(Resource.error("","",null))
            }
        }
        return response

    }


}