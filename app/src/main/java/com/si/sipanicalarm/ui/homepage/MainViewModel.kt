package com.si.sipanicalarm.ui.homepage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.bumptech.glide.request.target.NotificationTarget
import com.si.sipanicalarm.base.BaseViewModel
import com.si.sipanicalarm.data.room.NotificationCountTable
import com.si.sipanicalarm.utils.AbsentLiveData
import com.si.sipanicalarm.utils.SingleLiveEvent

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