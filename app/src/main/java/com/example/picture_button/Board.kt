package com.example.picture_button

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController

class Board : Fragment() , View.OnClickListener  {
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.grid_test_test, container, false)
            //val button = view.findViewById<Button>(R.id.button2)
          /* button.setOnClickListener {
                findNavController().navigate(R.id.action_AIFragment_to_homeFragment)
            }*/

            val root = view.findViewById<GridLayout>(R.id.root)

            for (i in 0 until root.childCount) {
                val kasten: GridLayout =
                    (root.getChildAt(i) as ConstraintLayout).getChildAt(0) as GridLayout

                for (j in 0 until kasten.childCount) {
                    val kästschen = kasten.getChildAt(j)
                    kästschen.setBackgroundColor(Color.BLACK)
                    kästschen.tag = (i+1)*10 +j+1
                    kästschen.setOnClickListener(this)
                    // idTObutton[(i+1)*10 +j+1] =kästschen
                }
            }

            return view
        }


    override fun onClick(p0: View?) {
        if (p0 != null) {
            println(p0.tag)
            p0.setBackgroundColor(Color.BLUE)
        }

    }

}