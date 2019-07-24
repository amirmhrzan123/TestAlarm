package com.example.sialarm.di

import com.example.sialarm.ui.homepage.MainViewModel
import com.example.sialarm.ui.homepage.contacts.ContactsViewModel
import com.example.sialarm.ui.homepage.home.HomeViewModel
import com.example.sialarm.ui.homepage.notification.NotificationViewModel
import com.example.sialarm.ui.landingScreen.LandingScreenViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModule = module {

    viewModel{MainViewModel()}

    viewModel { ContactsViewModel(get()) }

    viewModel { NotificationViewModel(get()) }

    viewModel { LandingScreenViewModel(get()) }

    viewModel { HomeViewModel(get()) }


}
