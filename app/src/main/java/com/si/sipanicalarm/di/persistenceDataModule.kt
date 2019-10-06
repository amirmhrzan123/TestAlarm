package com.si.sipanicalarm.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.si.sipanicalarm.data.prefs.PrefsManager
import com.si.sipanicalarm.data.prefs.PrefsManagerImpl
import com.si.sipanicalarm.data.room.AppDatabase
import com.si.sipanicalarm.data.room.dao.NotificationDao
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.dsl.module.module

/**
 * Remote Web Service dataSource
 */



val persistenceDataModule = module {
    single { provideSharePreference(get(), "SIAlarm") }
    single { providePrefsManager(get()) as PrefsManager }
    factory { provideFirebaseDatabase()}
    single { provideRoomDatabase(get(),"SIAlarm")}
    single { provideNotificationDao(get()) }
}

object PersistenceDataSourceProperties {
    const val PREF_NAME = "PREF_NAME"
    const val DB_NAME = "DB_NAME"
}


fun provideSharePreference(context: Context, prefName: String): SharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)

fun providePrefsManager(pref: SharedPreferences) = PrefsManagerImpl(pref)


fun provideFirebaseDatabase():FirebaseDatabase {
    val firebaseDatabse = FirebaseDatabase.getInstance()
    return firebaseDatabse
}

/**
 * TODO Provide the instance of room database
 * @param context
 */
fun provideRoomDatabase(context: Context, dbName: String): AppDatabase {
    return Room.databaseBuilder(context, AppDatabase::class.java, dbName).fallbackToDestructiveMigration().build()
}

fun provideNotificationDao(appDatabase: AppDatabase): NotificationDao = appDatabase.getNotificationDao()



