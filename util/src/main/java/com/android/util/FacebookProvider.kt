package com.android.util

import android.content.Context
import android.util.Log
import com.facebook.applinks.AppLinkData
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

object FacebookProvider {

    fun provideDeepLink(context: Context) = callbackFlow {
        AppLinkData.fetchDeferredAppLinkData(context) { data ->
            Log.d("DeepLink :", data?.targetUri.toString())
            trySend(data?.targetUri.toString())
        }
        awaitClose {
            cancel()
        }
    }
}