package com.example.tickets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.tickets.RoomDB.Ticket

class TicketAdapter(
    private val tickets: List<Ticket>,
    private val onItemClickListener: OnItemClickListener
): RecyclerView.Adapter<TicketAdapter.TickerViewHolder>(){

    interface OnItemClickListener {
        fun onItemClick(ticket: Ticket)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TickerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ticket_single_row_design, parent, false)
        return TickerViewHolder(view)
    }

    override fun onBindViewHolder(holder: TickerViewHolder, position: Int) {
        val ticket = tickets[position]

        holder.name.text = ticket.name
        holder.priority.text = ticket.priority
        holder.duedate.text = ticket.dueDate
        holder.duetime.text = ticket.dueTime

        holder.itemView.setOnClickListener{
            onItemClickListener.onItemClick(ticket)
        }
    }

    override fun getItemCount(): Int {
        return tickets.size
    }

    class TickerViewHolder(itemView: View) : ViewHolder(itemView){
          var name: TextView = itemView.findViewById(R.id.ticker_name)
          var priority: TextView = itemView.findViewById(R.id.priority)
          var duedate: TextView = itemView.findViewById(R.id.dueDate)
          var duetime: TextView = itemView.findViewById(R.id.dueTime)

    }
}