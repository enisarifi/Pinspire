package com.example.pinspire.models

data class Photo(
    val albumId: Int,
    val id: Int,
    val title: String,
    val url: String,
    val thumbnailUrl : String,
    var isLiked : Boolean = false
)
