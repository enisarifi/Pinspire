package com.example.pinspire.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pinspire.R
import com.example.pinspire.models.Comment

class CommentAdapter(private val comments: List<Comment>) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
        val tvBody: TextView = itemView.findViewById(R.id.tvBody)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]

        if(comment.name.length > 10) {
            holder.tvName.text = comment.name.replaceFirstChar { it.toUpperCase() }.substring(0, 10)
        }
        else holder.tvName.text = comment.name.replaceFirstChar {it.toUpperCase() }
        holder.tvEmail.text = comment.email
        holder.tvBody.text = comment.body.replaceFirstChar { it.toUpperCase() }
    }

    override fun getItemCount(): Int = comments.size

}


