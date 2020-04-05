package com.example.movie

//import android.widget.Toolbar
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.movie.api.RetrofitService
import com.example.movie.model.Movie
import com.example.movie.model.User
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    lateinit var nameofMovie: TextView
    lateinit var plotSynopsis: TextView
    lateinit var userRating: TextView
    lateinit var releaseDate: TextView
    lateinit var imageView: ImageView
    lateinit var toolbar: Toolbar
    var movie_id:Int?=null
    var account_id: Int? = null
     var session_id: String?=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        session_id =getSharedPreferences("Username", 0).getString("session_id", "")
//        account_id= getSharedPreferences("Username",0).getInt("account_id",0)
//         account_id=
//            PreferenceManager.getDefaultSharedPreferences(this@DetailActivity).getInt("account_id", 0)
//         session_id= PreferenceManager.getDefaultSharedPreferences(this@DetailActivity).getString("session_id", "")
        initCollapsingToolbar()

        imageView = findViewById(R.id.thumbnail_image_header)
        nameofMovie = findViewById(R.id.title)
        plotSynopsis = findViewById(R.id.plotsynopsis)
        userRating = findViewById(R.id.userrating)
        releaseDate = findViewById(R.id.releasedate)

        val intent = getIntent()
        if (intent.hasExtra("original_title")) {
           session_id=User.getSession()
            account_id=User.getAccountId()
            movie_id=getIntent().extras?.getInt("movie_id")
            val thumbnail =
                "https://image.tmdb.org/t/p/w500" + getIntent().getExtras()?.getString("poster_path")
            val movieName = getIntent().getExtras()?.getString("original_title")
            val synopsis = getIntent().getExtras()?.getString("overview")
            val rating = getIntent().getExtras()?.getString("vote_average")
            val sateOfRelease = getIntent().getExtras()?.getString("release_date")

            Glide.with(this)
                .load(thumbnail)
                .into(imageView)

            nameofMovie.text = movieName
            plotSynopsis.text = synopsis
            userRating.text = rating
            releaseDate.text = sateOfRelease

        } else {
            Toast.makeText(this, "No API Data", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        val id = item.getItemId()

        if (id == R.id.favourite) {
    //Toast.makeText(this@DetailActivity, session_id+" "+account_id,Toast.LENGTH_LONG).show()

            val body = JsonObject().apply {
                addProperty("media_type","movie" )
                addProperty("media_id", movie_id)
                addProperty("favorite", true)
            }

            RetrofitService.getPostApi()
                .rate(account_id, BuildConfig.THE_MOVIE_DB_API_TOKEN, session_id, body)
                .enqueue(object : Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                    }

                    override fun onResponse(
                        call: Call<JsonObject>,
                        response: Response<JsonObject>
                    ) {

                        if (response.isSuccessful) {

                            Toast.makeText(
                                this@DetailActivity,
                                "Movie has been added to favourites",
                                Toast.LENGTH_LONG
                            ).show()

                        }

                    }
                })
            return true
        }


        return super.onOptionsItemSelected(item)

    }


    fun initCollapsingToolbar() {
        val collapse: CollapsingToolbarLayout
        collapse = findViewById(R.id.collapsing_toolbar)
        collapse.title = " "
        val appBarLayout: AppBarLayout = findViewById(R.id.appbar)
        appBarLayout.setExpanded(true)

        appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    collapse.setTitle(getString(R.string.movie_details))
                    isShow = true
                } else if (isShow) {
                    collapse.setTitle(" ")
                    isShow = false
                }

            }
        })

    }
}