package com.example.sialarm.ui.homepage.settings

import com.example.sialarm.data.api.ApiServices
import com.example.sialarm.data.prefs.PrefsManager
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope

class SettingRepository constructor(private val apiServices: ApiServices,
                                    private val viewModelScope:CoroutineScope,
                                    private val firebaseDatabase: FirebaseDatabase,
                                    private val prefsManager: PrefsManager){


}