package org.foi.air.smartcharger.fragments

import ResponseListener
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
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
            openPopup(R.layout.create_new_card_popup)
        }

        rfidCardList = arrayListOf<CardBodyModel>()

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
        newRecyclerView.adapter = RfidCardRecyclerAdapter(this.rfidCardList, getContext(), this)
    }

    private fun openPopup(popup : Int){
        val mainActivityView = requireActivity().findViewById<View>(R.id.view)
        mainActivityView.visibility = View.VISIBLE
        val inflater = requireActivity().getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(popup, null)

        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow.setOnDismissListener {
            mainActivityView.visibility = View.INVISIBLE
        }

        popupWindow.showAtLocation(requireActivity().findViewById(R.id.mainConstraint), Gravity.CENTER, 0, 0)

        if(popup == R.layout.create_new_card_popup){
            val btnCreateCard = popupView.findViewById<Button>(R.id.btnDeleteCard)
            val CardName = popupView.findViewById<EditText>(R.id.etCardName)
            val CardValue = popupView.findViewById<EditText>(R.id.etCardValue)
            val ErrorText = popupView.findViewById<TextView>(R.id.tvError)
            btnCreateCard.setOnClickListener{
                val newCardBody = NewRfidCardBody(
                    CardValue.text.toString(),
                    CardName.text.toString()
                )
                val loginRequestHandler = CreateCardRequestHandler(Auth.userId!!.toInt(), newCardBody)

                loginRequestHandler.sendRequest(object: ResponseListener<CreateCardResponseBody>{
                    override fun onSuccessfulResponse(response: CreateCardResponseBody) {
                        val toast = Toast.makeText(this@RfidListFragment.context, response.message, Toast.LENGTH_LONG)
                        toast.show()
                        getAllCards()
                        popupWindow.dismiss()
                    }

                    override fun onErrorResponse(response: ErrorResponseBody) {
                        ErrorText.text = response.message

                    }

                    override fun onApiConnectionFailure(t: Throwable) {
                        ErrorText.text = resources.getString(R.string.cant_reach_server)
                    }


                })
            }

        }


    }



}

