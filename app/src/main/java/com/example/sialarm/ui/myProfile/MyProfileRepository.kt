package com.example.sialarm.ui.myProfile

import com.example.sialarm.data.prefs.PrefsManager
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope

class MyProfileRepository constructor(private val viewModelScope:CoroutineScope,
                                      private val firebaseDatabase: FirebaseDatabase,
                                      private val prefsManager: PrefsManager) {




}