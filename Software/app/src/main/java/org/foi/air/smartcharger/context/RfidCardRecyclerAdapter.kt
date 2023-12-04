package org.foi.air.smartcharger.context

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.foi.air.smartcharger.R

class RfidCardRecyclerAdapter(private val rfidCardList : ArrayList<RfidCardModel>) : RecyclerView.Adapter<RfidCardRecyclerAdapter.MyViewHolder>() {




        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rfid_card_element,parent, false)
        return MyViewHolder(itemView)
    }


    var lastSelected : Int? = null
    var lastHolder : MyViewHolder? = null


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = rfidCardList[position]

        holder.tvRfidCardName.text = currentItem.name
        holder.swRfidCardSwitch.isChecked = currentItem.active


        //because of view reusability this if else must be here
        if(lastSelected == position){
            holder.card.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.black ))
            holder.btnDelete.visibility = View.VISIBLE
        }else{
            holder.card.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.rfidCardColor ))
            holder.btnDelete.visibility = View.INVISIBLE
        }

        holder.swRfidCardSwitch.setOnClickListener{

        }

        holder.card.setOnClickListener{
            if(holder.btnDelete.visibility == View.INVISIBLE){
                if(lastSelected == null) {
                    lastSelected = position
                    lastHolder = holder
                }
                else{
                    lastHolder?.card?.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.rfidCardColor ))
                    lastHolder?.btnDelete?.visibility = View.INVISIBLE
                    lastSelected = position
                    lastHolder = holder
                }
                holder.card.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.black ))
                holder.btnDelete.visibility = View.VISIBLE
            }else{
                holder.card.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.rfidCardColor ))
                holder.btnDelete.visibility = View.INVISIBLE
            }
            //return@setOnLongClickListener true
        }
        holder.btnDelete.setOnClickListener{
            Log.i("rfid", "delete $position")
            rfidCardList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
            lastSelected = null
            lastHolder = null
        }


    }

/*
    var Selected : Int? = null
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = rfidCardList[position]
        holder.tvRfidCardName.text = currentItem.name
        holder.swRfidCardSwitch.isChecked = currentItem.active

        if(Selected == holder.adapterPosition) {
            holder.card.setCardBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.black
                )
            )
        }
        else
            holder.card.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.rfidCardColor))

        holder.card.setOnClickListener{
            Selected = position
            if(Selected == holder.adapterPosition) {
                holder.card.setCardBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.black
                    )
                )
            }
            else
                holder.card.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.rfidCardColor))
    }
    }

    public fun selectItem(position : Int){
        lastSelected = position
    }
*/


    override fun getItemCount(): Int {
        return rfidCardList.size
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val tvRfidCardName : TextView = itemView.findViewById(R.id.tvRfidCardName)
        val swRfidCardSwitch : Switch = itemView.findViewById(R.id.swRfidCardSwitch)
        val btnDelete : ImageButton = itemView.findViewById(R.id.btnDelete)
        val card : CardView = itemView.findViewById(R.id.cvCard)
        //val swRfidCardSwitch : androidx.appcompat.widget.SwitchCompat = itemView.findViewById(R.id.swRfidCardSwitch)

    }

}