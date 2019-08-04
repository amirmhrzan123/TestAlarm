package com.example.sialarm.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.sialarm.base.BaseViewModel
import com.example.sialarm.utils.AbsentLiveData
import com.example.sialarm.utils.Resource

class HistoryViewModel constructor(private val repository: HistoryRepository):BaseViewModel<IHistoryNavigator>() {


    var historyValid = MutableLiveData<Boolean>()

    val history : LiveData<Resource<List<HistoryResponseModel>>> = Transformations
        .switchMap(historyValid){
                historyValid->
            if(historyValid){
                repository.getNotificationList()
            }else{
                AbsentLiveData.create()
            }


        }

}