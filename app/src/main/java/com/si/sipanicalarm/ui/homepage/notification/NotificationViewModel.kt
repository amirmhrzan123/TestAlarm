package com.si.sipanicalarm.ui.homepage.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.si.sipanicalarm.base.BaseViewModel
import com.si.sipanicalarm.utils.AbsentLiveData
import com.si.sipanicalarm.utils.Resource

class NotificationViewModel constructor(private val repository:NotificationRepository):BaseViewModel<INotificationNavigator>() {

    var notificationValid = MutableLiveData<Boolean>()

    val notification : LiveData<Resource<List<NotificationResponseModel>>> = Transformations
        .switchMap(notificationValid){
            notificationValid->
            if(notificationValid){
                repository.getNotificationList()
            }else{
                AbsentLiveData.create()
            }


        }

}