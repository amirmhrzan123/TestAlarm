package com.example.sialarm.ui.history

import com.example.sialarm.utils.extensions.getMessageDateTime

data class HistoryResponseModel(val title:String="",
                                val total_message_sent: Int=0,
                                val message: String="",
                                val timeStamp:Long=0,
                                val paid:Boolean=false){
    fun getDateTime():String{
        println(timeStamp.toString())
        return timeStamp.getMessageDateTime()
    }
}