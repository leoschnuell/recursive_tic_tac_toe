package com.example.picture_button

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import java.io.File


class player_vs_player : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val connManager =
            activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val mWifi = connManager!!.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_player_vs_player, container, false)
        val button = view.findViewById<Button>(R.id.same_mobile_btn)
        button.setOnClickListener {
            val files: Array<out File>? = context?.cacheDir?.listFiles()
            println(files?.size)
            if (files?.size != 0) {
                files?.get(0)?.delete()
            }
            val bundle = bundleOf(
                "player1" to Board.playerType.HUMAN, "player2" to Board.playerType.HUMAN
            )
            findNavController().navigate(R.id.action_player_vs_player_to_board, bundle)
        }
        val button2 = view.findViewById<Button>(R.id.choose_player_btn)
        button2.setOnClickListener {
            if (mWifi?.isConnected() == true) {
                findNavController().navigate(R.id.action_player_vs_player_to_UDPtesting)
            } else {
                Toast.makeText(
                    getActivity(),
                    "Benötigst Internet für die zweite Option",
                    Toast.LENGTH_SHORT
                ).show();
                button2.isEnabled = false
                button2.setBackgroundColor(Color.GRAY);
            }
        }
        if (mWifi?.isConnected() == false) {
            Toast.makeText(
                getActivity(),
                "Benötigst Internet für die zweite Option",
                Toast.LENGTH_SHORT
            ).show();
            button2.isEnabled = false
            button2.setBackgroundColor(Color.GRAY);
        } else {
            button2.isEnabled = true
            button2.setBackgroundColor(Color.CYAN);
        }

        return view

    }
}