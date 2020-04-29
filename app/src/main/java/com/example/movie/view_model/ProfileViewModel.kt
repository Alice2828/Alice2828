package com.example.movie.view_model

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movie.BuildConfig
import com.example.movie.api.RetrofitService
import com.example.movie.model.Singleton
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ProfileViewModel(
    private val context: Context
) : ViewModel(), CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    val liveData = MutableLiveData<State>()

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun deleteProfileSession() {
        launch {
            liveData.value = State.ShowLoading
            val body: JsonObject = JsonObject().apply {
                addProperty("session_id", Singleton.getSession())
            }
            try {
                val response = RetrofitService.getPostApi()
                    .deleteSessionCoroutine(BuildConfig.THE_MOVIE_DB_API_TOKEN, body)
                if (response.isSuccessful) {
                    liveData.value = State.HideLoading
                    liveData.value = State.Result
                } else {
                    liveData.value = State.HideLoading
                    liveData.value = State.BadResult
                }
            } catch (e: Exception) {
                liveData.value = State.HideLoading
                liveData.value = State.BadResult
            }
        }
    }

    sealed class State {
        object ShowLoading : State()
        object HideLoading : State()
        object Result : State()
        object BadResult : State()
    }
}