package com.example.movie.myFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.movie.BuildConfig

import com.example.movie.R
import com.example.movie.adapter.MoviesAdapter
import com.example.movie.api.RetrofitService
import com.example.movie.model.Movie
import com.example.movie.model.MovieResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    private var postAdapter: MoviesAdapter? = null
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var movieList: List<Movie>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView=inflater.inflate(R.layout.activity_main, container, false) as ViewGroup

        recyclerView = rootView.findViewById(R.id.recycler_view)
        swipeRefreshLayout = rootView.findViewById(R.id.main_content)
        swipeRefreshLayout.setOnRefreshListener {
            initViews()
        }
        initViews()
        return rootView
    }

        fun initViews() {

            movieList = ArrayList<Movie>()
            postAdapter = activity?.applicationContext?.let { MoviesAdapter(it, movieList) }!!
            recyclerView.layoutManager = GridLayoutManager(activity, 1)
            recyclerView.itemAnimator= DefaultItemAnimator()
            recyclerView.adapter = postAdapter
            postAdapter?.notifyDataSetChanged()

            loadJSON()

        }

        fun loadJSON() {
            try {
                if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                    return;
                }
                RetrofitService.getPostApi().getPopularMovieList(BuildConfig.THE_MOVIE_DB_API_TOKEN)
                    .enqueue(object : Callback<MovieResponse> {
                        override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                            swipeRefreshLayout.isRefreshing = false
                        }

                        override fun onResponse(
                            call: Call<MovieResponse>,
                            response: Response<MovieResponse>
                        ) {
                            //Log.d("My_post_list", response.body().toString())
                            if (response.isSuccessful) {
                                val list = response.body()?.results
                                postAdapter?.moviesList = list
                                postAdapter?.notifyDataSetChanged()
                            }
                            swipeRefreshLayout.isRefreshing = false
                        }
                    })


            } catch (e: Exception) {
                Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT)
            }
        }

}
