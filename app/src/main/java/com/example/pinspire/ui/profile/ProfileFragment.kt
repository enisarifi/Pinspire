package com.example.pinspire.ui.profile



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
import com.example.pinspire.api.Helper.provideRetrofit
import com.example.pinspire.databinding.FragmentProfileBinding
import com.example.pinspire.models.Photo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var photoAdapter: PhotoAdapter
    private var photoList = mutableListOf<Photo>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        fetchPhotos()
    }

    private fun fetchPhotos() {
        val apiService = provideRetrofit()

        apiService.getPhotos().enqueue(object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                if (response.isSuccessful && response.body() != null) {
                    val photos = response.body()!!

                    val sharedPreferences = requireContext().getSharedPreferences("photo_likes", Context.MODE_PRIVATE)

                    photos.forEach { photo ->
                        val isLiked = sharedPreferences.getBoolean("isLiked_${photo.id}", false)
                        photo.isLiked = isLiked
                    }

                    val likedPhotos = photos.filter { it.isLiked }

                    photoList.clear()
                    photoList.addAll(likedPhotos)

                    if(likedPhotos.isEmpty()) {
                        binding.noPosts.visibility = View.VISIBLE
                    }
                    else binding.noPosts.visibility = View.GONE

                    photoAdapter = PhotoAdapter(photoList, object : PhotoAdapter.OnDetailsClickListener {
                        override fun onDetailsClick(photo: Photo) {
                            val bundle = Bundle()
                            val isLiked = sharedPreferences.getBoolean("isLiked_${photo.id}", false)
                            bundle.putInt("id", photo.id)
                            bundle.putBoolean("isLiked", isLiked)
                            findNavController().navigate(R.id.action_navigation_profile_to_postDetailsFragment, bundle)

                            val sharedPreferences = requireContext().getSharedPreferences("from_fragment", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("from_fragment", "profile")
                            editor.apply()
                        }
                    })

                    binding.recyclerView.adapter = photoAdapter
                } else {
                    Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
