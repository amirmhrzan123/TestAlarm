package com.si.sipanicalarm.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.si.sipanicalarm.base.BaseViewModel
import com.si.sipanicalarm.utils.AbsentLiveData
import com.si.sipanicalarm.utils.Resource

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