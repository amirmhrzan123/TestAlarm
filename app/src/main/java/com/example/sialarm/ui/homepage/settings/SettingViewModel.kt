package com.example.sialarm.ui.homepage.settings

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.sialarm.base.BaseViewModel
import com.example.sialarm.data.api.SendAlertMessages
import com.example.sialarm.data.api.SendSafeAlertMessages
import com.example.sialarm.utils.FirebaseData
import com.example.sialarm.utils.Resource

class SettingViewModel constructor(private val repository: SettingRepository):BaseViewModel<ISettingNavigator>() {

    var settingValid = MutableLiveData<SendSafeAlertMessages>()


    fun onProfileClick(){
        getNavigator().onProfileClicked()
    }

    fun onLogoutClick(){
        getNavigator().onLogoutClicked()
    }

    fun onSafeAlertClick(){
        getNavigator().onSendSafeAlertClicked()
    }

    fun onHistoryClick(){
        getNavigator().onHistoryClicked()
    }

    val sendSafeAlert: LiveData<Resource<String>> = Transformations
        .switchMap(settingValid){settingValid->
            repository.sendSafeAlertMessage(settingValid)
        }

}