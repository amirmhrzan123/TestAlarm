
package com.si.sipanicalarm.di

import com.si.sipanicalarm.ui.device.DeviceListViewModel
import com.si.sipanicalarm.ui.history.HistoryViewModel
import com.si.sipanicalarm.ui.homepage.MainViewModel
import com.si.sipanicalarm.ui.homepage.contacts.ContactsViewModel
import com.si.sipanicalarm.ui.homepage.home.HomeViewModel
import com.si.sipanicalarm.ui.homepage.notification.NotificationViewModel
import com.si.sipanicalarm.ui.homepage.settings.SettingViewModel
import com.si.sipanicalarm.ui.landingScreen.LandingScreenViewModel
import com.si.sipanicalarm.ui.myProfile.MyProfileViewModel
import com.si.sipanicalarm.ui.tutorial.TutorialViewModel
import com.si.sipanicalarm.ui.tutorial.firstTutorial.FirstStepViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModule = module {

    viewModel{MainViewModel(get())}

    viewModel { ContactsViewModel(get()) }

    viewModel { NotificationViewModel(get()) }

    viewModel { LandingScreenViewModel(get()) }

    viewModel { HomeViewModel(get()) }

    viewModel { MyProfileViewModel(get(),get(),get(),get())}

    viewModel {  SettingViewModel(get()) }

    viewModel { HistoryViewModel(get()) }

    viewModel { TutorialViewModel(get(),get())}

    viewModel { FirstStepViewModel() }

    viewModel { DeviceListViewModel() }


}
