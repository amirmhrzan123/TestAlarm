package com.si.sipanicalarm.ui.homepage.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.si.sipanicalarm.base.BaseViewModel
import com.si.sipanicalarm.utils.AbsentLiveData
import com.si.sipanicalarm.utils.Resource
import com.si.sipanicalarm.utils.SingleLiveEvent

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