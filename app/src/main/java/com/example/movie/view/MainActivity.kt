package com.example.movie.view

import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewpager.widget.PagerAdapter
import com.example.movie.R
import com.example.movie.adapter.SlidePagerAdapter
import com.example.movie.model.Movie
import com.example.movie.model.Singleton
import com.example.movie.myFragments.LikeFragment
import com.example.movie.myFragments.MainFragment
import com.example.movie.myFragments.ProfileFragment
import com.example.movie.pager.LockableViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var pager: LockableViewPager
    private lateinit var pagerAdapter: PagerAdapter
    private var fragmentMain: Fragment = MainFragment()
    private var fragmentLike: Fragment = LikeFragment()
    private var fragmentProfile: Fragment = ProfileFragment()
    private var list: MutableList<Fragment> = ArrayList()
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var movie: Movie
    private var mRegistrationBroadcastReceiver: BroadcastReceiver? = null

    override fun onPause() {
        this.mRegistrationBroadcastReceiver?.let {
            LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(it)
        }
        super.onPause()
    }

    override fun onResume() {
        Log.d("RESUME", "RESUME")

        super.onResume()

        this.mRegistrationBroadcastReceiver?.let {
            LocalBroadcastManager.getInstance(this).registerReceiver(
                it, IntentFilter(
                    "push"
                )
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page)
        FirebaseMessaging.getInstance().subscribeToTopic("weather")
            .addOnCompleteListener { task ->
                var msg = "Subscribed"
                if (!task.isSuccessful) {
                    msg = "Not subscribed"
                }
                Log.d("TAGGG", msg)
            }
        mRegistrationBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == "push") {
                    val message = intent.getStringExtra("message")
                    val title = intent.getStringExtra("title")
                    movie = Singleton.getMovie() as Movie
                    showNotification(title, message, movie)

                }
                if (intent?.hasExtra("movie") as Boolean) {
                    val intentDetail = Intent(applicationContext, DetailActivity::class.java)
                    intentDetail.putExtra("movie", intent.extras?.getSerializable("movie") as Movie)
                    startActivity(intentDetail)
                }
            }
        }
        bindView()
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        list.add(fragmentMain)
        list.add(fragmentLike)
        list.add(fragmentProfile)
        pager.setSwipable(false)
        pagerAdapter = SlidePagerAdapter(supportFragmentManager, list)
        pager.adapter = pagerAdapter

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    pager.setCurrentItem(0, false)
                    supportActionBar?.title = "Кино ТВ - Онлайн Фильмы"
                    val bundle = Bundle()
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, R.id.home.toString())
                    firebaseAnalytics.logEvent("homeFragment", bundle)
                }
                R.id.like_posts -> {
                    pager.setCurrentItem(1, false)
                    supportActionBar?.title = "Закладки"
                    val bundle = Bundle()
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, R.id.like_posts.toString())
                    firebaseAnalytics.logEvent("likeFragment", bundle)
                }
                R.id.about -> {
                    pager.setCurrentItem(2, false)
                    supportActionBar?.title = "Профиль"
                    val bundle = Bundle()
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, R.id.about.toString())
                    firebaseAnalytics.logEvent("profileFragment", bundle)
                }
            }
            false
        }
    }

    private fun bindView() {
        pager = findViewById(R.id.pager)
        bottomNavigationView = findViewById(R.id.bottom_navigation)
    }

    @SuppressLint("WrongConstant")
    private fun showNotification(title: String?, message: String?, movie: Movie) {

        val resultIntent = Intent(this, DetailActivity::class.java)
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        resultIntent.putExtra("movie", movie)

        val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addParentStack(DetailActivity::class.java)
        stackBuilder.addNextIntent(resultIntent)

        val pendingIntent: PendingIntent =
            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "Channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channelId, "FCM", NotificationManager.IMPORTANCE_MAX)
            notificationChannel.description = "title"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.setShowBadge(true)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

            notificationManager.createNotificationChannel(notificationChannel)
        }
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setSound(soundUri)
            .setContentTitle(title)
            .setContentText(message)
            .setCustomContentView(getDesign(title, message))
            .setCustomBigContentView(getExpandedDesign())
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setAutoCancel(true)

        notificationManager.notify(1, notificationBuilder.build())
    }

    private fun getDesign(title: String?, message: String?): RemoteViews {
        val remoteViews = RemoteViews(applicationContext.packageName, R.layout.notification_small)
        remoteViews.setTextViewText(R.id.title, title)
        remoteViews.setTextViewText(R.id.message, message)
        return remoteViews
    }

    fun getExpandedDesign(): RemoteViews {
        val remoteViews =
            RemoteViews(applicationContext.packageName, R.layout.notification_exp)

        val resultIntent = Intent(this, DetailActivity::class.java)
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        resultIntent.putExtra("movie", movie)

        val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addParentStack(DetailActivity::class.java)
        stackBuilder.addNextIntent(resultIntent)

        val pendingIntent: PendingIntent =
            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        remoteViews.setOnClickPendingIntent(R.id.messageExp, pendingIntent)
        return remoteViews
    }
}
