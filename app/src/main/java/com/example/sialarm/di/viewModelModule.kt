package com.example.sialarm.di

import com.example.sialarm.ui.history.HistoryViewModel
import com.example.sialarm.ui.homepage.MainViewModel
import com.example.sialarm.ui.homepage.contacts.ContactsViewModel
import com.example.sialarm.ui.homepage.home.HomeViewModel
import com.example.sialarm.ui.homepage.notification.NotificationViewModel
import com.example.sialarm.ui.homepage.settings.SettingViewModel
import com.example.sialarm.ui.landingScreen.LandingScreenViewModel
import com.example.sialarm.ui.myProfile.MyProfileViewModel
import com.example.sialarm.utils.FirebaseData
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



}
