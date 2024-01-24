package org.foi.air.smartcharger.fragments

import ResponseListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.foi.air.api.request_handlers.GetChargersRequestHandler
import org.foi.air.core.data_classes.ChargerInfo
import org.foi.air.core.models.ChargersResponseBody
import org.foi.air.core.models.ErrorResponseBody
import org.foi.air.smartcharger.MainActivity
import org.foi.air.smartcharger.R
import org.foi.air.smartcharger.context.Charger
import org.foi.air.smartcharger.context.ChargersAdapter
import org.foi.air.smartcharger.databinding.FragmentChargerSelectionBinding

class ChargerSelectionFragment : Fragment(), ChargersAdapter.OnChargerItemClickListener {
    private lateinit var binding: FragmentChargerSelectionBinding
    private lateinit var chargersAdapter: ChargersAdapter
    private var currentPage = 1
    private var isLoading = false
    private var reachedEnd = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChargerSelectionBinding.inflate(inflater,container,false)
        setupRecyclerView()

        binding.btnRetryConnection.setOnClickListener{
            loadMoreData()
        }

        binding.rvChargers.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

        getChargersPage(currentPage)

        return binding.root
    }

    private fun setupRecyclerView() {
        chargersAdapter = ChargersAdapter(mutableListOf(), context, this)
        binding.rvChargers.layoutManager = LinearLayoutManager(context)
        binding.rvChargers.adapter = chargersAdapter
    }

    private fun loadMoreData() {
        if(!reachedEnd) {
            isLoading = true
            currentPage++
            getChargersPage(currentPage)
            binding.btnRetryConnection.visibility = View.INVISIBLE
            binding.tvFailedConnection.visibility = View.INVISIBLE
        }
    }

    private fun getChargersPage(page: Int) {
        binding.progressBar.visibility = View.VISIBLE
        val getChargersRequestHandler = GetChargersRequestHandler(page)
        getChargersRequestHandler.sendRequest(object: ResponseListener<ChargersResponseBody>{
            override fun onSuccessfulResponse(response: ChargersResponseBody) {
                if(isAdded) {
                    sendDataToRV(response.chargers)
                    binding.progressBar.visibility = View.INVISIBLE
                }
                isLoading = false
            }


            override fun onErrorResponse(response: ErrorResponseBody) {
                if(response.message=="There are no chargers with that parameters."){
                    val toast = Toast.makeText(this@ChargerSelectionFragment.context, "You have reached the end of the list.", Toast.LENGTH_LONG)
                    binding.progressBar.visibility = View.INVISIBLE
                    toast.show()
                    isLoading = false
                    reachedEnd = true
                }

            }

            override fun onApiConnectionFailure(t: Throwable) {
                if(isAdded) {
                    val toast = Toast.makeText(
                        this@ChargerSelectionFragment.context,
                        resources.getString(R.string.cant_reach_server),
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                    binding.progressBar.visibility = View.INVISIBLE
                    isLoading = false
                    binding.btnRetryConnection.visibility = View.VISIBLE
                    binding.tvFailedConnection.visibility = View.VISIBLE
                }
            }

        })
    }

    private fun sendDataToRV(chargerList: MutableList<ChargerInfo>) {
        chargersAdapter.addData(chargerList)
    }

    override fun onChargerItemClick(chargerInfo: ChargerInfo) {
        if(!chargerInfo.active){
            Charger.chargerId = chargerInfo.id.toString()
            Charger.chargerName = chargerInfo.name
            Charger.saveChargerData()
            (requireActivity() as MainActivity).changeFragment("ChargerSimulatorFragment")
        }
    }


}