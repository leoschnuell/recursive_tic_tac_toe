package com.example.picture_button

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val button = view.findViewById<Button>(R.id.button)
        button.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_aiList)
        }
        val button2 = view.findViewById<Button>(R.id.home_button_2)
        button2.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_player_vs_player)
        }
        return view

    }

    fun button_click(view: View) {}
}