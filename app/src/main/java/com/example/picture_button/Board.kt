package com.example.picture_button

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import core_code.GameController
import core_code.Human
import java.util.concurrent.Executor


class Board : Fragment(), View.OnClickListener {
    var player = true;
    var lastButId = -1;
    lateinit var test: View;
    val gameController = GameController.getGameControler()
    val idToButton: MutableMap<Int, View> = mutableMapOf<Int,View>()
    private lateinit var executor: Executor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.grid_test_test, container, false)


        val root = view.findViewById<GridLayout>(R.id.root)

        for (i in 0 until root.childCount) {
            val kasten: GridLayout =
                (root.getChildAt(i) as ConstraintLayout).getChildAt(0) as GridLayout

            for (j in 0 until kasten.childCount) {
                val kästschen = kasten.getChildAt(j)
                if (i == 0 && j == 0)
                    test = kästschen
                kästschen.setBackgroundColor(Color.BLACK)
                kästschen.tag = (i + 1) * 10 + j + 1
                kästschen.setOnClickListener(this)
                idToButton[(i+1)*10 +j+1] =kästschen
            }
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        println("onStart")
        val gameController = GameController.getGameControler()

        val p1 = Human()
        p1.setBoard(this)
        val p2 = Human()
        p2.setBoard(this)

    }

    fun setButton() {
        test.setBackgroundColor(Color.RED)
    }

    override fun onClick(p0: View?) {
        if (p0 != null) {
            val move = p0.tag as Int
            if (gameController.checkMove(move)) {

                println(p0.tag)
                if (player) {
                    p0.setBackgroundColor(Color.CYAN)
                    idToButton[gameController.lastMove]?.setBackgroundColor(Color.RED)
                }else{
                    p0.setBackgroundColor(Color.MAGENTA)
                    idToButton[gameController.lastMove]?.setBackgroundColor(Color.BLUE)
                }


                GameController.gamebord[move] = if (player) 3 else 5
                if (0 != gameController.checkWin(move)) {
                    println("Player won")
                }
                gameController.lastMove = move
                player = !player;
                for (i in 0 until 9) {
                    for (j in 0 until 9) {
                        val id = ((i + 1) * 10 + j + 1)
                        if (GameController.gamebord[id] == 0) {
                            if (gameController.checkMove(id)) {

                                idToButton[id]?.setBackgroundColor(Color.GRAY)
                            } else {

                                idToButton[id]?.setBackgroundColor(Color.BLACK)

                            }
                        }
                    }
                }
            }

            //  lastButId = p0.tag as Int;
        }
    }
}

