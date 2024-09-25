package com.example.tickets.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tickets.R
import com.example.tickets.RoomDB.Ticket
import com.example.tickets.TicketAdapter
import com.example.tickets.databinding.ActivityMainBinding
import com.example.tickets.viewmodels.MainViewModel

class MainActivity : AppCompatActivity(), TicketAdapter.OnItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }

        binding.add.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        })

        viewModel.tickets.observe(this) {tickets ->
            if (!tickets.isNullOrEmpty() ) {
                //Toast.makeText(this, tickets[0].name, Toast.LENGTH_SHORT).show()
                binding.recView.layoutManager = LinearLayoutManager(this)
                binding.recView.adapter = TicketAdapter(tickets, this)
            }
        }

    }

    override fun onItemClick(ticket: Ticket) {
        Toast.makeText(this, "clicked on "+ticket.name , Toast.LENGTH_SHORT).show()
        val intent = Intent(this, ViewActivity::class.java)
        intent.putExtra("ticket", ticket)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetch()
    }
}