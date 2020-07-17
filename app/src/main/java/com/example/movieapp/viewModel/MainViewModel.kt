package com.example.movieapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.crawler.TypeList
import com.example.movieapp.crawler.internetCall
import com.example.movieapp.crawler.pojo.Movie

/**
 * Created by Benjamin Vouillon on 08,July,2020
 */

enum class TypeDisplay {
    POPULAR,
    RATED,
    LIKED
}

class MainViewModel : ViewModel() {

    /**
     * Our 3 lists of Movies
     */
    private var currentList: MutableLiveData<List<Movie>> = MutableLiveData<List<Movie>>()

    /**
     * Type of the list displayed
     */
    private var typeDisplay: MutableLiveData<TypeDisplay> = MutableLiveData<TypeDisplay>(TypeDisplay.POPULAR)

    init {
        // Change mean that we need to get another list
        typeDisplay.observeForever {
            changeValue(it)
        }
    }

    /*
    return the list of elements necessary
     */
    fun getListCurrent(): LiveData<List<Movie>> {
        return currentList
    }

    fun getList(typeDisplay: TypeDisplay) {
        this.typeDisplay.value = typeDisplay
    }

    private fun changeValue(typeDisplay: TypeDisplay) {
        currentList.value = when (typeDisplay) {
            TypeDisplay.POPULAR -> {
                internetCall(TypeList.POPULAR)
            }
            TypeDisplay.RATED -> {
                internetCall(TypeList.HIGHEST_RATE)
            }
            TypeDisplay.LIKED -> TODO("Room not implemented, so Likes doesn't exist")
        }
    }

    override fun onCleared() {
        super.onCleared()
        typeDisplay.removeObserver { }
    }
}