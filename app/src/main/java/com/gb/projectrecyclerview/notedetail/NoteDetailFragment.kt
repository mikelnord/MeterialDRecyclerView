package com.gb.projectrecyclerview.notedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gb.projectrecyclerview.R
import com.gb.projectrecyclerview.database.NoteDatabase
import com.gb.projectrecyclerview.databinding.FragmentNoteDetailBinding

class NoteDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentNoteDetailBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_note_detail, container, false
        )

        val application = requireNotNull(this.activity).application
        val arguments = NoteDetailFragmentArgs.fromBundle(requireArguments())

        val dataSource = NoteDatabase.getInstance(application).NoteDatabaseDao
        val viewModelFactory = NoteDetailViewModelFactory(arguments.noteKey, dataSource)
        val noteDetailViewModel =
            ViewModelProvider(this, viewModelFactory).get(NoteDetailViewModel::class.java)
        binding.noteDetailViewModel = noteDetailViewModel
        binding.lifecycleOwner = this

        noteDetailViewModel.navigateToNotepTracker.observe(viewLifecycleOwner, {
            if (it == true) {
                this.findNavController().navigate(
                    NoteDetailFragmentDirections.actionNoteDetailFragmentToNoteTrackerFragment()
                )
                noteDetailViewModel.doneNavigating()
            }
        })


        return binding.root
    }


}