package com.example.pinspire.adapters

import android.content.Context
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pinspire.R
import com.example.pinspire.api.Helper.provideRetrofit
import com.example.pinspire.api.ServiceApi
import com.example.pinspire.helpers.CircleTransform
import com.example.pinspire.models.Photo
import com.example.pinspire.models.User
import com.squareup.picasso.Picasso
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhotoAdapter(
    private val photoList: MutableList<Photo>,
    private val onDetailsClick: OnDetailsClickListener
) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    interface OnDetailsClickListener {
        fun onDetailsClick(photo: Photo)
    }

    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.imgPhoto)
        val tvUsername: TextView = itemView.findViewById(R.id.tvUsername)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val imgLike: ImageView = itemView.findViewById(R.id.imgLike)
        val postDetails: Button = itemView.findViewById(R.id.post_details)
        val profilePic: ImageView = itemView.findViewById(R.id.profilePic)
        val loader: ProgressBar = itemView.findViewById(R.id.loader)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = photoList[position]
        var photoId = photo.id

        holder.loader.visibility = View.VISIBLE
        holder.tvUsername.text = "Loading..."

        if (photoId > 10) {
            photoId %= 10
        }

        provideRetrofit().getUserById(photoId).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    holder.loader.visibility = View.GONE
                    val user = response.body()
                    holder.tvUsername.text = user?.name ?: "Unknown User"
                } else {
                    holder.tvUsername.text = "Error"
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                holder.loader.visibility = View.GONE
                holder.tvUsername.text = "Network Problem"
                Log.e("API_ERROR", "Error fetching user: ${t.message}")
            }
        })

        holder.tvDescription.text = photo.title.replaceFirstChar { it.uppercase() }

        val imageUrl = "https://picsum.photos/600/400?random=${photo.id}"
        Picasso.get()
            .load(imageUrl)
            .error(R.drawable.ic_error)
            .into(holder.imgPhoto)


        val thumbnailUrl = photo.thumbnailUrl
        Picasso.get()
            .load(thumbnailUrl)
            .placeholder(R.drawable.ic_loading_img)
            .error(R.drawable.ic_error)
            .into(holder.profilePic)

        if (photo.isLiked) {
            holder.imgLike.setImageResource(R.drawable.ic_liked)
        } else {
            holder.imgLike.setImageResource(R.drawable.ic_like)
        }

        holder.imgLike.setOnClickListener {
            photo.isLiked = !photo.isLiked
            if (photo.isLiked) {
                holder.imgLike.setImageResource(R.drawable.ic_liked)
                Toast.makeText(holder.itemView.context, "Liked!", Toast.LENGTH_SHORT).show()
            } else {
                holder.imgLike.setImageResource(R.drawable.ic_like)
                Toast.makeText(holder.itemView.context, "Unliked!", Toast.LENGTH_SHORT).show()
            }

            val sharedPreferences =
                holder.itemView.context.getSharedPreferences("photo_likes", Context.MODE_PRIVATE)
            sharedPreferences.edit()
                .putBoolean("isLiked_${photo.id}", photo.isLiked)
                .apply()

            notifyItemChanged(position)

        }

            holder.postDetails.setOnClickListener {
            onDetailsClick.onDetailsClick(photo)
        }
    }

    override fun getItemCount(): Int = photoList.size
}
