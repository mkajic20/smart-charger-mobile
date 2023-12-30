package org.foi.air.smartcharger.fragments

import ResponseListener
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.foi.air.api.request_handlers.GetEventsForUserRequestHandler
import org.foi.air.core.data_classes.EventInfo
import org.foi.air.core.models.ErrorResponseBody
import org.foi.air.core.models.EventsResponseBody
import org.foi.air.smartcharger.context.Auth
import org.foi.air.smartcharger.context.ChargerHistoryAdapter
import org.foi.air.smartcharger.databinding.FragmentChargingHistoryBinding


class ChargingHistoryFragment : Fragment() {

    private lateinit var binding: FragmentChargingHistoryBinding
    private lateinit var rvHistory: RecyclerView
    private var currentPage = 1
    private var isLoading = false
    private lateinit var chargerHistoryAdapter: ChargerHistoryAdapter
    private lateinit var tvConnectionError: TextView
    private lateinit var btnRetryConnection: Button
    private var reachedEnd = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChargingHistoryBinding.inflate(inflater,container,false)
        rvHistory = binding.rvChargingHistory
        rvHistory.layoutManager = LinearLayoutManager(context)
        chargerHistoryAdapter = ChargerHistoryAdapter(mutableListOf())
        rvHistory.adapter = chargerHistoryAdapter
        tvConnectionError = binding.tvFailedConnection
        btnRetryConnection = binding.btnRetryConnection

        btnRetryConnection.setOnClickListener{
            loadMoreData()
        }

        rvHistory.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount
                if (!isLoading && totalItemCount - 1 == lastVisibleItemPosition ) {
                    loadMoreData()
                }
            }
        })

        getEventPage(currentPage)

        return binding.root
    }

    private fun loadMoreData() {
        if(!reachedEnd) {
            isLoading = true
            currentPage++
            getEventPage(currentPage)
            btnRetryConnection.visibility = View.INVISIBLE
            tvConnectionError.visibility = View.INVISIBLE
        }
    }

    private fun getEventPage(page: Int) {
        if(!Auth.isLoggedIn())
            return
        val geteventsForUserRequestHandler = GetEventsForUserRequestHandler(Auth.userId!!.toInt(), page)
        geteventsForUserRequestHandler.sendRequest(object: ResponseListener<EventsResponseBody>{
            override fun onSuccessfulResponse(response: EventsResponseBody) {
                sendDataToRV(response.eventList)
                isLoading = false
            }


            override fun onErrorResponse(response: ErrorResponseBody) {
                if(response.message=="There are no events with that parameters."){
                    val toast = Toast.makeText(this@ChargingHistoryFragment.context, "You have reached the end of the list.", Toast.LENGTH_LONG)
                    toast.show()
                    isLoading = false
                    reachedEnd = true
                }

            }

            override fun onApiConnectionFailure(t: Throwable) {
                Log.i("eventi","Connection error: "+t.message)
                isLoading = false
                btnRetryConnection.visibility = View.VISIBLE
                tvConnectionError.visibility = View.VISIBLE
            }

        })
    }

    private fun sendDataToRV(eventList: MutableList<EventInfo>) {
        chargerHistoryAdapter.addData(eventList)
    }
}