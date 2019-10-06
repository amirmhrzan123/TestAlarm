package com.si.sipanicalarm.ui.homepage.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.LocationResult

class LocalReceiver:BroadcastReceiver() {

    companion object {
        val ACTION_PROCESS_UPDATE = "update"
    }
    override fun onReceive(p0: Context?, p1: Intent?) {
       /* if(p1!=null){
            val action = p1!!.action
            if(action.equals(ACTION_PROCESS_UPDATE)){
                val result = LocationResult.extractResult(itnetn!!)
                if(result!=null){
                    val location = result.lastLocation
                    val location
                }
            }
        }*/
    }
}