package org.foi.air.smartcharger.context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.foi.air.smartcharger.R

class RfidCardRecyclerAdapter(private val rfidCardList : ArrayList<RfidCardModel>) : RecyclerView.Adapter<RfidCardRecyclerAdapter.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rfid_card_element,parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = rfidCardList[position]
        holder.tvRfidCardName.text = currentItem.name
        holder.swRfidCardSwitch.isChecked = currentItem.active

    }

    override fun getItemCount(): Int {
        return rfidCardList.size
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val tvRfidCardName : TextView = itemView.findViewById(R.id.tvRfidCardName)
        val swRfidCardSwitch : Switch = itemView.findViewById(R.id.swRfidCardSwitch)
        //val swRfidCardSwitch : androidx.appcompat.widget.SwitchCompat = itemView.findViewById(R.id.swRfidCardSwitch)

    }

}