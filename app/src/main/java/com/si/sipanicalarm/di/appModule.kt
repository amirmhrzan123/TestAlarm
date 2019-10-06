package com.si.sipanicalarm.di

import android.content.Context
import android.content.res.Resources
import com.si.sipanicalarm.data.api.ApiServices
import com.si.sipanicalarm.data.prefs.PrefsManager
import com.si.sipanicalarm.data.room.dao.NotificationDao
import com.si.sipanicalarm.ui.history.HistoryRepository
import com.si.sipanicalarm.ui.homepage.MainRepository
import com.si.sipanicalarm.ui.homepage.contacts.ContactsRepository
import com.si.sipanicalarm.ui.homepage.home.HomeRepository
import com.si.sipanicalarm.ui.homepage.notification.NotificationRepository
import com.si.sipanicalarm.ui.homepage.settings.SettingRepository
import com.si.sipanicalarm.ui.landingScreen.LandingRepository
import com.si.sipanicalarm.ui.myProfile.MyProfileRepository
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
    single { provideLandingRepository(get(),get(),get(),get())}
    single { provideContactsRepository(get(),get(),get(),get()) }
    single { provideNotificationRepository(get(),get(),get(),get())}
    single { provideHomeRepository(get(),get(),get(),get())}
    single { provideMyProfileRepository(get(),get(),get())}
    single { provideSettingsRepository(get(),get(),get(),get())}
    single { provideMainRepository(get(),get(),get(),get())}
    single { provideHistoryRepository(get(),get(),get())}
}

fun provideResources(context: Context): Resources = context.resources

fun provideLandingRepository(viewModelScope:CoroutineScope,
                             prefsManager: PrefsManager,
                             userDatabase:FirebaseDatabase,
                             notificationDao:NotificationDao):LandingRepository
= LandingRepository(viewModelScope,prefsManager,userDatabase,notificationDao)

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

fun provideMyProfileRepository(viewModelScope: CoroutineScope,
                               prefsManager: PrefsManager,firebaseDatabase: FirebaseDatabase):MyProfileRepository
= MyProfileRepository(viewModelScope,firebaseDatabase,prefsManager)


fun provideSettingsRepository(viewModelScope: CoroutineScope,firebaseDatabase: FirebaseDatabase,
                              prefsManager: PrefsManager,apiServices: ApiServices): SettingRepository
= SettingRepository(apiServices,viewModelScope,firebaseDatabase,prefsManager)

fun provideMainRepository(notificationDao: NotificationDao,
                          firebaseDatabase: FirebaseDatabase,
                          prefsManager: PrefsManager,
                          viewModelScope: CoroutineScope):MainRepository
= MainRepository(notificationDao,firebaseDatabase,prefsManager,viewModelScope)

fun provideHistoryRepository(viewModelScope: CoroutineScope,
                             firebaseDatabase: FirebaseDatabase,
                             prefsManager: PrefsManager):HistoryRepository
= HistoryRepository(firebaseDatabase,prefsManager,viewModelScope)

