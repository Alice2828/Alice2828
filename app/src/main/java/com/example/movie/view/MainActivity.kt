package com.example.movie.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import com.example.movie.R
import com.example.movie.adapter.SlidePagerAdapter
import com.example.movie.myFragments.LikeFragment
import com.example.movie.myFragments.MainFragment
import com.example.movie.myFragments.ProfileFragment
import com.example.movie.pager.LockableViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.analytics.FirebaseAnalytics

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var pager: LockableViewPager
    private lateinit var pagerAdapter: PagerAdapter
    private var fragmentMain: Fragment = MainFragment()
    private var fragmentLike: Fragment = LikeFragment()
    private var fragmentProfile: Fragment = ProfileFragment()
    private var list: MutableList<Fragment> = ArrayList()
    private  lateinit var firebaseAnalytics:FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page)
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
                    bundle.putString("page_name", "Main_page")
                    firebaseAnalytics.logEvent("MainFragment", bundle)
                }
                R.id.like_posts -> {
                    pager.setCurrentItem(1, false)
                    supportActionBar?.title = "Закладки"
                    val bundle = Bundle()
                    bundle.putString("page_name", "Like_page")
                    firebaseAnalytics.logEvent("LikeFragment", bundle)
                }
                R.id.about -> {
                    pager.setCurrentItem(2, false)
                    supportActionBar?.title = "Профиль"
                    val bundle = Bundle()
                    bundle.putString("page_name", "Profile_page")
                    firebaseAnalytics.logEvent("ProfileFragment", bundle)
                }
            }
            false
        }
    }

    private fun bindView() {
        pager = findViewById(R.id.pager)
        bottomNavigationView = findViewById(R.id.bottom_navigation)
    }
}
