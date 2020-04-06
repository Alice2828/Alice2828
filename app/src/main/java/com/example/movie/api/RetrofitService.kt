package com.example.movie.api

import com.example.movie.model.MovieResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

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
    fun login(@Query("api_key") apiKey: String, @Body body: JsonObject): Call<JsonObject>


    @POST("authentication/session/new")
    fun getSession(@Query("api_key") apiKey: String, @Body body: JsonObject): Call<JsonObject>

    @GET("account")
    fun getAccount(@Query("api_key") apiKey: String, @Query("session_id") sessionId: String): Call<JsonObject>

    @POST("account/{account_id}/favorite")
    fun rate(
        @Path("account_id") accountId: Int?,
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String?,
        @Body body: JsonObject
    ): Call<JsonObject>

    @POST("account/{account_id}/favorite")
    fun unrate(
        @Path("account_id") accountId: Int?,
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String?,
        @Body body: JsonObject
    ): Call<JsonObject>

    @GET("account/{account_id}/favorite/movies")
    fun getFavoriteMovies(
        @Path("account_id") accountId: Int?,
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String?
    ): Call<MovieResponse>

    @GET("movie/{movie_id}/account_states")
    fun hasLike(
        @Path("movie_id") movieId: Int?,
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String?
    ): Call<JsonObject>

    @DELETE("authentication/session")
    fun deleteSession(@Query("api_key") apiKey: String, @Body body: JsonObject): Call<JsonObject>

}