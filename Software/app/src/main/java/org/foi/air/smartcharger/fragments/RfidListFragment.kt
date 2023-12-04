package org.foi.air.smartcharger.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.foi.air.smartcharger.R
import org.foi.air.smartcharger.context.Auth
import org.foi.air.smartcharger.context.RfidCardModel
import org.foi.air.smartcharger.context.RfidCardRecyclerAdapter
import org.foi.air.smartcharger.databinding.FragmentRfidListBinding


/**
 * A simple [Fragment] subclass.
 * Use the [RfidListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RfidListFragment : Fragment() {

    private lateinit var binding: FragmentRfidListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Auth.initialize(this)
    }

    private lateinit var newRecyclerView : RecyclerView
    private lateinit var rfidCardList : ArrayList<RfidCardModel>
    lateinit var rfidName : Array<String>
    lateinit var rfidState : Array<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRfidListBinding.inflate(inflater,container,false)

        rfidName = resources.getStringArray(R.array.rfid_cards_names)
        rfidState = resources.getStringArray(R.array.rfid_cards_states)

        newRecyclerView = binding.rwRfidCards
        newRecyclerView.layoutManager = LinearLayoutManager(context)

        rfidCardList = arrayListOf<RfidCardModel>()
        getRfidCardsData()

        return binding.root
    }

    private fun getRfidCardsData() {
        for(i in rfidName.indices){
            val rfidCard = RfidCardModel(rfidName[i],rfidState[i].toBoolean())
            rfidCardList.add(rfidCard)
        }
        newRecyclerView.adapter = RfidCardRecyclerAdapter(rfidCardList)
    }


}