package ru.tyupik.piclab.viewmodel

import androidx.lifecycle.ViewModel
import ru.tyupik.piclab.model.Picture
import ru.tyupik.piclab.repository.PictureRepositoryImpl
import java.lang.Exception

class PictureViewModel : ViewModel() {
    private val pictureRepositoryImpl = PictureRepositoryImpl()
    var data: MutableList<Picture> = mutableListOf()


    fun loadPicture() {
        try {
            data = pictureRepositoryImpl.loadPicture()
        } catch (e: Exception) {

        }
    }
    fun loadPreviousPicture() {
    }
}