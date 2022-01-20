package com.rahul.apps.scoreboard

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.rahul.apps.scoreboard.databinding.ListEditableTextItemBinding
import com.rahul.apps.scoreboard.models.Player


class TextRecyclerViewAdapter: RecyclerView.Adapter<TextRecyclerViewAdapter.ViewHolder>(){
    var players = mutableListOf<Player>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("DEBUG", "onCreateViewHolder")
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListEditableTextItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("DEBUG", "onBindViewHolder $position ${players.map { it.name }}")
        if(position < players.size) {
            holder.binding.playerName.setText(players[position].name)
            holder.binding.playerName.doOnTextChanged { text, _, _, _ ->
                players[position].name = text.toString()
            }
            holder.binding.textInputLayout.setEndIconOnClickListener {
                it.isClickable = false
                players.removeAt(position)
                notifyItemRemoved(position)
                it.isClickable = true
            }
        }
    }

    override fun getItemCount(): Int {
        return players.size
    }

    class ViewHolder(val binding: ListEditableTextItemBinding): RecyclerView.ViewHolder(binding.root)
}