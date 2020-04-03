package com.example.movie.api


import com.example.movie.model.MovieResponse
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import okhttp3.Response

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit
import javax.security.auth.callback.Callback

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

    @POST("authentication/token/validate_with_login")
    fun login(@Query("api_key")apiKey:String,@Body body: JsonObject): Call<JsonObject>


    @POST("authentication/session/new")
    fun getSession(@Query("api_key")apiKey:String,@Body body: JsonObject) : Call<JsonObject>

    @GET("account")
    fun getAccount(@Query("session_id") sessionId: String):Call<JsonObject>

   @POST("account/{account_id}/favorite")
   fun postFavourite()

//    @DELETE("authentication/session")
//    fun deleteSession(@Query("api_key") apiKey:String, @Query("session_id")session_id:Session):Call<BooleanResponse>
}