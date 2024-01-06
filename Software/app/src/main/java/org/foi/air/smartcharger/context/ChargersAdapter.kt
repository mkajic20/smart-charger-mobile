package org.foi.air.smartcharger.context

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import org.foi.air.core.data_classes.ChargerInfo
import org.foi.air.smartcharger.R

class ChargersAdapter (private val chargerList: MutableList<ChargerInfo>, private val context : Context?, private val itemClickListener: OnChargerItemClickListener? = null) : RecyclerView.Adapter<ChargersAdapter.MyViewHolder>(){
    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val chargerName: TextView = itemView.findViewById(R.id.chargerName)
        val state: TextView = itemView.findViewById(R.id.chargerState)
        val chargerCard : CardView = itemView.findViewById(R.id.chargerCard)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.charger_element,parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return chargerList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = chargerList[holder.adapterPosition]

        holder.chargerName.text = currentItem.name
        if(currentItem.active) {
            holder.state.text = context!!.getString(R.string.occupied_state)
            holder.chargerCard.setCardBackgroundColor(Color.parseColor("#FF900000"))
        }
        else {
            holder.state.text = context!!.getString(R.string.free_state)
            holder.chargerCard.setCardBackgroundColor(Color.parseColor("#FF009056"))
        }

        holder.itemView.setOnClickListener{
            itemClickListener?.onChargerItemClick(currentItem)
        }

    }


    fun addData(newData: List<ChargerInfo>) {
        chargerList.addAll(newData)
        notifyDataSetChanged()
    }

    interface OnChargerItemClickListener{
        fun onChargerItemClick(chargerInfo: ChargerInfo)
    }

}