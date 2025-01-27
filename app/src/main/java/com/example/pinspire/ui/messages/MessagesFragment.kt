package com.example.pinspire.ui.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pinspire.databinding.FragmentMessagesBinding

class MessagesFragment : Fragment() {

    private lateinit var binding: FragmentMessagesBinding
    private lateinit var messagesAdapter: MessagesAdapter
    private val messagesList = mutableListOf<String>() // Replace with a data class for real messages

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentMessagesBinding.inflate(layoutInflater)
        setupSendButton()

        return binding.root
    }



    private fun setupSendButton() {
        binding.sendButton.setOnClickListener {
            val message = binding.messageInput.text.toString().trim()
            if (message.isNotEmpty()) {
                messagesList.add(message)
                messagesAdapter.notifyItemInserted(messagesList.size - 1)
                binding.messageInput.text?.clear()
            }
        }
    }
}