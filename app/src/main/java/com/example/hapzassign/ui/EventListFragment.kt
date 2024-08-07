package com.example.hapzassign.ui

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hapzassign.R
import com.example.hapzassign.databinding.FragmentEventListBinding
import com.example.hapzassign.eventmanager.viewmodel.EventViewModel
import kotlinx.coroutines.launch

class EventListFragment : Fragment() {

    private var _binding: FragmentEventListBinding? = null
    private val binding get() = _binding!!

    private val eventViewModel: EventViewModel by viewModels()
    val adapter = EventListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        binding.recyclerView.adapter = adapter

//        eventViewModel.allEvents.observe(viewLifecycleOwner) { events ->
//            events?.let { adapter.submitList(it)
//            adapter.notifyDataSetChanged()}
//        }
        eventViewModel.allEvents.observe(viewLifecycleOwner) { events ->
            if (events != null) {
                Log.d("EventListFragment", "Events fetched: ${events.size}")
                adapter.submitList(events)
                adapter.notifyDataSetChanged()  // This shouldn't be necessary with ListAdapter
            } else {
                Log.d("EventListFragment", "No events found")
            }
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // No action needed for query submission
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Filter events based on the search query
                filterEvents(newText)
                return true
            }
        })
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_eventListFragment_to_createEventFragment)
        }

        adapter.setOnItemClickListener { event ->
            val action = EventListFragmentDirections
                .actionEventListFragmentToEventDetailFragment(event.id)
            findNavController().navigate(action)
        }
    }
    private fun filterEvents(query: String?) {
        val trimmedQuery = query?.trim()
        try {
            if (TextUtils.isEmpty(trimmedQuery)) {
                // If query is empty, show all events
                eventViewModel.allEvents.observe(viewLifecycleOwner, Observer { events ->
                    adapter.submitList(events)
                })
            } else {
                // Filter the events based on the search query
                lifecycleScope.launch {
                    eventViewModel.allEvents.observe(viewLifecycleOwner, Observer { events ->
                        val filteredEvents =
                            events.filter { it.name.contains(trimmedQuery!!, ignoreCase = true) }
                        adapter.submitList(filteredEvents)
                    })
                }
            }
        }catch (e:Exception){
            Log.d("Filter-Events",e.toString())
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
