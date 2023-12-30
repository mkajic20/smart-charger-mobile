package org.foi.air.smartcharger.fragments

import ResponseListener
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private lateinit var rvHistory : RecyclerView
    private var currentPage = 1
    private var isLoading = false
    private lateinit var chargerHistoryAdapter: ChargerHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChargingHistoryBinding.inflate(inflater,container,false)
        rvHistory = binding.rvChargingHistory
        rvHistory.layoutManager = LinearLayoutManager(context)
        chargerHistoryAdapter = ChargerHistoryAdapter(mutableListOf())
        rvHistory.adapter = chargerHistoryAdapter

        rvHistory.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val zadnjiElement = "zadnji - " + lastVisibleItemPosition.toString()
                val totalItemCount = layoutManager.itemCount
                val sviElementi = "svi - " + totalItemCount.toString()
                if (!isLoading && totalItemCount - 1 == lastVisibleItemPosition ) {
                    Log.i("eventi",zadnjiElement)
                    Log.i("eventi",sviElementi)
                    // Load more data when the user is close to the end of the list
                    loadMoreData()
                }
            }
        })

        getEventPage(currentPage)

        return binding.root
    }

    private fun loadMoreData() {
        Log.i("eventi", "More data loaded")
        isLoading = true
        currentPage++
        getEventPage(currentPage)
    }

    private fun getEventPage(page: Int) {
        if(!Auth.isLoggedIn())
            return
        val geteventsForUserRequestHandler = GetEventsForUserRequestHandler(Auth.userId!!.toInt(), page)
        geteventsForUserRequestHandler.sendRequest(object: ResponseListener<EventsResponseBody>{
            override fun onSuccessfulResponse(response: EventsResponseBody) {
                for(i in response.eventList.indices)
                    Log.i("eventi", response.eventList[i].eventId)
                sendDataToRV(response.eventList)
                isLoading = false
            }


            override fun onErrorResponse(response: ErrorResponseBody) {
                Log.i("eventi","Error: "+response.error)
            }

            override fun onApiConnectionFailure(t: Throwable) {
                Log.i("eventi","Connection error: "+t.message)
            }

        })
    }

    private fun sendDataToRV(eventList: MutableList<EventInfo>) {
        chargerHistoryAdapter.addData(eventList)
    }
}