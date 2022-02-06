package ru.tyupik.piclab.model

data class PictureState(
    val loading: Boolean = false,
    val error: Boolean = false,
    val firstPicture: Boolean = true
)
