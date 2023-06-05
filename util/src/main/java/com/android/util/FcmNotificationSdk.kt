package com.android.util

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import kotlin.coroutines.CoroutineContext


class MyFirebaseMessagingService(private val fcmNotificationSdk: FcmNotificationSdk) : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        fcmNotificationSdk.sendFcmToken(token)
    }
}

data class User(
    val gadid: String = "null",
    val country: String = "null",
    val language: String= "null",
    val app_package: String = "null"
)

data class UserTag(
    val tag: String = "null",
    val app_package: String = "null"
)

class FcmNotificationSdk:CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.IO

    private var client = OkHttpClient()
    private var url = "http://aff-demo-push.herokuapp.com"
    private lateinit var user: User

    fun init(user: User) {
        launch {
            this@FcmNotificationSdk.user = user
            registerUser(user)
        }
    }

    private fun registerUser(user: User) {
        val url = "$url/adduser"

        val json = """
        {
            "gadid": "${user.gadid}",
            "country": "${user.country}",
            "language": "${user.language}",
            "app_package": "${user.app_package}"
        }
    """.trimIndent()

        val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                Log.d("TAG", e.message.toString())

            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("TAG", responseBody.toString())
                createToken()
            }
        })
    }

    private fun createToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task->
            if (task.isSuccessful){
                val token = task.result
                MyFirebaseMessagingService(this).onNewToken(token)
            } else {
                Log.d("TAG","error")
            }
        }
    }

    fun sendFcmToken(fcmToken: String) {
        val url = "$url/updatetoken/${user.gadid}"
        val json = """
        {
            "fcmtoken": "$fcmToken",
            "app_package": "${user.app_package}"
        }
    """.trimIndent()

        val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url(url)
            .put(requestBody)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                Log.d("TAG",e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("TAG",responseBody.toString())

            }
        })
    }

    fun sendUserTag(userTag: UserTag) {
        Log.d("TAG",userTag.toString())
        val url = "$url/updatetag/${user.gadid}"
        val json = """
        {
            "tag": "${userTag.tag}",
            "app_package": "${userTag.app_package}"
        }
    """.trimIndent()

        val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url(url)
            .put(requestBody)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                Log.d("TAG",e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("TAG",responseBody.toString())

            }
        })
    }
}