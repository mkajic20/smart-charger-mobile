package org.foi.air.smartcharger.fragments

import ResponseListener
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.foi.air.api.models.NewRfidCardBody
import org.foi.air.api.network.ApiService
import org.foi.air.api.request_handlers.CreateCardRequestHandler
import org.foi.air.api.request_handlers.GetRfidCardsForUserRequestHandler
import org.foi.air.core.data_classes.RfidCard
import org.foi.air.core.models.CreateCardResponseBody
import org.foi.air.core.models.ErrorResponseBody
import org.foi.air.core.models.RfidCardResponseBody
import org.foi.air.smartcharger.R
import org.foi.air.smartcharger.context.Auth
import org.foi.air.smartcharger.context.CardBodyModel
import org.foi.air.smartcharger.context.RfidCardRecyclerAdapter
import org.foi.air.smartcharger.databinding.FragmentRfidListBinding
import org.foi.air.smartcharger.dialogs.AddNewRfidCardDialog

class RfidListFragment : Fragment() {

    private lateinit var binding: FragmentRfidListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Auth.initialize(this)
    }

    private lateinit var newRecyclerView : RecyclerView
    private lateinit var rfidCardList : ArrayList<CardBodyModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRfidListBinding.inflate(inflater,container,false)
        ApiService.authToken = Auth.jwt!!

        //get all cards for logged user
        getAllCards()
        binding.btnRetryConnection.setOnClickListener{
            getAllCards()
            Log.i("rfid", "click")
        }

        newRecyclerView = binding.rwRfidCards
        newRecyclerView.layoutManager = LinearLayoutManager(context)

        binding.ibAddCard.setOnClickListener{
            openDialog(R.layout.create_new_card_dialog)
            //openPopup(R.layout.create_new_card_popup)
        }

        rfidCardList = arrayListOf()

        return binding.root
    }

    private fun getAllCards(){
        val getRfidCardsForUserRequestHandler = GetRfidCardsForUserRequestHandler(Auth.userId!!.toInt())
        binding.tvServerError.visibility = View.INVISIBLE
        binding.btnRetryConnection.visibility = View.INVISIBLE
        getRfidCardsForUserRequestHandler.sendRequest(object: ResponseListener<RfidCardResponseBody>{

            override fun onSuccessfulResponse(response: RfidCardResponseBody) {
                getRfidCardsData(response.rfidCardList)
            }

            override fun onErrorResponse(response: ErrorResponseBody) {
                val toast = Toast.makeText(this@RfidListFragment.context, response.message, Toast.LENGTH_LONG)
                toast.show()
            }

            override fun onApiConnectionFailure(t: Throwable) {
                val toast = Toast.makeText(this@RfidListFragment.context, resources.getString(R.string.cant_reach_server), Toast.LENGTH_LONG)
                toast.show()
                binding.tvServerError.visibility = View.VISIBLE
                binding.btnRetryConnection.visibility = View.VISIBLE
            }


        })
    }


    private fun getRfidCardsData(rfidCardList: MutableList<RfidCard>) {
        this.rfidCardList.clear()
        for(i in rfidCardList.indices){
            val rfidCard = CardBodyModel(rfidCardList[i].name,rfidCardList[i].active.toString(), rfidCardList[i].id)
            this.rfidCardList.add(rfidCard)
        }
        newRecyclerView.adapter = RfidCardRecyclerAdapter(this.rfidCardList, context)
    }

    private fun openDialog(dialog: Int){
        val dialogLayout = AddNewRfidCardDialog(requireContext())
        dialogLayout.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogLayout.show()

        if(dialog == R.layout.create_new_card_dialog){
            val btnCreateCard = dialogLayout.findViewById<Button>(R.id.btnDeleteCard)
            val cardName = dialogLayout.findViewById<EditText>(R.id.etCardName)
            val cardValue = dialogLayout.findViewById<EditText>(R.id.etCardValue)
            val errorText = dialogLayout.findViewById<TextView>(R.id.tvError)
            btnCreateCard.setOnClickListener{
                val newCardBody = NewRfidCardBody(
                    cardValue.text.toString(),
                    cardName.text.toString()
                )
                val loginRequestHandler = CreateCardRequestHandler(Auth.userId!!.toInt(), newCardBody)

                loginRequestHandler.sendRequest(object: ResponseListener<CreateCardResponseBody>{
                    override fun onSuccessfulResponse(response: CreateCardResponseBody) {
                        val toast = Toast.makeText(this@RfidListFragment.context, response.message, Toast.LENGTH_LONG)
                        toast.show()
                        getAllCards()
                        dialogLayout.dismiss()
                    }

                    override fun onErrorResponse(response: ErrorResponseBody) {
                        errorText.text = response.message

                    }

                    override fun onApiConnectionFailure(t: Throwable) {
                        errorText.text = resources.getString(R.string.cant_reach_server)
                    }


                })
            }


    }
    }


}

