package com.example.movie.view

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.RingtoneManager
import android.os.Bundle
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

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var pager: LockableViewPager
    private lateinit var pagerAdapter: PagerAdapter
    private var fragmentMain: Fragment = MainFragment()
    private var fragmentLike: Fragment = LikeFragment()
    private var fragmentProfile: Fragment = ProfileFragment()
    private var list: MutableList<Fragment> = ArrayList()
    private var movie: Movie? = Singleton.getMovie()

    private var mRegistrationBroadcastReceiver: BroadcastReceiver? = null
    override fun onPause() {
        this.mRegistrationBroadcastReceiver?.let {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(
                it
            )
        }

        super.onPause()
    }

    override fun onResume() {
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

        mRegistrationBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == "push") {
                    val message = intent.getStringExtra("message")
                    val title = intent.getStringExtra("title")
                    showNotification(title, message)
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

    private fun showNotification(title: String?, message: String?) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("movie_id", movie?.id)
        intent.putExtra("original_title", movie?.original_title)
        intent.putExtra("movie", movie)
        intent.putExtra("poster_path", movie?.getPosterPath())
        intent.putExtra("overview", movie?.overview)
        intent.putExtra("vote_average", (movie?.vote_average).toString())
        intent.putExtra("release_date", movie?.release_date)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setWhen(System.currentTimeMillis())
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }
}
