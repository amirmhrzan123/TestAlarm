package com.example.sialarm.ui.homepage.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sialarm.data.api.ApiServices
import com.example.sialarm.data.api.SendAlertMessages
import com.example.sialarm.data.prefs.PrefsManager
import com.example.sialarm.di.viewModelModule
import com.example.sialarm.utils.Resource
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class HomeRepository constructor(private val apiServices: ApiServices,
                                 private val viewMOdelScope:CoroutineScope,
                                 private val prefsManager: PrefsManager,
                                 private val firebaseDatabase: FirebaseDatabase) {

    fun sendAlertToAll(latitude:String,longitude:String):LiveData<Resource<String>>{
        val sendAlertResponse  = MutableLiveData<Resource<String>>()
        viewMOdelScope.launch {
            try{
                sendAlertResponse.postValue(Resource.loading(null))
                val response = apiServices.sendAlertMessages(SendAlertMessages(prefsManager.getUserId(),
                    prefsManager.getUserName(),latitude,longitude))
                sendAlertResponse.postValue(Resource.success("","",null))

            }catch(e:Exception){
                sendAlertResponse.postValue(Resource.error("","",null))
            }
        }
        return sendAlertResponse

    }

    fun sendUptoDateLatLong(latitude:String,longitude:String){
        firebaseDatabase.getReference("EmergencyALert").child(prefsManager.getUserId())
            .setValue(EmergencyAlertModel(latitude,longitude))
    }


}