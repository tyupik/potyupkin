package ru.tyupik.piclab.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.tyupik.piclab.dto.Picture
import ru.tyupik.piclab.model.PictureState
import ru.tyupik.piclab.repository.PictureRepositoryImpl
import java.lang.Exception

class PictureViewModel : ViewModel() {
    private val pictureRepositoryImpl = PictureRepositoryImpl()
    private var _data: MutableList<Picture> = mutableListOf()
    val pictureLiveData = pictureRepositoryImpl.picturesList
    private val repoState = pictureRepositoryImpl.errorState
    var num: Int = 0


    private val _dataState = MutableLiveData<PictureState>()
    val dataState: LiveData<PictureState>
        get() = _dataState

    fun loadPicture() {
        try {
            _dataState.value = PictureState(loading = true)
            _data = pictureRepositoryImpl.loadPicture()
            _dataState.value = PictureState()
        } catch (e: Exception) {
            _dataState.value = PictureState(error = true)
        }
    }

    fun loadNextPicture() {
        try {
            _dataState.value = PictureState(loading = true, error = false)
            num = pictureRepositoryImpl.loadNextPicture()
            _dataState.value = PictureState(firstPicture = false)
            if (repoState.value == PictureState(error = true, firstPicture = false)) {
                _dataState.value = PictureState(error = true, firstPicture = false)
            }
        } catch (e: Exception) {
            _dataState.value = PictureState(error = true)
        }
    }

    fun loadPreviousPicture() {
        try {
            _dataState.value = PictureState(loading = true)
            num = pictureRepositoryImpl.loadPreviousPicture()
            if (num == 0) {
                _dataState.value = PictureState(firstPicture = true)
            } else {
                _dataState.value = PictureState(firstPicture = false)
            }
        } catch (e: Exception) {
            _dataState.value = PictureState(error = true)
        }
    }
}