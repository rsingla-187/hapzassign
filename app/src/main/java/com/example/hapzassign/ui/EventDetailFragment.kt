package com.example.hapzassign.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hapzassign.databinding.FragmentEventDetailBinding
import com.example.hapzassign.eventmanager.viewmodel.EventViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class EventDetailFragment : Fragment() {

    private var _binding: FragmentEventDetailBinding? = null
    private val binding get() = _binding!!

    private val eventViewModel: EventViewModel by viewModels()
    private val args: EventDetailFragmentArgs by navArgs()
    private var selectedDateTime: Long = Calendar.getInstance().timeInMillis

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadEventDetails(args.eventId)

//        binding.btnEditEvent.setOnClickListener {
//            val action = EventListFragmentDirections.actionEventListFragmentToCreateEventFragment(args.eventId)
//            findNavController().navigate(action)
//        }
        binding.btnEditEvent.setOnClickListener {
            val action = EventDetailFragmentDirections.actionEventDetailFragmentToCreateEventFragment(args.eventId)
            findNavController().navigate(action)
        }

    }

    private fun loadEventDetails(eventId: Int) {
        eventViewModel.getEventById(eventId).observe(viewLifecycleOwner) { event ->
            event?.let {
                // Set the text for event details
                binding.eventName.text = it.name
                binding.eventLocation.text = it.location
                binding.eventDescription.text = it.description
                selectedDateTime = it.dateTime
                binding.eventDate.text = formatDateTime(selectedDateTime)
                val participantsString = it.participants.joinToString(separator = ", ")
                binding.eventParticipants.setText(participantsString)

            } ?: run {
                // Event not found case
                Toast.makeText(requireContext(), "Event not found", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }
    }
    private fun formatDateTime(timeInMillis: Long): String {
        val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        return dateFormat.format(Date(timeInMillis))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
