package com.gb.projectrecyclerview.notetracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gb.projectrecyclerview.R
import com.gb.projectrecyclerview.database.NoteDatabase
import com.gb.projectrecyclerview.databinding.FragmentNoteTrackerBinding

class NoteTrackerFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: FragmentNoteTrackerBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_note_tracker, container, false
        )

        val application = requireNotNull(this.activity).application
        val dataSource = NoteDatabase.getInstance(application).NoteDatabaseDao
        val viewModelFactory = NoteTrackerViewModelFactory(dataSource, application)
        val noteTrackerViewModel =
            ViewModelProvider(this, viewModelFactory).get(NoteTrackerViewModel::class.java)
        binding.lifecycleOwner = this
        binding.noteTrackerViewModel = noteTrackerViewModel

        val adapter = NoteAdapter(NoteListener { noteId ->
            noteTrackerViewModel.onNoteClicked(noteId)

        })

        binding.noteList.adapter = adapter
        val manager = LinearLayoutManager(context)
        binding.noteList.layoutManager = manager

        noteTrackerViewModel.notes.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })
        return binding.root
    }
}