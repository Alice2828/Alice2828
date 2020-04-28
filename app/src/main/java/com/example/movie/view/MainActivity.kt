package com.example.movie.view

import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewpager.widget.PagerAdapter
import com.example.movie.R
import com.example.movie.adapter.SlidePagerAdapter
import com.example.movie.model.Movie
import com.example.movie.myFragments.LikeFragment
import com.example.movie.myFragments.MainFragment
import com.example.movie.myFragments.ProfileFragment
import com.example.movie.pager.LockableViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var pager: LockableViewPager
    private lateinit var pagerAdapter: PagerAdapter
    private var fragmentMain: Fragment = MainFragment()
    private var fragmentLike: Fragment = LikeFragment()
    private var fragmentProfile: Fragment = ProfileFragment()
    private var list: MutableList<Fragment> = ArrayList()
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
                    movie = intent.getSerializableExtra("movie") as Movie
                    showNotification(title, message, movie)

                }
            }
        }
        bindView()
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
                }
                R.id.like_posts -> {
                    pager.setCurrentItem(1, false)
                    supportActionBar?.title = "Закладки"
                }
                R.id.about -> {
                    pager.setCurrentItem(2, false)
                    supportActionBar?.title = "Профиль"
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

//        val pendingIntent =
//            PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_ONE_SHOT)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "Channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channelId, "FCM", NotificationManager.IMPORTANCE_MAX)
            notificationChannel.setDescription("title")
            notificationChannel.enableLights(true)
            notificationChannel.setLightColor(Color.RED)
            notificationChannel.enableVibration(true)

            notificationManager.createNotificationChannel(notificationChannel)
        }
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val longText = "To have a notification appear in an expanded view, " +
                "first create a NotificationCompat.Builder object " +
                "with the normal view options you want. " +
                "Next, call Builder.setStyle() with an " +
                "expanded layout object as its argument.";

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
            .setWhen(System.currentTimeMillis())
            .setAutoCancel(true)
            .setSound(soundUri)
            .setOnlyAlertOnce(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(Notification.PRIORITY_MAX)
            .setStyle(NotificationCompat.BigTextStyle().bigText(longText))
            .setContentIntent(pendingIntent)


        notificationManager.notify(1, notificationBuilder.build())
    }
}
