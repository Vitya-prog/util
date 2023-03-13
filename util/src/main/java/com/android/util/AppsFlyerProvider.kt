package com.android.util

import android.content.Context
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

object AppsFlyerProvider {
    private const val APPS_FLYER_KEY = "uSaygNhnRvXzFpwvgjCMG3"

    fun provideAppsFlyer(context: Context) = callbackFlow {
        AppsFlyerLib.getInstance().init(
            APPS_FLYER_KEY,
            object : AppsFlyerConversionListener {
                override fun onConversionDataSuccess(data: MutableMap<String, Any>?) {
                    trySend(data)
                }

                override fun onConversionDataFail(message: String?) {
                    trySend(null)

                }

                override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {

                }

                override fun onAttributionFailure(p0: String?) {

                }
            },
            context
        )
        AppsFlyerLib.getInstance().start(context)
        awaitClose {
            cancel()
        }
    }
}