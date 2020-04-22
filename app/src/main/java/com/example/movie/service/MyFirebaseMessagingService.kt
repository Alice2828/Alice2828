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
class MyFirebaseMessagingService : FirebaseMessagingService(), CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

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
//    private fun showNotification(title: String?, body: String?) {
//        val intent = Intent(this, DetailActivity::class.java)
//        intent.putExtra("movie_id", movie?.id)
//        intent.putExtra("original_title", movie?.original_title)
//        intent.putExtra("movie", movie)
//        intent.putExtra("poster_path", movie?.getPosterPath())
//        intent.putExtra("overview", movie?.overview)
//        intent.putExtra("vote_average", (movie?.vote_average).toString())
//        intent.putExtra("release_date", movie?.release_date)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val pendingIntent = PendingIntent.getActivity(
//            this, 0, intent,
//            PendingIntent.FLAG_ONE_SHOT
//        )
//
//        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val notificationBuilder = NotificationCompat.Builder(this)
//            .setSmallIcon(R.mipmap.ic_launcher)
//            .setWhen(System.currentTimeMillis())
//            .setContentTitle(title)
//            .setContentText(body)
//            .setAutoCancel(true)
//            .setSound(soundUri)
//            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//            .setContentIntent(pendingIntent)
//
//        val notificationManager =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.notify(0, notificationBuilder.build())
//    }
}