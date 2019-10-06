package com.si.sipanicalarm.ui.homepage.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.si.sipanicalarm.data.api.ApiServices
import com.si.sipanicalarm.data.api.SendAlertMessages
import com.si.sipanicalarm.data.prefs.PrefsManager
import com.si.sipanicalarm.di.viewModelModule
import com.si.sipanicalarm.utils.Resource
import com.si.sipanicalarm.utils.extensions.handleException
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class HomeRepository constructor(private val apiServices: ApiServices,
                                 private val viewMOdelScope:CoroutineScope,
                                 private val prefsManager: PrefsManager,
                                 private val firebaseDatabase: FirebaseDatabase) {

    fun sendAlertToAll(latitude:String,longitude:String, test:Boolean = false):LiveData<Resource<String>>{
        val sendAlertResponse  = MutableLiveData<Resource<String>>()
        viewMOdelScope.launch {
            try{
                sendAlertResponse.postValue(Resource.loading(null))
                val response = apiServices.sendAlertMessages(SendAlertMessages(prefsManager.getUserId(),
                    prefsManager.getUserName(),latitude,longitude, test))
                sendAlertResponse.postValue(Resource.success("","",null))

            }catch(e:Exception){
                e.handleException(""){
                    sendAlertResponse.postValue(Resource.error("",it.message,null))
                }
            }
        }
        return sendAlertResponse

    }

    fun sendUptoDateLatLong(latitude:String,longitude:String){
        firebaseDatabase.getReference("EmergencyALert").child(prefsManager.getUserId())
            .setValue(EmergencyAlertModel(latitude,longitude))
    }


}