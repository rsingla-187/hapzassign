package com.example.hapzassign.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hapzassign.databinding.ListItemEventBinding
import com.example.hapzassign.eventmanager.model.Event
import java.text.SimpleDateFormat
import java.util.*

class EventListAdapter : ListAdapter<Event, EventListAdapter.EventViewHolder>(EventDiffCallback()) {

    private var onItemClickListener: ((Event) -> Unit)? = null

    fun setOnItemClickListener(listener: (Event) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ListItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

//    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
//        val event = getItem(position)
//        holder.bind(event)
//    }
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        Log.d("EventListAdapter", "Binding event: ${event.name}")
        holder.bind(event)
    }
    inner class EventViewHolder(private val binding: ListItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.eventName.text = event.name
            binding.eventLocation.text = event.location
            binding.eventDateTime.text = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(event.dateTime))
            Log.d("EventViewHolder", "Bound event: ${event.name} at position $adapterPosition")

            binding.root.setOnClickListener {
                onItemClickListener?.invoke(event)
            }
        }
    }
}

class EventDiffCallback : DiffUtil.ItemCallback<Event>() {
    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem == newItem
    }
}
