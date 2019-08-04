package com.example.sialarm.data.prefs

import android.content.SharedPreferences

class PrefsManagerImpl(private val pref: SharedPreferences) : PrefsManager {
    override fun setDistrict(district: String) {
        pref.district = district
    }

    override fun getDistrict(): String =  pref.district

    override fun setTole(tole: String) {
        pref.tole = tole
    }

    override fun getTole(): String = pref.tole

    override fun setState(state: String) {
        pref.state = state
    }

    override fun getState(): String = pref.state

    override fun setWardNo(ward: String) {
        pref.wardNumber = ward
    }

    override fun getWardNo(): String = pref.wardNumber

    override fun setEmail(email: String) {
        pref.email = email
    }

    override fun getEmail(): String = pref.email

    override fun setUserImage(image: String) {
        pref.userImage = image
    }

    override fun getUserImage(): String {
        return pref.userImage
    }

    override fun setPreSetupComplete(boolean: Boolean) {
        pref.preSetupComplete = boolean
    }

    override fun getPreSetupComplete(): Boolean = pref.preSetupComplete

    override fun setDeviceId(deviceId: String) {
        pref.deviceId = deviceId
    }

    override fun getDeviceId(): String = pref.deviceId



    override fun isDeviceRegistered(): Boolean {
        return pref.isDeviceRegistered
    }

    override fun setIsDeviceRegistered(boolean: Boolean) {
        pref.isDeviceRegistered = boolean
    }

    override fun clear() {
        pref.edit().clear().commit()
    }
    override fun getPhoneNumber(): String = pref.phoneNumber

    override fun setPhoneNumber(number: String) {
        pref.phoneNumber = number
    }

    override fun setLoginAs(loginAs: Int) {
        pref.loginAs = loginAs
    }

    override fun getLoginAs(): Int = pref.loginAs

    override fun setLoginStatus(status: Boolean) {
        pref.isLogin = status
    }

    override fun isLogin(): Boolean = pref.isLogin

    companion object {
        const val ACCESSTOKEN = "access_token"
        const val REFRESHTOKEN = "refresh_token"
        const val TOLE = "tole"
        const val WARDNUMBER = "wardNumber"
        const val STATE = "state"
        const val EMAIL = "email"
        const val USERID = "userId"
        const val USERNAME = "userName"
        const val LOGINSTATUS = "loginStatus"
        const val LOGIN_AS = "login_as"
        const val PHONENUMBER = "phoneNumber"
        const val COMPLETED_STEPS = "completed_steps"
        const val USERIMAGE = "userImage"
        const val COMPLETED_SUB_STEPS = "completed_sub_steps"
        const val IS_DEVICE_REGISTERED = "Is_device_registered"
        const val ENABLE_SUBSCRIPTION = "enable_subscription"
        const val DEVICE_ID = "device_id"
        const val PRE_SETUP_COMPLETE = "pre_setup_complete"
        const val DISTRICT = "district"
    }

    override fun setUserId(userId: String) {
        pref.userId = userId
    }

    override fun getUserId(): String = pref.userId

    override fun setUserName(userName: String) {
        pref.userName = userName
    }

    override fun getUserName(): String = pref.userName

    override fun setRefreshToken(refreshToken: String) {
        pref.refreshToken = refreshToken
    }

    override fun getRefreshToken(): String = pref.refreshToken

    override fun setAccessToken(token: String) {
        pref.accessToken = token
    }

    override fun getAccessToken(): String = pref.accessToken

    private var SharedPreferences.accessToken
        get() = getString(ACCESSTOKEN, "")
        set(value) {
            putValue(ACCESSTOKEN, value)
        }

    private var SharedPreferences.deviceId
        get()= getString(DEVICE_ID,"")
        set(value){
            putValue(DEVICE_ID,value)
        }

    private var SharedPreferences.enableSubsription
        get() = getBoolean(ENABLE_SUBSCRIPTION,true)
        set(value){
            putValue(ENABLE_SUBSCRIPTION,value)
        }

    private var SharedPreferences.refreshToken
        get() = getString(REFRESHTOKEN, "")
        set(value) {
            putValue(REFRESHTOKEN, value)
        }

    private var SharedPreferences.userId
        get() = getString(USERID, "")
        set(value) {
            putValue(USERID, value)
        }

    private var SharedPreferences.userName
        get() = getString(USERNAME, "")
        set(value) {
            putValue(USERNAME, value)
        }
    private var SharedPreferences.isLogin
        get() = getBoolean(LOGINSTATUS, false)
        set(value) {
            putValue(LOGINSTATUS, value)
        }

    private var SharedPreferences.isDeviceRegistered
        get() = getBoolean(IS_DEVICE_REGISTERED, false)
        set(value) {
            putValue(IS_DEVICE_REGISTERED, value)
        }

    private var SharedPreferences.loginAs
        get() = getInt(LOGIN_AS, 1)
        set(value) {
            putValue(LOGIN_AS, value)
        }

    private var SharedPreferences.userImage
        get() = getString(USERIMAGE,"")
        set(value){
            putValue(USERIMAGE,value)
        }



    private var SharedPreferences.phoneNumber
        get() = getString(PHONENUMBER, "")
        set(value) {
            putValue(PHONENUMBER, value)
        }

    private var SharedPreferences.preSetupComplete
        get() = getBoolean(PRE_SETUP_COMPLETE,false)
        set(value){
            putValue(PRE_SETUP_COMPLETE,value)
        }

    private var SharedPreferences.email
        get() = getString(EMAIL,"")
        set(value){
            putValue(EMAIL,value)
        }

    private var SharedPreferences.tole
        get() = getString(TOLE,"")
        set(value){
            putValue(TOLE,value)
        }

    private var SharedPreferences.wardNumber
        get() = getString(WARDNUMBER,"")
        set(value){
            putValue(WARDNUMBER,value)
        }


    private var SharedPreferences.state
        get() = getString(STATE,"")
        set(value){
            putValue(STATE,value)
        }

    private var SharedPreferences.district
        get() = getString(DISTRICT,"")
        set(value){
            putValue(DISTRICT,value)
        }


    private fun SharedPreferences.putValue(key: String, value: Any?) {
        when (value) {
            is String -> edit().putString(key, value).apply()
            is Boolean -> edit().putBoolean(key, value).apply()
            is Int -> edit().putInt(key, value).apply()
            is Float -> edit().putFloat(key, value).apply()
            else -> UnsupportedOperationException("Not yet implemented.")
        }
    }

}