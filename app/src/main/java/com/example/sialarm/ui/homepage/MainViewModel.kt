package com.example.sialarm.ui.homepage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.bumptech.glide.request.target.NotificationTarget
import com.example.sialarm.base.BaseViewModel
import com.example.sialarm.data.room.NotificationCountTable
import com.example.sialarm.utils.AbsentLiveData
import com.example.sialarm.utils.SingleLiveEvent

class MainViewModel constructor(private val repository:MainRepository) : BaseViewModel<IHomeNavigator>(){

    var contactName = ""
    var contactNumber = ""
    var contactTrigger = SingleLiveEvent<Boolean>()
    var stopTimer = SingleLiveEvent<Boolean>()
    var getNotificationCountValid = MutableLiveData<Boolean>()

    fun saveUsers(){
        repository.saveUsers()
    }

    fun resetNotificationCount(){
        repository.resetNotification()
    }

    val getNotificationResponse: LiveData<NotificationCountTable> = Transformations
        .switchMap(getNotificationCountValid){
                getNotificationCountValid->
            if(getNotificationCountValid){
                repository.getNotificationCount()
            }else{
                AbsentLiveData.create()
            }
        }

}