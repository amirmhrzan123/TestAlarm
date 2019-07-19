package com.example.sialarm.service

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.annotation.RequiresApi
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class LocationJobService : JobService(),GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {

    lateinit var handler: Handler
    lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    lateinit var mLocationRequest: LocationRequest
    lateinit var mLocationCallback: LocationCallback
    lateinit var jobParameters: JobParameters
    var isJobRunning = false
    lateinit var mGoogleApiClient: GoogleApiClient
    var updatesList = ArrayList<Location>()


    companion object {
        const val LOCATION_SERVICE_JOB_ID = 111
        const val ACTION_STOP_JOB = "actionStopJob"
    }

    private val stopJobReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            if(intent?.action!=null && intent.action.equals(ACTION_STOP_JOB)){

            }
        }

    }

    override fun onStopJob(p0: JobParameters?): Boolean {

    }

    override fun onStartJob(p0: JobParameters?): Boolean {
    }

    override fun onConnected(p0: Bundle?) {
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }
}