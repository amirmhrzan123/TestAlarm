package com.example.sialarm.ui.homepage.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Looper
import androidx.lifecycle.Observer
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.content.PermissionChecker.checkSelfPermission
import com.example.sialarm.BR
import com.example.sialarm.R
import com.example.sialarm.base.BaseFragment
import com.example.sialarm.databinding.FragmentAlertBinding
import com.example.sialarm.ui.homepage.MainViewModel
import com.example.sialarm.utils.Status
import com.example.sialarm.utils.extensions.isConnectingToInternet
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.kotlinpermissions.KotlinPermissions
import kotlinx.android.synthetic.main.fragment_alert.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.DateFormat
import java.util.*


class HomeFragment:BaseFragment<HomeViewModel,FragmentAlertBinding>() {

    private val homeViewModel: HomeViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.fragment_alert

    override fun getViewModel(): HomeViewModel = homeViewModel

    private val mainViewModel: MainViewModel by sharedViewModel()

    override fun getBindingVariable(): Int = BR.viewModel

    var down : Long = 0
    var runnable:Runnable?=null
    var counDownTimer : CountDownTimer?=null
    var time:Long  = 1500
    var buttonPressed = false



    companion object {
        /**
         * Code used in requesting runtime permissions.
         */
        const val REQUEST_PERMISSION_REQUEST_CODE = 34
        /**
         * Constant used in the location settings dialog.
         */
        const val REQUEST_CHECK_SETTINGS = 0x1
        /**
         * The desired interval for location updates. Inexact. Updates may be more or less frequent.
         */
        const val UPDATE_INTERVAL_IN_MILLISECONDS:Long = 10000
        /**
         * The fastest rate for active location updates. Exact. Updates will never be more frequent
         * than this value.
         */
        const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS:Long = UPDATE_INTERVAL_IN_MILLISECONDS / 2

        const val KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates"
        const val KEY_LOCATION = "location"
        const val KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string"


        /**
         * Provides access to the Fused Location Provider API.
         */
        @SuppressLint("StaticFieldLeak")
        lateinit var mFusedLocationClient : FusedLocationProviderClient


        @SuppressLint("StaticFieldLeak")
        lateinit var mSettingsClient: SettingsClient
        /**
         * Stores parameters for requests to the FusedLocationProviderApi.
         */

        lateinit var mLocationRequest: LocationRequest

        /**
         * Stores the types of location services the client is interested in using. Used for checking
         * settings to determine if the device has optimal location settings.
         */

        lateinit var mLocationSettingsRequest: LocationSettingsRequest


        /**
         * Callback for Location events.
         */
        lateinit var mLocationCallback: LocationCallback

        /**
         * Represents a geographical location.
         */

        lateinit var mCurrentLocation: Location

        /**
         * Tracks the status of the location updates request. Value changes when the user presses the
         * Start Updates and Stop Updates buttons.
         */

        var mRequestingLocationUpdates : Boolean = false

        /**
         * Time when the location was updated represented as a String.
         */

        var mLastUpdateTime: String = ""

        fun newInstance():HomeFragment{
            return HomeFragment()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
        mSettingsClient = LocationServices.getSettingsClient(activity!!)

        // Kick off the process of building the LocationCallback, LocationRequest, and
        // LocationSettingsRequest objects.
        createLocationCallback()
        createLocationRequest()
        buildLocationSettingsRequest()
        mainViewModel.stopTimer.observe(this@HomeFragment,Observer{
            counDownTimer=null
            btnUrgent.setBackgroundResource(R.drawable.button_unpressed)
            progress.visibility = View.INVISIBLE
        })


        KotlinPermissions.with(activity!!)
            .permissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
            .onAccepted { permissions ->
                if(permissions.size==2){
                    createLocationCallback()
                    createLocationRequest()
                    buildLocationSettingsRequest()
                }
            }
            .onDenied {
                //List of denied permissions
            }
            .onForeverDenied { permissions ->
                //List of forever denied permissions
            }
            .ask()

        homeViewModel.sendAlertMessages.observe(this@HomeFragment,Observer {
            when(it.status){
                Status.LOADING->{
                    showLoading("")
                }
                Status.SUCCESS->{
                    hideLoading()
                }
                Status.ERROR->{
                    hideLoading()
                }
            }
        })

        runnable = Runnable{
            btnUrgent.setBackgroundResource(R.drawable.button_unpressed)
        }
            btnUrgent.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if(counDownTimer==null){
                            progress.visibility = View.VISIBLE
                            counDownTimer =object: CountDownTimer(time,10){
                                override fun onFinish() {
                                    counDownTimer=null
                                    btnUrgent.setBackgroundResource(R.drawable.button_unpressed)
                                    progress.visibility = View.INVISIBLE
                                    buttonPressed = true
                                    if (!mRequestingLocationUpdates) {
                                        mRequestingLocationUpdates = true
                                        startLocationUpdates()
                                    }
                                }

                                override fun onTick(millisUntilFinished: Long) {
                                    val finishedSeconds = time - millisUntilFinished
                                    val total = (finishedSeconds.toFloat() / time.toFloat()  * 100.0).toInt()
                                    progress.progress = total
                                    Log.d("time",total.toString())
                                }
                            }.start()
                        }
                        btnUrgent.setBackgroundResource(R.drawable.button_pressed)
                        progress.visibility = View.VISIBLE
                        down = System.currentTimeMillis()

                    }
                    MotionEvent.ACTION_UP -> {
                        progress.visibility = View.GONE
                        btnUrgent.setBackgroundResource(R.drawable.button_unpressed)
                        progress.progress = 0
                        counDownTimer?.cancel()
                        counDownTimer = null
                        progress.visibility = View.INVISIBLE
                        return@OnTouchListener true
                    }
                }
                false
            })
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * `ACCESS_COARSE_LOCATION` and `ACCESS_FINE_LOCATION`. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     *
     *
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     *
     *
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.interval = UPDATE_INTERVAL_IN_MILLISECONDS

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS

        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    /**
     * Creates a callback for receiving location events.
     */
    private fun createLocationCallback() {
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)

                mCurrentLocation = locationResult!!.lastLocation
                mLastUpdateTime = DateFormat.getTimeInstance().format(Date())
                homeViewModel.longitude = mCurrentLocation.longitude.toString()
                homeViewModel.latitude = mCurrentLocation.latitude.toString()

                if(buttonPressed){
                    if(isConnectingToInternet(activity!!)){
                        homeViewModel.sendALertValid.value = true
                    }else{
                        KotlinPermissions.with(activity!!)
                            .permissions(Manifest.permission.SEND_SMS)
                            .onAccepted { permissions ->

                            }
                            .onDenied {
                                //List of denied permissions
                            }
                            .onForeverDenied { permissions ->
                                //List of forever denied permissions
                            }
                            .ask()
                    }
                    buttonPressed = false
                }

                if(isConnectingToInternet(activity!!)){
                    homeViewModel.sendLocationUpdates(mCurrentLocation.latitude.toString(),
                        mCurrentLocation.longitude.toString())
                }

                //updateLocationUI()
            }
        }
    }

    /**
     * Uses a [com.google.android.gms.location.LocationSettingsRequest.Builder] to build
     * a [com.google.android.gms.location.LocationSettingsRequest] that is used for checking
     * if a device has the needed location settings.
     */
    private fun buildLocationSettingsRequest() {
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest)
        mLocationSettingsRequest = builder.build()
    }


    override fun onActivityResult(requestCode:Int, resultCode:Int, data:Intent? ) {
        when (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            REQUEST_CHECK_SETTINGS -> when (resultCode) {
                Activity.RESULT_OK -> Log.i("", "User agreed to make required location settings changes.")
                Activity.RESULT_CANCELED -> {
                    Log.i("", "User chose not to make required location settings changes.")
                    mRequestingLocationUpdates = false
                }
            }// Nothing to do. startLocationupdates() gets called in onResume again.
        }
    }

    /**
     * Requests location updates from the FusedLocationApi. Note: we don't call this unless location
     * runtime permission has been granted.
     */
    fun startLocationUpdates(){
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
            .addOnSuccessListener(activity!!,object:OnSuccessListener<LocationSettingsResponse>{
                override fun onSuccess(p0: LocationSettingsResponse?) {

                    if (checkSelfPermission(activity!!,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                            activity!!,Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // TODO: Consider calling
                        //    Activity#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        return
                    }
                    mFusedLocationClient.requestLocationUpdates(
                        mLocationRequest,
                        mLocationCallback, Looper.myLooper()
                    )

                }

            })
            .addOnFailureListener(activity!!,object:OnFailureListener{
                override fun onFailure(e: Exception) {
                    val  statusCode =(e as ApiException).getStatusCode();
                    when (statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                            Log.i(
                                "",
                                "Location settings are not satisfied. Attempting to upgrade " + "location settings "
                            )
                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the
                                // result in onActivityResult().
                                val rae = e as ResolvableApiException
                                rae.startResolutionForResult(activity!!, REQUEST_CHECK_SETTINGS)
                            } catch (sie: IntentSender.SendIntentException) {
                                Log.i("", "PendingIntent unable to execute request.")
                            }

                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            val errorMessage =
                                "Location settings are inadequate, and cannot be " + "fixed here. Fix in Settings."
                            Log.e("", errorMessage)
                            Toast.makeText(activity!!, errorMessage, Toast.LENGTH_LONG).show()
                            mRequestingLocationUpdates = false
                        }
                    }
                }
            })
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    private fun stopLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            Log.d("", "stopLocationUpdates: updates never requested, no-op.")
            return
        }

        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.

        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
            .addOnCompleteListener(activity!!, OnCompleteListener {
                mRequestingLocationUpdates = false
            })
    }

    private fun getPendingIntent(): PendingIntent?{
        val intent = Intent(activity!!,LocalReceiver::class.java)
        intent.action = LocalReceiver.ACTION_PROCESS_UPDATE
        return PendingIntent.getBroadcast(activity!!,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
    }
}