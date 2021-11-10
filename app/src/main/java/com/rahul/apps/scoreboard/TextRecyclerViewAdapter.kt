package com.rahul.apps.scoreboard

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.rahul.apps.scoreboard.databinding.ListEditableTextItemBinding


class TextRecyclerViewAdapter: RecyclerView.Adapter<TextRecyclerViewAdapter.ViewHolder>(){
    var data = mutableListOf<String>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, data)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(private val binding: ListEditableTextItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int, data: MutableList<String>){
            binding.itemText.text = data[position]
            binding.editButton.setOnClickListener {
                val editTextView = EditText(binding.root.context)
                editTextView.setText(binding.itemText.text)
                editTextView.setOnClickListener {
                    editTextView.setSelection(0, binding.itemText.text.length)
                }
                val tossDialog = AlertDialog.Builder(it.context)
                    .setTitle(binding.root.context.resources.getString(R.string.toss))
                    .setView(editTextView)
                    .setPositiveButton("OK") { _, _ ->
                        binding.itemText.text = editTextView.text
                        data[position] = editTextView.text.toString()
                    }
                    .setNegativeButton("Cancel", null)
                    .create()
                tossDialog.show()
            }
        }
        companion object{
            fun from(parent: ViewGroup): ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListEditableTextItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}