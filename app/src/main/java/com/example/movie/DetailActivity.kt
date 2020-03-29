package com.example.movie

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
//import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout

class DetailActivity:AppCompatActivity(){
    lateinit var nameofMovie: TextView
    lateinit var plotSynopsis: TextView
    lateinit var userRating: TextView
    lateinit var releaseDate: TextView
    lateinit var imageView: ImageView
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initCollapsingToolbar()

        imageView = findViewById(R.id.thumbnail_image_header)
        nameofMovie = findViewById(R.id.title)
        plotSynopsis= findViewById(R.id.plotsynopsis)
        userRating=findViewById(R.id.userrating)
        releaseDate=findViewById(R.id.releasedate)

        val intent=getIntent()
        if(intent.hasExtra("original_title"))
        {
            val thumbnail="https://image.tmdb.org/t/p/w500" +getIntent().getExtras()?.getString("poster_path")
            val movieName=getIntent().getExtras()?.getString("original_title")
            val synopsis= getIntent().getExtras()?.getString("overview")
            val rating=getIntent().getExtras()?.getString("vote_average")
            val sateOfRelease=getIntent().getExtras()?.getString("release_date")

            Glide.with(this)
                .load(thumbnail)
                .into(imageView)

            nameofMovie.text=movieName
            plotSynopsis.text=synopsis
            userRating.text=rating
            releaseDate.text=sateOfRelease
        }

        else
        {
            Toast.makeText(this,"No API Data", Toast.LENGTH_SHORT).show()
        }

    }

     fun initCollapsingToolbar()
    {
        val collapse: CollapsingToolbarLayout
        collapse=findViewById(R.id.collapsing_toolbar)
        collapse.title=" "
        val appBarLayout: AppBarLayout =findViewById(R.id.appbar)
        appBarLayout.setExpanded(true)

        appBarLayout.addOnOffsetChangedListener(object: AppBarLayout.OnOffsetChangedListener {
            var isShow=false
            var scrollRange=-1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset:Int)
            {
                if (scrollRange==-1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange+verticalOffset==0)
                {
                    collapse.setTitle(getString(R.string.movie_details))
                    isShow=true
                }
                else if(isShow)
                {
                    collapse.setTitle(" ")
                    isShow=false
                }

            }
        })

    }
}