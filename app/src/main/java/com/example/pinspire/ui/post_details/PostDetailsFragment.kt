package com.example.pinspire.ui.post_details

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pinspire.R
import com.example.pinspire.adapters.CommentAdapter
import com.example.pinspire.api.Helper.provideRetrofit
import com.example.pinspire.api.ServiceApi
import com.example.pinspire.databinding.FragmentPostDetailsBinding
import com.example.pinspire.models.Comment
import com.example.pinspire.models.Post
import com.example.pinspire.models.User
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostDetailsFragment : Fragment() {

    private lateinit var binding: FragmentPostDetailsBinding
    private lateinit var commentAdapter: CommentAdapter
    private val commentList = mutableListOf<Comment>()
    private val apiService: ServiceApi = provideRetrofit()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val backBtn = binding.backBtn
        binding.loader.visibility = View.VISIBLE;
        binding.loader1.visibility = View.VISIBLE;

        backBtn.setOnClickListener {
            val sharedPreferences = requireContext().getSharedPreferences("from_fragment", Context.MODE_PRIVATE)
            val fromWhatFragment = sharedPreferences.getString("from_fragment", null)
            Log.e("WHAAAAAAAAAAAAAAAAAAAAAAAAAAAATISIT", "BROOOOOOOOOOOOOOOOOOOOOOOOOO : ${fromWhatFragment} ", )
            Log.e("Error___::", "Response Code: ${fromWhatFragment}}")

            if (fromWhatFragment == "profile") {
                findNavController().navigate(R.id.action_postDetailsFragment_to_navigation_profile)
            } else {
                findNavController().navigate(R.id.action_postDetailsFragment_to_navigation_home)
            }
        }

        commentAdapter = CommentAdapter(commentList)
        binding.recyclerViewComments.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewComments.adapter = commentAdapter


        val postId = arguments?.getInt("id") ?: 0
        var isLiked = arguments?.getBoolean("isLiked") ?: false

        val likeBtn = binding.imgLike
        val likeAmount = binding.likeAmount

        likeAmount.text = ((Math.random() * 100 + 1).toInt()).toString()
        var likeAmountAsInt = Integer.parseInt(likeAmount.text as String)

        if (isLiked) {
            likeBtn.setImageResource(R.drawable.ic_liked)
            likeAmount.text = (++likeAmountAsInt).toString()
        } else {
            likeBtn.setImageResource(R.drawable.ic_like)
        }

        likeBtn.setOnClickListener {
            isLiked = !isLiked
            if (isLiked) {
                likeBtn.setImageResource(R.drawable.ic_liked)
                likeAmount.text = (++likeAmountAsInt).toString()
            } else {
                likeBtn.setImageResource(R.drawable.ic_like)
                likeAmount.text = (--likeAmountAsInt).toString()
            }

            val sharedPreferences = requireContext().getSharedPreferences("photo_likes", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("isLiked_$postId", isLiked)
            editor.apply()
        }


        getPostDetails(postId)
        getCommentsByPostId(postId)

        provideRetrofit().getUserById(postId).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    binding.loader1.visibility = View.GONE;
                    val user = response.body()
                    if (user != null) {
                        binding.postedBy.text = user.name
                    }
                } else {
                    binding.postedBy.text = "Error";
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                binding.loader1.visibility = View.GONE;

                binding.postedBy.text = "Error";
                Log.e("API_ERROR", "Error fetching user: ${t.message}")
            }
        })


    }

    private fun getPostDetails(postId: Int) {
        apiService.getPostById(postId).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (response.isSuccessful && response.body() != null) {
                    val post = response.body()!!
                    binding.loader1.visibility = View.GONE;

                    if (post.title.length > 20) {
                        binding.tvTitle.text = "Title : " + post.title.substring(0, 20).replaceFirstChar { it.toUpperCase() } + "..."
                    } else binding.tvTitle.text = "Title : " + post.title.replaceFirstChar { it.toUpperCase() }

                    binding.tvDescription.text = "Description: \n" + post.body.replaceFirstChar { it.toUpperCase() }

                    val imageUrl = "https://picsum.photos/600/400?random=$postId"
                    Picasso.get()
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_loading_img)
                        .error(R.drawable.ic_error)
                        .into(binding.imgPhoto)

                } else {
                    binding.loader1.visibility = View.GONE;

                    Toast.makeText(requireContext(), "Failed to load post details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                binding.loader1.visibility = View.GONE;

            }
        })
    }

    private fun getCommentsByPostId(postId: Int) {
        apiService.getCommentsByPostId(postId).enqueue(object : Callback<List<Comment>> {
            override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
                if (response.isSuccessful && response.body() != null) {
                    binding.loader.visibility = View.GONE;

                    commentList.clear()
                    commentList.addAll(response.body()!!)
                    commentAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(requireContext(), "Failed to load comments", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                binding.loader.visibility = View.GONE;

                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
