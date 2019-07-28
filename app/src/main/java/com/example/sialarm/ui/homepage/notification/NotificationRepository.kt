package com.example.sialarm.ui.homepage.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sialarm.data.api.ApiServices
import com.example.sialarm.data.prefs.PrefsManager
import com.example.sialarm.utils.Resource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class NotificationRepository constructor(private val viewModelScope:CoroutineScope,
                                         private val prefsManager: PrefsManager,
                                         private val apiServices: ApiServices,
                                         private val firebaseData: FirebaseDatabase) {


    val listNotification : MutableList<NotificationResponseModel> = mutableListOf()


    fun getNotificationList():LiveData<Resource<List<NotificationResponseModel>>>{
        val notificationResponse = MutableLiveData<Resource<List<NotificationResponseModel>>>()
        firebaseData.getReference("Notification").child(prefsManager.getUserId())
            .addValueEventListener(object:ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {

                    if(p0.exists()){
                        listNotification.clear()
                        if(p0.exists()){
                            for(data in p0.children){
                                val notification : NotificationResponseModel? = data.getValue(NotificationResponseModel::class.java)
                                listNotification.add(notification!!)
                            }
                            notificationResponse.postValue(Resource.success("","",listNotification,null))
                        }else{
                            notificationResponse.postValue(Resource.success("","",listNotification,null))

                        }

                    }else{

                    }
                }

            })

        return notificationResponse
    }
}