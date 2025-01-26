package com.example.pinspire.api

import com.example.pinspire.models.Comment
import com.example.pinspire.models.Photo
import com.example.pinspire.models.Post
import com.example.pinspire.models.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServiceApi {
    @GET("photos")
    fun getPhotos(): Call<List<Photo>>

    @GET("posts/{id}")
    fun getPostById(@Path("id") id: Int): Call<Post>

    @GET("comments")
    fun getCommentsByPostId(@Query("postId") postId: Int): Call<List<Comment>>

    @GET("users")
    fun getUsers(): Call<List<User>>

    @GET("users/{id}")
    fun getUserById(@Path("id") id: Int): Call<User>

}
