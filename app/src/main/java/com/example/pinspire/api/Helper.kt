package com.example.pinspire.api

import android.content.Context
import android.content.SharedPreferences
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Helper {
    //fun add( a : Int, b : Int) : Int

    //public int add(int a, int b) { return a+b}

    fun provideRetrofit() : ServiceApi {
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://jsonplaceholder.typicode.com/").build().create(ServiceApi::class.java)
    }

    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
    }

    fun putIntToSharedPreferences(context: Context, key: String, value: Int) {
        provideSharedPreferences(context).edit().putInt(key, value).apply()
    }

    fun getIntFromSharedPreferences(context: Context, key: String) : Int{
        return provideSharedPreferences(context).getInt(key, 0)
    }

    fun setPostLikeState(context: Context, postId: Int, isLiked: Boolean) {
        val sharedPreferences = context.getSharedPreferences("likes_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("post_$postId", isLiked).apply()
    }

    fun getPostLikeState(context: Context, postId: Int): Boolean {
        val sharedPreferences = context.getSharedPreferences("likes_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("post_$postId", false)
    }
}