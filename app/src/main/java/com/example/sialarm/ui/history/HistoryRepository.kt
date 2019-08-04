package com.example.sialarm.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sialarm.data.prefs.PrefsManager
import com.example.sialarm.utils.Resource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope

class HistoryRepository constructor(private val firebaseDatabase: FirebaseDatabase,
                                    private val prefsManager: PrefsManager,
                                    private val viewModelScoper:CoroutineScope) {

    val listHistory : MutableList<HistoryResponseModel> = mutableListOf()


    fun getNotificationList(): LiveData<Resource<List<HistoryResponseModel>>> {
        val historyResponse = MutableLiveData<Resource<List<HistoryResponseModel>>>()
        firebaseDatabase.getReference("History").child(prefsManager.getUserId())
            .addValueEventListener(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {

                    if(p0.exists()){
                        listHistory.clear()
                        if(p0.exists()){
                            for(data in p0.children){
                                val history : HistoryResponseModel? = data.getValue(HistoryResponseModel::class.java)
                                listHistory.add(history!!)
                            }
                            historyResponse.postValue(Resource.success("","",listHistory,null))
                        }else{
                            historyResponse.postValue(Resource.success("","",listHistory,null))

                        }

                    }else{
                        historyResponse.postValue(Resource.error("","",null))

                    }
                }

            })

        return historyResponse
    }
}