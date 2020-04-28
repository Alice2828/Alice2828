package com.example.movie.service

import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.movie.model.Movie
import com.example.movie.model.Singleton
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private var movie: Movie? = Singleton.getMovie()
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
        pushNotification.putExtra("movie", movie)
        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification)
    }
}
