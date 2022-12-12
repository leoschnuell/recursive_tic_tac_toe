package com.example.picture_button

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import core_code.Player

class AIFragment : Fragment() {
   override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val many_ai = requireArguments().getInt("many_ai")
       var player_1 = "Human"
       val view = inflater.inflate(R.layout.fragment_a_i, container, false)
       val button_1 = view.findViewById<Button>(R.id.ai_button_1)
       button_1.setOnClickListener {
           if(many_ai == 1) {
               findNavController().navigate(R.id.action_AIFragment_to_board, Bundle().apply {
                   putString("Player_2", "Alg_1")
               })
           }
           else if (player_1 == "Human"){
               player_1 = "Alg_1"
           }
           else{
               findNavController().navigate(R.id.action_AIFragment_to_board, Bundle().apply {
                   putString("Player_2", "Alg_1")
                   putString("Player_1", player_1)
               })
           }
       }
       val button_2 = view.findViewById<Button>(R.id.ai_button_2)
       button_2.setOnClickListener {
           if(many_ai == 1) {
               findNavController().navigate(R.id.action_AIFragment_to_board, Bundle().apply {
                   putString("Player_2", "Alg_2")
               })
           }
           else if (player_1 == "Human"){
               player_1 = "Alg_2"
           }
           else{
               findNavController().navigate(R.id.action_AIFragment_to_board, Bundle().apply {
                   putString("Player_2", "Alg_2")
                   putString("Player_1", player_1)
               })
           }
       }
       val button_3 = view.findViewById<Button>(R.id.ai_button_3)
       button_3.setOnClickListener {
           if(many_ai == 1) {
               findNavController().navigate(R.id.action_AIFragment_to_board, Bundle().apply {
                   putString("Player_2", "Alg_3")
               })
           }
           else if (player_1 == "Human"){
               player_1 = "Alg_3"
           }
           else{
               findNavController().navigate(R.id.action_AIFragment_to_board, Bundle().apply {
                   putString("Player_2", "Alg_3")
                   putString("Player_1", player_1)
               })
           }

    }
       return view
   }
}