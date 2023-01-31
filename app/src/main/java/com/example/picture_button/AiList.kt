package com.example.picture_button

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

// define all buttons
var listOfAis: List<Board.playerType> = listOf(
    Board.playerType.RANDOM,
    Board.playerType.KI_LEO,
    Board.playerType.DAISY,
    Board.playerType.EVELINE,
    Board.playerType.OLOI,
)


//Generates a list of buttons to select an ai
class AiList : Fragment() {
    var uiList: LinearLayout? = null;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ai_liste, container, false)

        uiList = view.findViewById<LinearLayout>(R.id.ai_list)

        for (ai in listOfAis)
        {
            addBtnToUi(ai);
        }

        return view;
    }


    private fun addBtnToUi(ai: Board.playerType) {
        val btn = Button(context)
        btn.layoutParams =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        btn.text = ai.name
        btn.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("player1", Board.playerType.HUMAN)
            bundle.putSerializable("player2", ai)
            findNavController().navigate(R.id.action_aiList_to_board, bundle)

        }
        UDPtesting.stopUDPBroadcasting()
        uiList?.addView(btn)
    }

}