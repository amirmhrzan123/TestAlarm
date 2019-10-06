package com.si.sipanicalarm.data.api

import kotlinx.coroutines.Deferred
import retrofit2.http.*

/**
 *network data source retrofit
 */
interface ApiServices {


    @POST(EndPoint.API.ACCEPTDENYINVITATION)
    fun acceptDenyInvitation(@Body acceptDenyRequestModel: AcceptDenyRequestModel): Deferred<String>

    @POST(EndPoint.API.SENDFRIENDREQUEST)
    fun sendFriendRequest(@Body sendFriendRequest: SendFriendRequest):Deferred<String>

    @POST(EndPoint.API.SENDALERTMESSAGES)
    fun sendAlertMessages(@Body sendAlertMessages: SendAlertMessages):Deferred<String>

    @POST(EndPoint.API.SENDSAFEALERTMESSAGES)
    fun sendSafeAlertMessages(@Body sendSafeAlertMessages: SendSafeAlertMessages):Deferred<String>

}

class EndPoint {
    object API {
        const val LOGIN = "auth/login"
        const val SENDFRIENDREQUEST = "sendFriendRequest"
        const val SENDALERTMESSAGES = "sendAlertMessages"
        const val ACCEPTDENYINVITATION = "acceptDenyInvitation"
        const val SENDSAFEALERTMESSAGES = "sendSafeAlert"
    }
}