package com.example.hapzassign.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.hapzassign.databinding.FragmentCreateEventBinding
import com.example.hapzassign.eventmanager.model.Event
import com.example.hapzassign.eventmanager.viewmodel.EventViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CreateEventFragment : Fragment() {

    private var _binding: FragmentCreateEventBinding? = null
    private val binding get() = _binding!!

    private val eventViewModel: EventViewModel by viewModels()

    private var eventId: Int? = null
    private var isEditMode = false
    private var selectedDateTime: Long = Calendar.getInstance().timeInMillis

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments?.let { CreateEventFragmentArgs.fromBundle(it) }
        eventId = args?.eventId
        isEditMode = (eventId != null && eventId !=0)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = if (isEditMode) "Edit Event" else "Create Event"

        if (isEditMode) {
            loadEventDetails(eventId!!)
        }

        binding.btnSaveEvent.setOnClickListener {
            saveEvent()
        }
        binding.btnPickDateTime.setOnClickListener {
            showDateTimePicker()
        }
    }

    private fun loadEventDetails(eventId: Int) {
        eventViewModel.getEventById(eventId).observe(viewLifecycleOwner) { event ->
            event?.let {
                binding.eventNameInput.setText(it.name)
                binding.eventLocationInput.setText(it.location)
                binding.eventDescriptionInput.setText(it.description)
                // Handle date, time, and participants population here
                selectedDateTime = it.dateTime
                binding.eventDateTimeInput.text = formatDateTime(selectedDateTime)
                val participantsString = it.participants.joinToString(separator = ", ")
                binding.participantsInput.setText(participantsString)
            }
        }
    }

    private fun saveEvent() {
        val name = binding.eventNameInput.text.toString().trim()
        val location = binding.eventLocationInput.text.toString().trim()
        val description = binding.eventDescriptionInput.text.toString().trim()
        val participants = binding.participantsInput.text.toString().split(",").map { it.trim() }

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(context, "Event name cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        val event = Event(
            id = eventId ?: 0,
            name = name,
            dateTime = selectedDateTime,
            location = location,
            description = description,
            participants = participants
        )

        if (isEditMode) {
            viewLifecycleOwner.lifecycleScope.launch {
                eventViewModel.update(event)
                Toast.makeText(context, "Event updated successfully", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        } else {
            viewLifecycleOwner.lifecycleScope.launch {
                eventViewModel.insert(event)
                Toast.makeText(context, "Event added successfully", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }
    }
    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = selectedDateTime
        }

        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                TimePickerDialog(
                    requireContext(),
                    { _, hourOfDay, minute ->
                        calendar.set(year, month, dayOfMonth, hourOfDay, minute)
                        selectedDateTime = calendar.timeInMillis
                        binding.eventDateTimeInput.text = formatDateTime(selectedDateTime)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                ).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
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
