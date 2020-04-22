package com.example.movie.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.RingtoneManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.movie.BuildConfig
import com.example.movie.R
import com.example.movie.api.RetrofitService
import com.example.movie.model.Movie
import com.example.movie.model.Singleton
import com.example.movie.view.DetailActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(s: String) {
        Log.e("NEW_TOKEN", s)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null) {
            handleNotification(
                remoteMessage.notification?.title,
                remoteMessage.notification?.body
            )
        }
    }

    private fun handleNotification(title: String?, body: String?) {
        val pushNotification = Intent("push")
        pushNotification.putExtra("title", title)
        pushNotification.putExtra("message", body)
        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification)
    }
}