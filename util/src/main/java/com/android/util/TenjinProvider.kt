package com.android.util

import android.content.Context
import com.tenjin.android.TenjinSDK
import com.tenjin.android.config.TenjinConsts
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

object TenjinProvider {

    private const val apiKey = "SYX39EJEVNXZTXXNRPPYRGAOBHHKD3WT"


    fun provideTenjin(context: Context) = callbackFlow {
        val instance = TenjinSDK.getInstance(context, apiKey)
        instance.connect()
        instance.getAttributionInfo { data->
            if(data.containsKey(TenjinConsts.ATTR_PARAM_CAMPAIGN_NAME)) {
                trySend(data[TenjinConsts.ATTR_PARAM_CAMPAIGN_NAME].toString())
            }
        }
        awaitClose {
            cancel()
        }
    }


}