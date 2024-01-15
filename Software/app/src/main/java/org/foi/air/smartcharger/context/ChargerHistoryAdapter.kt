package org.foi.air.smartcharger.context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.foi.air.core.data_classes.EventInfo
import org.foi.air.smartcharger.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class ChargerHistoryAdapter (private val eventList: MutableList<EventInfo>) : RecyclerView.Adapter<ChargerHistoryAdapter.MyViewHolder>() {

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val chargerName: TextView = itemView.findViewById(R.id.chargerName)
        val started: TextView = itemView.findViewById(R.id.startTime)
        val ended: TextView = itemView.findViewById(R.id.endTime)
        val consumed: TextView = itemView.findViewById(R.id.consumed)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.charging_history_elemenent,parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = eventList[holder.adapterPosition]

        holder.chargerName.text = currentItem.charger.name
        holder.started.text = formatDate(currentItem.startTime)
        holder.ended.text = formatDate(currentItem.endTime)
        holder.consumed.text = currentItem.volume
    }

    private fun formatDate(date: String): String {
        val possiblePatterns = arrayOf(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd'T'HH:mm:ss'Z'"
        )

        val outputFormat = SimpleDateFormat("HH:mm:ss\ndd/MM/yyyy", Locale.getDefault())

        for (pattern in possiblePatterns) {
            try {
                val inputFormat = SimpleDateFormat(pattern, Locale.getDefault())
                val formatedDate = inputFormat.parse(date)
                return outputFormat.format(formatedDate!!)
            } catch (e: ParseException) {
                // Continue to the next pattern if parsing fails
            }
        }
        return date
    }

    fun addData(newData: List<EventInfo>) {
        eventList.addAll(newData)
        notifyDataSetChanged()
    }

}