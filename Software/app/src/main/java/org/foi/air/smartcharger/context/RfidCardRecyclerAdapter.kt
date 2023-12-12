package org.foi.air.smartcharger.context

import ResponseListener
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.foi.air.api.request_handlers.DeleteCardRequestHandler
import org.foi.air.core.models.ErrorResponseBody
import org.foi.air.core.models.ResponseBody
import org.foi.air.smartcharger.R
class RfidCardRecyclerAdapter(private val rfidCardList: ArrayList<CardBodyModel>,private val context: Context?) : RecyclerView.Adapter<RfidCardRecyclerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rfid_card_element,parent, false)
        return MyViewHolder(itemView)
    }

    var lastSelected : Int? = null
    var lastHolder : MyViewHolder? = null


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = rfidCardList[holder.adapterPosition]

        holder.tvRfidCardName.text = currentItem.name
        holder.tvRfidCardState.text = currentItem.active

        //because of view reusability this if else must be here
        if(lastSelected == holder.adapterPosition){
            holder.card.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.black ))
            holder.btnDelete.visibility = View.VISIBLE
        }else{
            holder.card.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.rfidCardColor ))
            holder.btnDelete.visibility = View.INVISIBLE
        }

        holder.card.setOnLongClickListener{
            if(holder.btnDelete.visibility == View.INVISIBLE){
                if(lastSelected == null) {
                    lastSelected = holder.adapterPosition
                    lastHolder = holder
                }
                else{
                    lastHolder?.card?.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.rfidCardColor ))
                    lastHolder?.btnDelete?.visibility = View.INVISIBLE
                    lastSelected = holder.adapterPosition
                    lastHolder = holder
                }
                holder.card.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.black ))
                holder.btnDelete.visibility = View.VISIBLE
            }else{
                holder.card.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.rfidCardColor ))
                holder.btnDelete.visibility = View.INVISIBLE
            }
            return@setOnLongClickListener true
        }
        holder.btnDelete.setOnClickListener{

            val deleteCardRequestHandler = DeleteCardRequestHandler(Auth.userId!!.toInt(), currentItem.id)

            deleteCardRequestHandler.sendRequest(object: ResponseListener<ResponseBody>{
                override fun onSuccessfulResponse(response: ResponseBody) {
                    val toast = Toast.makeText(context, response.message, Toast.LENGTH_SHORT)
                    toast.show()
                    rfidCardList.removeAt(holder.adapterPosition)
                    notifyItemRemoved(holder.adapterPosition)
                    notifyItemRangeChanged(holder.adapterPosition, itemCount)
                    lastSelected = null
                    lastHolder = null

                }

                override fun onErrorResponse(response: ErrorResponseBody) {
                    val toast = Toast.makeText(context, response.message, Toast.LENGTH_SHORT)
                    toast.show()
                }

                override fun onApiConnectionFailure(t: Throwable) {
                    val toast = Toast.makeText(context, R.string.cant_reach_server, Toast.LENGTH_SHORT)
                    toast.show()
                }

            })



        }


    }


    override fun getItemCount(): Int {
        return rfidCardList.size
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val tvRfidCardName : TextView = itemView.findViewById(R.id.tvRfidCardName)
        val tvRfidCardState : TextView = itemView.findViewById(R.id.tvRfidCardState)
        val btnDelete : ImageButton = itemView.findViewById(R.id.btnDelete)
        val card : CardView = itemView.findViewById(R.id.cvCard)

}}