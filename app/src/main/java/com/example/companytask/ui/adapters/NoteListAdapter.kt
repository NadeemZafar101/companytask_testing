package com.example.companytask.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.companytask.databinding.ItemNoteBinding
import com.example.companytask.roomDb.NoteEntity
import com.example.companytask.ui.helper.ItemDetails

class NoteListAdapter(private val arrayList: ArrayList<NoteEntity>
    , private val itemDetails: ItemDetails
) :RecyclerView.Adapter<NoteListAdapter.ViewHolder>() {
   private var context:Context? = null
    inner class ViewHolder(val binding:ItemNoteBinding):RecyclerView.ViewHolder(binding.root)
    override fun getItemCount(): Int = arrayList.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListAdapter.ViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        context = parent.context
        return ViewHolder(binding)
    }
    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: NoteListAdapter.ViewHolder, position: Int) {
        val model = arrayList[position]
        holder.binding.noteTitle.text = model.noteTitle
        holder.binding.noteDec.text = model.noteDec
        itemDetails.getItemDetails(position,holder.binding.editButton,holder.binding.deleteButton)

    }


}