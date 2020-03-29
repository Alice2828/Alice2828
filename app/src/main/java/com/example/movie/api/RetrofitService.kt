package com.example.movie.api


import com.example.movie.model.MovieResponse
import com.google.gson.Gson

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

object RetrofitService {

    const val BASE_URL = "https://api.themoviedb.org/3/"

    fun getPostApi(): PostApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(PostApi::class.java)
    }
}

interface PostApi {

    @GET("movie/popular")
    fun getPopularMovieList(@Query("api_key") apiKey: String): Call<MovieResponse>


    @GET("authentication/token/new")
    fun getRequestToken(@Query("api_key") apiKey: String): Call<RequestToken>


    @GET("authenticate/")
    fun getResponse(@Query("api_key") apiKey: String)

    @POST ("authentication/session/new")
    fun getNewSession(@Query("api_key") apiKey:String, @Query("request_token") requestToken:RequestToken):Call<Session>

//    @DELETE("authentication/session")
//    fun deleteSession(@Query("api_key") apiKey:String, @Query("session_id")session_id:Session):Call<BooleanResponse>
}