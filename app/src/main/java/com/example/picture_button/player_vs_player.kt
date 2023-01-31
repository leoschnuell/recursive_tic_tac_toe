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
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController


class player_vs_player : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val connManager = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val mWifi = connManager!!.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_player_vs_player, container, false) //Bestimmung der Anzeige
        val button = view.findViewById<Button>(R.id.same_mobile_btn) //Zusteilung ds Buttons des Layouts
        button.setOnClickListener {//Wenn der Button angeklickt wird
            val bundle = bundleOf(
                "player1" to Board.playerType.HUMAN, "player2" to Board.playerType.HUMAN
            )
            findNavController().navigate(R.id.action_player_vs_player_to_board, bundle) //Durch den NavController
        }
        val button2 = view.findViewById<Button>(R.id.choose_player_btn)//Zusteilung ds Buttons des Layouts
        button2.setOnClickListener {//Wenn der Button angeklickt wird
            if (mWifi?.isConnected() == true) {//Testen ob man mit dem WLAN verbunden ist
                findNavController().navigate(R.id.action_player_vs_player_to_UDPtesting) //Wenn true Weiterleitung zum nächsten Fragment
            } else {
                Toast.makeText(
                    getActivity(),
                    "Benötigst Internet für die zweite Option", //Anzeige für den Benutzer
                    Toast.LENGTH_SHORT
                ).show();
                button2.isEnabled = false
                button2.setBackgroundColor(Color.GRAY);
            }
        }
        if (mWifi?.isConnected() == false) {//Testen ob man mit dem WLAN verbunden ist
            Toast.makeText(
                getActivity(),
                "Benötigst Internet für die zweite Option",//Anzeige für den Benutzer
                Toast.LENGTH_SHORT
            ).show();
            button2.isEnabled = false //nicht ermöglichen den Button zu benutzen
            button2.setBackgroundColor(Color.GRAY);//Anzeigen das der Button gerade nicht benutzt werden kann
        } else {
            button2.isEnabled = true
            button2.setBackgroundColor(Color.MAGENTA);//Anzeigen das der Button gerade benutzt werden kann
        }

        return view

    }
}