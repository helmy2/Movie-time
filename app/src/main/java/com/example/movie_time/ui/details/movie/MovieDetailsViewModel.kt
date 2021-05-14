package com.example.movie_time.ui.details.movie

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie_time.data.Repository
import com.example.movie_time.data.Result
import com.example.movie_time.data.movie.Cast
import com.example.movie_time.data.movie.Movie
import com.example.movie_time.data.movie.Poster
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _detailsData = MutableLiveData<Movie>()
    val detailsData: LiveData<Movie>
        get() = _detailsData

    private val _images = MutableLiveData<List<Poster>>()
    val images: LiveData<List<Poster>>
        get() = _images

    private val _recommendationsData = MutableLiveData<List<Result>>()
    val recommendationsData: LiveData<List<Result>>
        get() = _recommendationsData

    private val _castData = MutableLiveData<List<Cast>>()
    val castData: LiveData<List<Cast>>
        get() = _castData

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    fun refresh(id: Int) {
        getMovieDetails(id)
        getMovieCast(id)
        getMovieRecommendations(id)
        getMovieImages(id)
    }

    private fun getMovieDetails(id: Int) = viewModelScope.launch {
        val response = repository.getMovieDetails(id)
        if (response.error == null) {
            _detailsData.value = response.data!!
        } else {
            _error.value = response.error.localizedMessage
            Log.i("TAG", response.error.message.toString())
        }
    }

    private fun getMovieCast(id: Int) = viewModelScope.launch {
        val response = repository.getMovieCast(id)
        if (response.error == null)
            _castData.value = response.data!!
        else {
            _error.value = response.error.localizedMessage
            Log.i("TAG", response.error.message.toString())
        }
    }

    private fun getMovieRecommendations(id: Int) = viewModelScope.launch {
        val response = repository.getMovieRecommendations(id)
        if (response.error == null)
            _recommendationsData.value = response.data?.results
        else {
            _error.value = response.error.localizedMessage
            Log.i("TAG", response.error.message.toString())
        }
    }

    private fun getMovieImages(id: Int) = viewModelScope.launch {
        val response = repository.getMovieImages(id)
        if (response.error == null) {
            Log.i("TAG", "getMovieImages: ${response.data.toString()}")
            if (response.data != null) {
                _images.value = response.data.backdrops
            }
        } else {
            _error.value = response.error.localizedMessage
            Log.i("TAG", response.error.message.toString())
        }
    }
}