package com.example.sialarm.data.api

import kotlinx.coroutines.Deferred
import retrofit2.http.*

/**
 *network data source retrofit
 */
interface ApiServices {






}

class EndPoint {
    object API {
        const val LOGIN = "auth/login"
        const val SENDFRIENDREQUEST = "sendFriendRequest"
        const val SENDALERTMESSAGES = "sendAlertMessages"
        const val ACCEPTDENYINVITATION = "acceptDenyInvitation"
    }
}