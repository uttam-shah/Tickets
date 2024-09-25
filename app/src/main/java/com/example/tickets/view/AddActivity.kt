package com.example.tickets.view

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.tickets.R
import com.example.tickets.databinding.ActivityAddBinding
import com.example.tickets.viewmodels.AddViewModel
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: ActivityAddBinding
    private val viewModel: AddViewModel by viewModels ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.floatingActionButton.setOnClickListener(View.OnClickListener {
            val name = viewModel.name.value?.trim()
            val desc = viewModel.name.value?.trim()

            var isValid = true

            if (name.isNullOrEmpty()) {
                binding.tickerName.error = "Name is required"
                binding.dueDate.requestFocus()  // Move focus to the title field
                isValid = false
            }

            if (desc.isNullOrEmpty()) {
                binding.description.error = "Description is required"
                if (isValid) {
                    binding.dueDate.requestFocus()  // Move focus to the due date field only if title is valid
                }
                isValid = false
            }

            if (isValid) {
                viewModel.saveData()
            }
        })

        // Observe the LiveData for task added
        viewModel.taskAdded.observe(this, Observer { success ->
            if (success.equals(true)) {
                onBackPressed()  // Use this for standard back navigation
            }
        })

        val items = arrayOf("Select Priority","High","Medium","Low")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinner.adapter = adapter

        // Set an item selected listener if needed
        // Set the item selected listener
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position) as String
                if (selectedItem != "Select Priority") {
                    Toast.makeText(
                        this@AddActivity,
                        "Selected: $selectedItem",
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.priority.value = selectedItem
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case where nothing is selected, if needed
            }
        }

        //date picker
        binding.picDueDate.setOnClickListener {
            val now = Calendar.getInstance()
            val dpd = DatePickerDialog.newInstance(
                this,  // 'this' refers to the OnDateSetListener implemented in your activity/fragment
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
            )

            dpd.show(supportFragmentManager, "Datepickerdialog")
        }

//        if(addEditTaskViewModel.dueDate.value != null){
//            binding.DueDateCross.visibility = View.VISIBLE
//        }
        binding.DueDateCross.setOnClickListener {
            viewModel.dueDate.value = null // Clear the dueDate
            binding.DueDateCross.visibility = View.GONE
            binding.layotTime.visibility = View.GONE
        }

        //time picker
        binding.picDueTime.setOnClickListener {
            val now = Calendar.getInstance()
            val timePickerDialog = TimePickerDialog(
                this,  // Context
                { _, hourOfDay, minute ->
                    // Handle the selected time
                    val selectedTime = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, hourOfDay)
                        set(Calendar.MINUTE, minute)
                    }

                    // Format the selected time (e.g., 3:45 PM)
                    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
                    val timeString = timeFormat.format(selectedTime.time)

                    // Display a Toast message with the selected time
                    Toast.makeText(this, "Selected time: $timeString", Toast.LENGTH_SHORT).show()

                    // Update the dueTime in the ViewModel
                    viewModel.dueTime.value = timeString
                    binding.DueTimeCross.visibility = View.VISIBLE
                },
                now.get(Calendar.HOUR_OF_DAY),  // Initial hour
                now.get(Calendar.MINUTE),  // Initial minute
                false  // 12-hour format (AM/PM), change to true for 24-hour format
            )

            timePickerDialog.show()
        }

// Clear the due time when the cross button is clicked
        binding.DueTimeCross.setOnClickListener {
            viewModel.dueTime.value = null // Clear the dueTime
            binding.DueTimeCross.visibility = View.GONE
        }



    }

    // Implement the onDateSet method
    override fun onDateSet(view: DatePickerDialog, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        // Create a Calendar instance for the selected date
        val selectedDate = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, monthOfYear)
            set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }

        // Get today's and tomorrow's dates for comparison
        val today = Calendar.getInstance()
        val tomorrow = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 1)
        }

        // Determine the appropriate date string
        val dateString = when {
            selectedDate.isSameDay(today) -> "Today"
            selectedDate.isSameDay(tomorrow) -> "Tomorrow"
            else -> {
                val dateFormat = SimpleDateFormat("EEE, MMMM d, yyyy", Locale.getDefault())
                dateFormat.format(selectedDate.time)
            }
        }

        // Display a Toast message with the selected date
        Toast.makeText(this, "Selected date: $dateString", Toast.LENGTH_SHORT).show()

        // Update the dueDate in the ViewModel
        viewModel.dueDate.value = dateString
        binding.DueDateCross.visibility = View.VISIBLE
        binding.layotTime.visibility = View.VISIBLE
    }

    // Helper function to check if two Calendar instances represent the same day
    private fun Calendar.isSameDay(other: Calendar): Boolean {
        return this.get(Calendar.YEAR) == other.get(Calendar.YEAR) &&
                this.get(Calendar.DAY_OF_YEAR) == other.get(Calendar.DAY_OF_YEAR)
    }
}