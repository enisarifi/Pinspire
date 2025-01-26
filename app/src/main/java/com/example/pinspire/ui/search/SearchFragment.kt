package com.example.pinspire.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pinspire.R
import com.example.pinspire.adapters.CardAdapter
import com.example.pinspire.databinding.FragmentSearchBinding
import com.example.pinspire.models.CardItem

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        // Set up RecyclerView for "Ideas for You"
        binding.ideasForYouRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.ideasForYouRecycler.adapter = CardAdapter(getIdeasForYou()) // here it says :
        /*
        Type mismatch.
            Required:
            Context
            Found:
            List<CardItem>
            No value passed for parameter 'cardList'
         */

        // Set up RecyclerView for "Popular on Pinterest"
        binding.popularOnPinterestRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.popularOnPinterestRecycler.adapter = CardAdapter(getPopularItems())

        return binding.root
    }

    private fun getIdeasForYou(): List<CardItem> {
        return listOf(
            CardItem("Philosophy quotes deep", R.drawable.philosophy_image),
            CardItem("Environment concept", R.drawable.environment_image)
        )
    }

    private fun getPopularItems(): List<CardItem> {
        return listOf(
            CardItem("Manga art", R.drawable.manga_image),
            CardItem("Cartoon profile pics", R.drawable.cartoon_image),
            CardItem("Character design", R.drawable.character_design_image),
            CardItem("Funny pfp", R.drawable.funny_image),
            CardItem("Mood humor", R.drawable.mood_humor_image),
            CardItem("Reaction face", R.drawable.reaction_face_image)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
