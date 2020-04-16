package com.example.movie.myFragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.movie.BuildConfig
import com.example.movie.R
import com.example.movie.adapter.LikeMoviesAdapter
import com.example.movie.api.RetrofitService
import com.example.movie.database.MovieDatabase
import com.example.movie.database.MovieDao
import com.example.movie.model.Movie
import com.example.movie.model.Singleton
import com.example.movie.view_model.MovieListViewModel
import com.example.movie.view_model.ViewModelProviderFactory
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LikeFragment : Fragment() {
    private lateinit var relativeLayout: RelativeLayout
    private lateinit var commentsIc: ImageView
    private lateinit var timeIc: ImageView
    private lateinit var recyclerView: RecyclerView
    private var dateTv: TextView? = null
    private var commentsTv: TextView? = null
    private var bigPictv: TextView? = null
    private var bigPicCardIm: ImageView? = null
    private var postAdapter: LikeMoviesAdapter? = null
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var movieList: List<Movie>
    lateinit var movie: Movie
    private lateinit var movieListViewModel: MovieListViewModel
    private var rootView: View? = null
    private var sessionId = Singleton.getSession()
    private var accountId = Singleton.getAccountId()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.activity_main, container, false) as ViewGroup
        bindView()
        val viewModelProviderFactory = ViewModelProviderFactory(context = this.activity as Context)
        movieListViewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(MovieListViewModel::class.java)
        relativeLayout.visibility = View.INVISIBLE
        relativeLayout.visibility = View.GONE
        swipeRefreshLayout.setOnRefreshListener {
            initViews()
        }
        initViews()
        swipeRefreshLayout.isRefreshing = true
        movieListViewModel.liveDataLike.observe(this, Observer { result ->
            postAdapter?.moviesList = result
            postAdapter?.notifyDataSetChanged()
            swipeRefreshLayout.isRefreshing = false

        })
        return rootView
    }

    private fun initViews() {
        bigPicCardIm?.visibility = View.INVISIBLE
        movieList = ArrayList()
        postAdapter = activity?.applicationContext?.let { LikeMoviesAdapter(it, movieList) }!!
        recyclerView.layoutManager = GridLayoutManager(activity, 1)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = postAdapter
        postAdapter?.notifyDataSetChanged()
        loadJSON()
    }

    private fun loadJSON() {
        movieListViewModel.getMovieLike()
    }

    private fun bindView() {
        commentsIc = (rootView as ViewGroup).findViewById(R.id.ic_comments)
        timeIc = (rootView as ViewGroup).findViewById(R.id.ic_times)
        dateTv = (rootView as ViewGroup).findViewById(R.id.date_movie_info)
        commentsTv = (rootView as ViewGroup).findViewById(R.id.comment_movie_info)
        bigPicCardIm = (rootView as ViewGroup).findViewById(R.id.main_big_pic)
        bigPictv = (rootView as ViewGroup).findViewById(R.id.main_big_tv)
        recyclerView = (rootView as ViewGroup).findViewById(R.id.recycler_view)
        relativeLayout = (rootView as ViewGroup).findViewById(R.id.main_layout_pic)
        swipeRefreshLayout = (rootView as ViewGroup).findViewById(R.id.main_content)
    }
}






