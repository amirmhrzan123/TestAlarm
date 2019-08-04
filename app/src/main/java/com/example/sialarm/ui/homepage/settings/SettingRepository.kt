package com.example.sialarm.ui.homepage.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sialarm.data.api.ApiServices
import com.example.sialarm.data.api.SendAlertMessages
import com.example.sialarm.data.api.SendSafeAlertMessages
import com.example.sialarm.data.prefs.PrefsManager
import com.example.sialarm.utils.Resource
import com.example.sialarm.utils.extensions.handleException
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SettingRepository constructor(private val apiServices: ApiServices,
                                    private val viewModelScope:CoroutineScope,
                                    private val firebaseDatabase: FirebaseDatabase,
                                    private val prefsManager: PrefsManager){

    fun sendSafeAlertMessage(sendAlertMessages: SendSafeAlertMessages):LiveData<Resource<String>>{
        sendAlertMessages.userName = prefsManager.getUserName()
        sendAlertMessages.sender_id = prefsManager.getUserId()
        val sendSafeAlertResponse = MutableLiveData<Resource<String>>()
        viewModelScope.launch {
            try{
                sendSafeAlertResponse.postValue(Resource.loading(null))
                val response = apiServices.sendSafeAlertMessages(sendAlertMessages)
                sendSafeAlertResponse.postValue(Resource.success("","",null))
            }catch(e:Exception){
                e.handleException(""){
                    sendSafeAlertResponse.postValue(Resource.error("","",null))
                }
            }
        }
        return sendSafeAlertResponse
    }


}