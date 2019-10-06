package com.si.sipanicalarm.ui.device

import androidx.databinding.ObservableField
import com.daimajia.androidanimations.library.BaseViewAnimator
import com.si.sipanicalarm.base.BaseViewModel

class DeviceListViewModel: BaseViewModel<IDeviceListNavigator>() {

    val searchText = ObservableField<String>("")

    val isSearchBarToVisible = ObservableField<Boolean>(false)


}