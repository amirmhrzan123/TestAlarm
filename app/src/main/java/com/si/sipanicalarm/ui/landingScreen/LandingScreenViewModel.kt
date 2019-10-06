package com.si.sipanicalarm.ui.landingScreen

import android.view.animation.Transformation
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.si.sipanicalarm.base.BaseViewModel
import com.si.sipanicalarm.utils.AbsentLiveData
import com.si.sipanicalarm.utils.Resource

class LandingScreenViewModel constructor(private val repository:LandingRepository): BaseViewModel<ILandingNavigator>() {
    var userName = ""
    var number = ""
    var token = ""
    var isValid = MutableLiveData<Boolean>()


    val insertUsers  : LiveData<Resource<String>> = Transformations
        .switchMap(isValid){isValid->
            if(isValid){
                repository.insertUsers(userName,number,token)
            }else{
                AbsentLiveData.create()
            }

        }

}