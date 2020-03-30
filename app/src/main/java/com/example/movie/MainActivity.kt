package com.example.movie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.PagerAdapter
import com.example.movie.adapter.MoviesAdapter
import com.example.movie.adapter.SlidePagerAdapter
import com.example.movie.api.RetrofitService
import com.example.movie.model.Movie
import com.example.movie.model.MovieResponse
import com.example.movie.myFragments.LikeFragment
import com.example.movie.myFragments.MainFragment
import com.example.movie.myFragments.ProfileFragment
import com.example.movie.pager.LockableViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {


    lateinit var bottomNavigationView: BottomNavigationView
   // var pagefragment: MainFragment = MainFragment()
   private lateinit var pager: LockableViewPager
    private lateinit var pagerAdapter: PagerAdapter
    private var f1: Fragment = MainFragment()
    private var f2: Fragment = LikeFragment()
    private var f3: Fragment = ProfileFragment()
    private var list: MutableList<Fragment> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page)
        list.add(f1)
        list.add(f2)
        list.add(f3)
        pager = findViewById(R.id.pager)
        pager.setSwipable(false)
        pagerAdapter = SlidePagerAdapter(supportFragmentManager, list)
        pager.adapter = pagerAdapter
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    pager.setCurrentItem(0, false)
                }
                R.id.like_posts -> {
                    pager.setCurrentItem(1, false)
                }
                R.id.about -> {
                    pager.setCurrentItem(2, false)
                }
//                    <item
//                android:id="@+id/home"
//                android:title="Лента"
//                android:icon="@drawable/ic_home"/>
//                    <item
//                android:id="@+id/like_posts"
//                android:title="Закладки"
//                android:icon="@drawable/ic_favorite"/>
//                    <item
//                android:id="@+id/catygory"
//                android:title="Категории"
//                android:icon="@drawable/ic_storage"/>
//                    <item
//                android:id="@+id/about"
//                android:title="Профиль"
//                android:icon="@drawable/ic_perm"/>
            }
            false
        }
    }

}
