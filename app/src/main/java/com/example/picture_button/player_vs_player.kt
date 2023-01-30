package com.example.picture_button

import android.content.Intent
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import java.io.File

class player_vs_player : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_player_vs_player, container, false)
        val button = view.findViewById<Button>(R.id.same_mobile_btn)
        button.setOnClickListener {
            val files: Array<out File>? = context?.cacheDir?.listFiles()
            println(files?.size  )
            if (files?.size != 0){
                files?.get(0)?.delete()
            }
            val bundle = bundleOf("player1" to Board.playerType.HUMAN
                ,"player2" to Board.playerType.HUMAN)
            findNavController().navigate(R.id.action_player_vs_player_to_board,bundle)
        }
        val button2 = view.findViewById<Button>(R.id.choose_player_btn)
        button2.setOnClickListener {
            findNavController().navigate(R.id.action_player_vs_player_to_showPlayer)
        }
        return view

    }
}