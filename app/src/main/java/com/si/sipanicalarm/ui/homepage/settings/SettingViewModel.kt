package com.si.sipanicalarm.ui.homepage.settings

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.si.sipanicalarm.base.BaseViewModel
import com.si.sipanicalarm.data.api.SendAlertMessages
import com.si.sipanicalarm.data.api.SendSafeAlertMessages
import com.si.sipanicalarm.utils.AbsentLiveData
import com.si.sipanicalarm.utils.FirebaseData
import com.si.sipanicalarm.utils.Resource

class SettingViewModel constructor(private val repository: SettingRepository):BaseViewModel<ISettingNavigator>() {

    var settingValid = MutableLiveData<SendSafeAlertMessages>()
    var logoutValid = MutableLiveData<Boolean>()

    var latitude = ""
    var longitude = ""


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

    fun onAddDeviceClicked(){
        getNavigator().onAddDeviceClicked()
    }

    fun onChangePasswordClicked(){
        getNavigator().onChangePasswordClicked()
    }

    fun onEnablePasswordClicked(){
        getNavigator().onEnablePasswordClicked()
    }

    fun onPrivacyClicked(){
        getNavigator().onPrivacyClicked()
    }

    fun onTermsClicked(){
        getNavigator().onTermsConditionClicked()
    }

    val sendSafeAlert: LiveData<Resource<String>> = Transformations
        .switchMap(settingValid){settingValid->
            repository.sendSafeAlertMessage(settingValid)
        }

    val logout: LiveData<Resource<String>> = Transformations
        .switchMap(logoutValid){logoutValid->
            if(logoutValid){
                repository.logout()
            }else{
                AbsentLiveData.create()
            }
        }

}