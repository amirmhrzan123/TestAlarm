package com.si.sipanicalarm.ui.homepage.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.si.sipanicalarm.data.api.ApiServices
import com.si.sipanicalarm.data.api.SendAlertMessages
import com.si.sipanicalarm.data.api.SendSafeAlertMessages
import com.si.sipanicalarm.data.prefs.PrefsManager
import com.si.sipanicalarm.utils.Resource
import com.si.sipanicalarm.utils.extensions.handleException
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


    fun logout():LiveData<Resource<String>>{
        val logoutResponse = MutableLiveData<Resource<String>>()
        firebaseDatabase.getReference("users").child(prefsManager.getUserId())
            .child("notification_token").setValue("")
            .addOnCompleteListener {
                logoutResponse.postValue(Resource.success("Logout","You are logout successfully.",""))
            }
            .addOnFailureListener {
                logoutResponse.postValue(Resource.error("Logout","Something went wrong",""))
            }
        return logoutResponse
    }


}