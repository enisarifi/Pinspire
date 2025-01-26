package com.example.pinspire.adapters

import android.content.Context
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
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
        val postDetails: TextView = itemView.findViewById(R.id.post_details)
        val profilePic : ImageView = itemView.findViewById(R.id.profilePic)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = photoList[position]
        var photoId = photo.id;


        // pjesa me marr ni user based on post id t klikume
        // ama mduhet me i ba me rekurzion se api i kish veq 10 usera

        holder.tvUsername.text = "Loading..."
        var response2: Int? = null;
        var responseErrorBody2: ResponseBody? = null;

        if(photoId > 10) {
            photoId = photoId % 10;
        }
        provideRetrofit().getUserById(photoId).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                response2 = response.code()
                responseErrorBody2 = response.errorBody()
                if (response.isSuccessful) {
                    val user = response.body()
                    holder.tvUsername.text = user?.name ?: "Unknown User"
                } else {
                    holder.tvUsername.text = "Error"

                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                holder.tvUsername.text = "Netvërk probllëm"
                Log.e("API_ERROR", "Response Code: ${response2}, Error Body: ${responseErrorBody2?.string()}")

            }
        })



//        holder.tvUsername.text = user?.name.toString();
        holder.tvDescription.text = photo.title.replaceFirstChar { it.uppercase() }

        val imageUrl = "https://picsum.photos/600/400?random=${photo.id}"
        val thumbnailUrl = "${photo.thumbnailUrl}"
        Picasso.get()
            .load(imageUrl)
            .placeholder(R.drawable.ic_loading_img)
            .error(R.drawable.ic_error)
            .into(holder.imgPhoto)

        Picasso.get()
            .load(thumbnailUrl).placeholder(R.drawable.ic_loading_img).error(R.drawable.ic_error)
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

            val sharedPreferences = holder.itemView.context.getSharedPreferences("photo_likes", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("isLiked_${photo.id}", photo.isLiked)
            editor.apply()

            notifyItemChanged(position)
        }

        holder.postDetails.setOnClickListener {
            onDetailsClick.onDetailsClick(photo)
        }
    }

    override fun getItemCount(): Int = photoList.size
}

//sdi qka o kjo androidi ma generoi
private operator fun <T> Call<T>.invoke(callback: Callback<T>) {

}
