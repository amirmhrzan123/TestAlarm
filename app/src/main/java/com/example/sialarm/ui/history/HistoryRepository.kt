package com.example.sialarm.ui.history

import com.example.sialarm.data.prefs.PrefsManager
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope

class HistoryRepository constructor(private val firebaseDatabase: FirebaseDatabase,
                                    private val prefsManager: PrefsManager,
                                    private val viewModelScoper:CoroutineScope) {


}