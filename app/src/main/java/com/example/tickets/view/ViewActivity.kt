package com.example.tickets.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.example.tickets.R
import com.example.tickets.RoomDB.Ticket
import com.example.tickets.databinding.ActivityMainBinding
import com.example.tickets.databinding.ActivityViewBinding
import com.example.tickets.viewmodels.ViewViewModel

class ViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewBinding

    private val viewModel: ViewViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view)

        binding = ActivityViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val ticket = intent.getSerializableExtra("ticket")as? Ticket

        binding.name.text = ticket?.name.toString()
        binding.text2.text = ticket?.description
        binding.priority.text = "(" + ticket?.priority+ ")"
        binding.dueDate.text = ticket?.dueDate
        binding.dueTime.text = ticket?.dueTime

        binding.btnDelete.setOnClickListener{
            Toast.makeText(this, "delect button clicked", Toast.LENGTH_SHORT).show()
            if (ticket != null) {
               // viewModel.deleteTicket(ticket.id)
                displayDialog(ticket.id)

            }
        }

        viewModel.isdeleted.observe(this, Observer { status ->
            if(status == true){
                onBackPressed()
                viewModel.isdeleted.value = false
            }
        })

        binding.btnEdit.setOnClickListener{
            //Toast.makeText(this, "edit button clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, EditActivity::class.java)
            intent.putExtra("ticket", ticket)
            startActivity(intent)

        }
    }

    fun displayDialog(tid: Int){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmation")
        builder.setMessage("Are you sure you want to Delete this Ticket")

// Set the Yes button and its action
        builder.setPositiveButton("Yes") { dialog, _ ->
            // Handle Yes button action
            // You can add logic here for what happens when Yes is clicked
            viewModel.deleteTicket(tid)
            onStart()
        }

// Set the No button and its action
        builder.setNegativeButton("No") { dialog, _ ->
            // Handle No button action
            dialog.dismiss()
        }

// Create and show the dialog
        val dialog = builder.create()
        dialog.show()
    }
}