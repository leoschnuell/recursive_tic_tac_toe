package com.example.picture_button

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class AIFragment : Fragment() {
   override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view = inflater.inflate(R.layout.fragment_a_i, container, false)
       val button = view.findViewById<Button>(R.id.button2)
       button.setOnClickListener {
           val bundle = Bundle()
           bundle.putSerializable("player1", Board.playerType.HUMAN)
           bundle.putSerializable("player2", Board.playerType.KI_LEO)
           findNavController().navigate(R.id.action_AIFragment_to_board,bundle)
       }
       return view
    }
}