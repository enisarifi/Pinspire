package com.example.pinspire.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pinspire.R

class TopicsAdapter(
    private val topics: List<String>,
    private val onTopicClick: (String) -> Unit
) : RecyclerView.Adapter<TopicsAdapter.TopicViewHolder>() {

    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_topic, parent, false)
        return TopicViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        holder.bind(topics[position], position == selectedPosition)

        holder.itemView.setOnClickListener {
            val previousSelected = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(previousSelected)
            notifyItemChanged(selectedPosition)
            onTopicClick(topics[position])
        }
    }

    override fun getItemCount(): Int {
        return topics.size
    }

    class TopicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val topicName: TextView = itemView.findViewById(R.id.topicName)
        private val underline: View = itemView.findViewById(R.id.underline)

        fun bind(topic: String, isSelected: Boolean) {
            topicName.text = topic
            underline.visibility = if (isSelected) View.VISIBLE else View.GONE
        }
    }
}
