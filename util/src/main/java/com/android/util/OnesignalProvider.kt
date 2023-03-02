package com.android.util

import android.app.Application
import com.onesignal.OneSignal

object OnesignalProvider {

    private const val ONESIGNAL_APP_ID = "b4e377a2-51fa-4872-8d43-08822b02d546"

    fun initOneSignal(context: Application){
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        OneSignal.initWithContext(context)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
    }
}