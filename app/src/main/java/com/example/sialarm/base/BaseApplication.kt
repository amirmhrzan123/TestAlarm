package com.example.sialarm.base

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.example.sialarm.di.apiModule
import com.example.sialarm.di.appModule
import com.example.sialarm.di.persistenceDataModule
import com.example.sialarm.di.viewModelModule
import com.facebook.accountkit.AccountKit
import com.facebook.accountkit.AccountKitCallback
import com.facebook.stetho.DumperPluginsProvider
import com.facebook.stetho.Stetho
import com.facebook.stetho.dumpapp.DumperPlugin
import com.google.firebase.database.FirebaseDatabase
import org.koin.android.ext.android.startKoin

class BaseApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        // start Koin context
//        Stetho.initializeWithDefaults(this)

        MultiDex.install(this)
        Stetho.initialize(
            Stetho.newInitializerBuilder(this)
                .enableDumpapp(SampleDumperPluginProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());
        startKoin(this, listOf(appModule, apiModule, persistenceDataModule,  viewModelModule), mapOf(), true)

        val firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseDatabase.setPersistenceEnabled(true)
    }

    private class SampleDumperPluginProvider(private val mContext: Context) : DumperPluginsProvider {

        override fun get(): Iterable<DumperPlugin> {
            val plugins = arrayListOf<DumperPlugin>()
            for (defaultPlugin in Stetho.defaultDumperPluginsProvider(mContext).get()) {
                plugins.add(defaultPlugin)
            }
            return plugins
        }
    }
}