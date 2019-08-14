package com.example.sialarm.ui.device

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker
import com.example.sialarm.R
import com.example.sialarm.ui.district.SearchDistrictActivity
import com.example.sialarm.utils.customViews.CustomSpinnerDialog
import com.example.sialarm.utils.customViews.CustomSpinnerWardAdapter
import com.example.sialarm.utils.extensions.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_add_device.*
import java.text.DateFormat
import java.util.*

class AddDeviceActivity :AppCompatActivity() {

    var latitude = ""
    var longitude = ""

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

        fun newInstance(activity: Activity){
            activity.startActivity(Intent(activity,AddDeviceActivity::class.java))
        }
    }

    var selectedId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_device)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mSettingsClient = LocationServices.getSettingsClient(this)

        createLocationCallback()
        createLocationRequest()
        buildLocationSettingsRequest()
        etDistrict.setOnClickListener {
            startActivityForResult(Intent(this, SearchDistrictActivity::class.java),3)

        }

        etWardNumber.setOnClickListener {
            val wardList = arrayListOf<Int>(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,29,30,31,32,33,34,35)

            val dialog = CustomSpinnerDialog.getInstanceForWard("Choose ward number", wardList as ArrayList<Int>, 0)
            dialog.setListener(object : CustomSpinnerWardAdapter.onCustomSpinnerItemSelected {
                override fun onItemSelected(selectedId: Int, position: Int) {
                    // myProfileViewModel.selectedWardId = selectedId
                    etWardNumber.setText(selectedId.toString())
                }
            })
            dialog.show(supportFragmentManager, "")
        }

        set.setOnClickListener {
            startLocationUpdates()
        }

        add.setOnClickListener {
            if(isConnectingToInternet(this)){
                val key = FirebaseDatabase.getInstance().getReference("deviceList").push().key
                FirebaseDatabase.getInstance().getReference("deviceList").child(key!!).setValue(Device(id = etPhone.text.toString(),
                    name = deviceName.text.toString(),district = etDistrict.text.toString(),
                    ward = etWardNumber.text.toString().toInt(),
                    latitude = latitude,
                    longitude = longitude))

                FirebaseDatabase.getInstance().getReference("deviceStatus").child(etPhone.text.toString())
                    .child("status").setValue("false")

                FirebaseDatabase.getInstance().getReference("deviceStatus").child(etPhone.text.toString())
                    .child("sender_id").setValue("")

                finish()
            }else{
                showValidationDialog("SI alarm","No internet connection")
            }

        }
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
                longitude = mCurrentLocation.longitude.toString()
                latitude = mCurrentLocation.latitude.toString()
                tvLatLong.text = "Lat:${latitude}, Lon:${longitude}"
                stopLocationUpdates()
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


    /**
     * Requests location updates from the FusedLocationApi. Note: we don't call this unless location
     * runtime permission has been granted.
     */
    fun startLocationUpdates(){
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this,object: OnSuccessListener<LocationSettingsResponse> {
                    override fun onSuccess(p0: LocationSettingsResponse?) {

                        if (PermissionChecker.checkSelfPermission(this@AddDeviceActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && PermissionChecker.checkSelfPermission(
                                        this@AddDeviceActivity, Manifest.permission.ACCESS_COARSE_LOCATION
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
                .addOnFailureListener(this,object: OnFailureListener {
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
                                    rae.startResolutionForResult(this@AddDeviceActivity, REQUEST_CHECK_SETTINGS)
                                } catch (sie: IntentSender.SendIntentException) {
                                    Log.i("", "PendingIntent unable to execute request.")
                                }

                            }
                            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                                val errorMessage =
                                        "Location settings are inadequate, and cannot be " + "fixed here. Fix in Settings."
                                Log.e("", errorMessage)
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
                .addOnCompleteListener(this, OnCompleteListener {
                    mRequestingLocationUpdates = false
                })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==Activity.RESULT_OK){
            if(requestCode==3){
                val district = Gson().fromJson(data!!.getStringExtra("Extra"), District::class.java)
                etDistrict.setText(district.name)
            }
        }
    }


}