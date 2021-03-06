package com.example.sialarm.di

import android.content.Context
import android.content.SharedPreferences
import com.example.sialarm.data.prefs.PrefsManager
import com.example.sialarm.data.prefs.PrefsManagerImpl
import com.example.sialarm.di.PersistenceDataSourceProperties.PREF_NAME
import com.example.sialarm.utils.FireKey
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.dsl.module.module

/**
 * Remote Web Service dataSource
 */



val persistenceDataModule = module {
    single { provideSharePreference(get(), "SIAlarm") }
    single { providePrefsManager(get()) as PrefsManager }
    single { provideFirebaseStore()}
    single { provideFirebaseDatabase()}
}

object PersistenceDataSourceProperties {
    const val PREF_NAME = "PREF_NAME"
    const val DB_NAME = "DB_NAME"
}


fun provideSharePreference(context: Context, prefName: String): SharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)

fun providePrefsManager(pref: SharedPreferences) = PrefsManagerImpl(pref)

fun provideFirebaseStore() = FirebaseFirestore.getInstance()

fun provideFirebaseDatabase():FirebaseDatabase = FirebaseDatabase.getInstance()



