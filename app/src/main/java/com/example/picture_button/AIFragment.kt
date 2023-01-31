package com.example.picture_button

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import java.io.File

class AIFragment : Fragment() {
   override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view = inflater.inflate(R.layout.fragment_a_i, container, false)
       val button1 = view.findViewById<Button>(R.id.leo_alg_btn)
       button1.setOnClickListener {
           val files: Array<out File>? = context?.cacheDir?.listFiles()
           println(files?.size  )
           if (files?.size != 0){
               files?.get(0)?.delete()
           }
           val bundle = Bundle()
           bundle.putSerializable("player1", Board.playerType.HUMAN)
           bundle.putSerializable("player2", Board.playerType.KI_LEO)
           findNavController().navigate(R.id.action_AIFragment_to_board,bundle)
       }
       val button2 = view.findViewById<Button>(R.id.liz_alg_btn)
       button2.setOnClickListener {
           val files: Array<out File>? = context?.cacheDir?.listFiles()
           println(files?.size  )
           if (files?.size != 0){
               files?.get(0)?.delete()
           }
           val bundle = Bundle()
           bundle.putSerializable("player1", Board.playerType.HUMAN)
           bundle.putSerializable("player2", Board.playerType.KI_LIZ)
           findNavController().navigate(R.id.action_AIFragment_to_board,bundle)
       }
       val button3 = view.findViewById<Button>(R.id.san_alg_btn)
       button2.setOnClickListener {
           val files: Array<out File>? = context?.cacheDir?.listFiles()
           println(files?.size  )
           if (files?.size != 0){
               files?.get(0)?.delete()
           }
           val bundle = Bundle()
           bundle.putSerializable("player1", Board.playerType.HUMAN)
           bundle.putSerializable("player2", Board.playerType.RANDOM)
           findNavController().navigate(R.id.action_AIFragment_to_board,bundle)
       }

       return view
    }
}