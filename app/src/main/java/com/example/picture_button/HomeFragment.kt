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
        val view = inflater.inflate(R.layout.fragment_home, container, false) //Anzeige
        val button = view.findViewById<Button>(R.id.button)//Benennung/Zuteilung des Buttons vom Layout
        button.setOnClickListener {// wenn de Button benutzt wird
            findNavController().navigate(R.id.action_homeFragment_to_aiList) //Durch den NavController von einem Fragment zum anderen
        }
        val button2 = view.findViewById<Button>(R.id.home_button_2)//Benennung/Zuteilung des Buttons vom Layout
        button2.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_player_vs_player)//Durch den NavController von einem Fragment zum anderen
        }
        return view//Ausgabe der Anzeige

    }
}