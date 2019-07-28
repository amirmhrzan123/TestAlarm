package com.example.sialarm.ui.homepage.notification

import com.example.sialarm.utils.extensions.getMessageDateTime
import java.sql.Timestamp

data class NotificationResponseModel(
    val title: String= "",
    val message:String="",
    val notification_type_id:String="",
    val sender_id:String="",
    val time: Long=0
){
    fun getDateTime():String{
        println(time.toString())
        return time.getMessageDateTime()
    }
}