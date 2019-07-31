package com.example.sialarm.ui.homepage.settings

import com.example.sialarm.base.BaseViewModel

class SettingViewModel constructor(private val repository: SettingRepository):BaseViewModel<ISettingNavigator>() {

    fun onProfileClick(){
        getNavigator().onProfileClicked()
    }

    fun onLogoutClick(){
        getNavigator().onLogoutClicked()
    }

    fun onSafeAlertClick(){
        getNavigator().onSendSafeAlertClicked()
    }

}