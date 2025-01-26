package com.example.pinspire.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pinspire.R
import com.example.pinspire.models.CardItem

class CardAdapter(private val cardList: List<CardItem>) :
    RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = cardList[position]
        holder.title.text = card.title
        holder.image.setImageResource(card.imageResId)
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById<ImageView>(R.id.card_image)
        var title: TextView = itemView.findViewById<TextView>(R.id.card_title)
    }
}
