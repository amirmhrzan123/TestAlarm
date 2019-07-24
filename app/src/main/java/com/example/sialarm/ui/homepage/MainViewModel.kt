package com.example.sialarm.ui.homepage

import com.example.sialarm.base.BaseViewModel
import com.example.sialarm.utils.SingleLiveEvent

class MainViewModel : BaseViewModel<IHomeNavigator>(){
    var contactName = ""
    var contactNumber = ""
    var contactTrigger = SingleLiveEvent<Boolean>()
    var stopTimer = SingleLiveEvent<Boolean>()

}