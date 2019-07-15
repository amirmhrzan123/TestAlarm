package com.example.sialarm.ui.homepage.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.sialarm.base.BaseViewModel
import com.example.sialarm.utils.AbsentLiveData
import com.example.sialarm.utils.Resource

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