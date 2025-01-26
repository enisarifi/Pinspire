package com.example.pinspire.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pinspire.R
import com.example.pinspire.adapters.PhotoAdapter
import com.example.pinspire.adapters.TopicsAdapter
import com.example.pinspire.api.Helper.provideRetrofit
import com.example.pinspire.databinding.FragmentHomeBinding
import com.example.pinspire.models.Photo
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.withTimeout
import kotlinx.coroutines.withTimeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var photoAdapter: PhotoAdapter
    private var photoList = mutableListOf<Photo>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topicsRecyclerView = binding.topicsRecyclerView
        val topics = listOf(
            "All", "Universe", "Retro Future", "Arcane", "Nature", "Studio Ghibli",
            "Ocean", "Clouds", "Art"
        )



        topicsRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val topicsAdapter = TopicsAdapter(topics) { topic ->
            refreshPhotoList()
        }
        topicsRecyclerView.adapter = topicsAdapter

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        fetchPhotos()
        //Pjesa per like -------------

        //-----------------------------

    }

    private fun fetchPhotos() {
        val apiService = provideRetrofit()
        // redundant o qikjo kom provu diqka
        photoList.clear()
        binding.progressCircular.show()

        apiService.getPhotos().enqueue(object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                if (response.isSuccessful && response.body() != null) {
                    val photos = response.body()!!

                    //Me shared preferences me kqyr a ka pas t like a jo posti
                    val sharedPreferences = requireContext().getSharedPreferences("photo_likes", Context.MODE_PRIVATE)

                    photos.forEach { photo ->
                        val isLiked = sharedPreferences.getBoolean("isLiked_${photo.id}", false)
                        photo.isLiked = isLiked
                    }
//                    ------------------------------------------------------------------

                    photoList.addAll(photos)

                    val editor = sharedPreferences.edit()
                    editor.putString("last_fragment", "profile")
                    editor.apply()

                    photoAdapter = PhotoAdapter(photoList, object : PhotoAdapter.OnDetailsClickListener {
                        override fun onDetailsClick(photo: Photo) {
                            val bundle = Bundle()
                            val sharedPreferences = requireContext().getSharedPreferences("photo_likes", Context.MODE_PRIVATE)
                            val isLiked = sharedPreferences.getBoolean("isLiked_${photo.id}", false)
                            bundle.putInt("id", photo.id)
                            bundle.putBoolean("isLiked", isLiked)
                            findNavController().navigate(R.id.action_navigation_home_to_postDetailsFragment, bundle)

                            val sharedPreferences2 = requireContext().getSharedPreferences("from_fragment", Context.MODE_PRIVATE)
                            val editor = sharedPreferences2.edit()
                            editor.putString("from_fragment", "home")
                            editor.apply()
                        }
                    })


                    binding.recyclerView.adapter = photoAdapter

                    binding.progressCircular.hide()

                } else {
                    binding.progressCircular.hide()

                    Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun refreshPhotoList() {
        photoList.clear()
        CoroutineScope(Dispatchers.Main).launch {
            delay(3000L)
        }
        fetchPhotos()
    }

}
