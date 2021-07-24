package com.gb.projectrecyclerview.notetracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gb.projectrecyclerview.R
import com.gb.projectrecyclerview.database.Note
import com.gb.projectrecyclerview.databinding.ListItemNoteBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class NoteAdapter(private val clickListener: NoteListener) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(NoteDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder(ListItemNoteBinding.inflate(inflater, parent, false))
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val noteItem = getItem(position) as DataItem.NoteItem
                holder.bind(noteItem.note, clickListener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.NoteItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    fun addHeaderAndSubmitList(list: List<Note>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.NoteItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    inner class ViewHolder(private val binding: ListItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Note, clickListener: NoteListener) {
            binding.note = item
            binding.clickListener = clickListener
            binding.titleString.setOnClickListener { toggleText() }
            binding.textNoteString.visibility =
                if (currentList[layoutPosition].isVisible) View.VISIBLE else View.GONE
            binding.executePendingBindings()
        }

        private fun toggleText() {
            currentList[layoutPosition].isVisible = !currentList[layoutPosition].isVisible
            notifyItemChanged(layoutPosition)
        }

    }

    class TextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.header, parent, false)
                return TextViewHolder(view)
            }
        }
    }
}


class NoteDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

class NoteListener(val clickListener: (NoteId: Long) -> Unit,
                    val clickListenerDel:(note:Note) -> Unit) {
    fun onClick(note: Note) = clickListener(note.noteId)
    fun onClickDel(note: Note) = clickListenerDel(note)
}

sealed class DataItem {
    data class NoteItem(val note: Note) : DataItem() {
        override val id = note.noteId
    }

    object Header : DataItem() {
        override val id = Long.MIN_VALUE
    }

    var isVisible = false
    abstract val id: Long
}