package com.example.sialarm.di

import android.content.Context
import android.content.res.Resources
import com.example.sialarm.data.api.ApiServices
import com.example.sialarm.data.prefs.PrefsManager
import com.example.sialarm.ui.homepage.contacts.ContactsRepository
import com.example.sialarm.ui.homepage.home.HomeRepository
import com.example.sialarm.ui.homepage.notification.NotificationRepository
import com.example.sialarm.ui.landingScreen.LandingRepository
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.dsl.module.module

/**
 * app module koin
 */
val appModule = module {
    single { provideResources(get()) }
    single { CoroutineScope(Dispatchers.Main + Job()) }
    single { provideLandingRepository(get(),get(),get())}
    single { provideContactsRepository(get(),get(),get(),get()) }
    single { provideNotificationRepository(get(),get(),get(),get())}
    single { provideHomeRepository(get(),get(),get(),get())}
}

fun provideResources(context: Context): Resources = context.resources

fun provideLandingRepository(viewModelScope:CoroutineScope,
                             prefsManager: PrefsManager,
                             userDatabase:FirebaseDatabase):LandingRepository
= LandingRepository(viewModelScope,prefsManager,userDatabase)

fun provideContactsRepository(viewModelScope:CoroutineScope,
                              prefsManager: PrefsManager,
                              firebaseDatabase: FirebaseDatabase,apiServices: ApiServices):ContactsRepository
= ContactsRepository(firebaseDatabase,viewModelScope,prefsManager,apiServices)

fun provideNotificationRepository(viewModelScope: CoroutineScope,firebaseDatabase: FirebaseDatabase,
                                  prefsManager: PrefsManager,apiServices: ApiServices): NotificationRepository
= NotificationRepository(viewModelScope,prefsManager,apiServices,firebaseDatabase)

fun provideHomeRepository(viewModelScope: CoroutineScope,firebaseDatabase: FirebaseDatabase,
                          prefsManager: PrefsManager,apiServices: ApiServices):HomeRepository
= HomeRepository(apiServices,viewModelScope,prefsManager,firebaseDatabase)