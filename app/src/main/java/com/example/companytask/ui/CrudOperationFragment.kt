package com.example.companytask.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.companytask.R
import com.example.companytask.databinding.FragmentCrudOperationBinding
import com.example.companytask.repository.NoteRepository
import com.example.companytask.roomDb.AppDatabase
import com.example.companytask.roomDb.NoteEntity
import com.example.companytask.ui.adapters.NoteListAdapter
import com.example.companytask.ui.helper.ItemDetails
import com.example.companytask.viewModels.MainViewModel
import com.example.companytask.viewModels.MainViewModelFactory
class CrudOperationFragment : Fragment() {
    private lateinit var binding: FragmentCrudOperationBinding
    private lateinit var mainViewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View{
        binding = FragmentCrudOperationBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager =LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager

        val dao = AppDatabase.getDatabase(requireActivity()).noteDao()
        val repository = NoteRepository(dao)
        mainViewModel = ViewModelProvider(this, MainViewModelFactory(repository))[MainViewModel::class.java]

        mainViewModel.getRecords().observe(viewLifecycleOwner){
            if(it.isEmpty()){
                binding.errorMessage.visibility= VISIBLE
                binding.recyclerView.visibility = INVISIBLE
            }else{
                binding.errorMessage.visibility= INVISIBLE
                binding.recyclerView.visibility = VISIBLE
                val adapter = NoteListAdapter(it as ArrayList ,object:ItemDetails{
                    override fun getItemDetails(
                        position:Int,
                        editButton: ImageView,
                        deleteButton: ImageView
                    ) {
                        val model = it[position]
                        val data = NoteEntity(model.id,model.noteTitle,model.noteDec)
                        deleteButton.setOnClickListener {mainViewModel.deleteRecord(data)  }
                        editButton.setOnClickListener { openDialog(1,model.id,model.noteTitle,model.noteDec)}
                    }
                })
                binding.recyclerView.adapter = adapter
            }
        }

        binding.floatingActionButton.setOnClickListener{
            openDialog(0,0,"","")
        }
    }
//
    @SuppressLint("SuspiciousIndentation")
    private fun openDialog(callingState:Int, id:Int, title:String, dec:String){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.edit_text_box)
        val width = WindowManager.LayoutParams.MATCH_PARENT
        val height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window!!.setLayout(width, height)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)
        val noteTitle = dialog.findViewById<EditText>(R.id.noteTitle)
        val noteDec = dialog.findViewById<EditText>(R.id.noteDec)
     val clickButton = dialog.findViewById<TextView>(R.id.addButton);
        noteTitle.setText(title)
        noteDec.setText(dec)
    if(callingState==0){
     clickButton.text = "Add data"
    }else if(callingState ==1){
        clickButton.text = "update Data"
    }
    clickButton.setOnClickListener {
            val getNoteTitle = noteTitle.text.toString()
            val getNoteDec = noteDec.text.toString()
            if (getNoteTitle.isEmpty()) {
                noteTitle.error = "Enter title"
            } else if (getNoteDec.isEmpty()) {
                noteDec.error = "Enter description"
            } else {
                val enterData = NoteEntity(id, getNoteTitle, getNoteDec)
                if(callingState==0){
                    mainViewModel.insertRecord(enterData)
                    Toast.makeText(requireContext(), "Data successfully entered", Toast.LENGTH_SHORT).show()
                }else if(callingState ==1)
                {
                  mainViewModel.updateRecord(enterData)
                    Toast.makeText(requireContext(), "Data updated successfully", Toast.LENGTH_SHORT).show()

                }
                dialog.dismiss()

            }
        }
        dialog.show()
    }


}