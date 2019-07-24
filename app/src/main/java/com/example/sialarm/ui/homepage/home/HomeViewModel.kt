package com.example.sialarm.ui.homepage.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.sialarm.base.BaseViewModel
import com.example.sialarm.utils.AbsentLiveData
import com.example.sialarm.utils.Resource
import com.example.sialarm.utils.SingleLiveEvent

class HomeViewModel constructor(private val repository: HomeRepository):BaseViewModel<IHomeNavigator>() {



    var latitude = ""
    var longitude = ""
    val sendALertValid = MutableLiveData<Boolean>()

     val sendAlertMessages: LiveData<Resource<String>>  = Transformations
        .switchMap(sendALertValid){sendALertValid->
            if(sendALertValid){
                repository.sendAlertToAll(latitude,longitude)
            }else{
                AbsentLiveData.create()
            }
        }

    fun sendLocationUpdates( latitudes:String, longitudes:String){
        repository.sendUptoDateLatLong(latitudes,longitudes)
    }
}