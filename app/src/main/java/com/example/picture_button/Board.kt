package com.example.picture_button

import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import core_code.GameController
import core_code.Human
import core_code.Player
import kotlinx.coroutines.delay
import java.util.concurrent.*
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime


class Board : Fragment(), View.OnClickListener {
    var player = true;
    var lastButId = -1;
    lateinit var test: View;
    val gameController = GameController.getGameControler()
    val idToButton: MutableMap<Int, View> = mutableMapOf<Int, View>()
    private lateinit var executor: Executor

    private val exec = Executors.newSingleThreadExecutor()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println("onCreate")
        val view = inflater.inflate(R.layout.grid_test_test, container, false)
        val root = view.findViewById<GridLayout>(R.id.root)
        AsyncTask.execute {
            for (i in 0 until root.childCount) {
                val kasten: GridLayout =
                    (root.getChildAt(i) as ConstraintLayout).getChildAt(0) as GridLayout

                idToButton[(i + 1) * 10] = kasten as View


                for (j in 0 until kasten.childCount) {
                    val kästschen = kasten.getChildAt(j)
                    if (i == 0 && j == 0)
                        test = kästschen
                    kästschen.setBackgroundColor(Color.BLACK)
                    kästschen.tag = (i + 1) * 10 + j + 1
                    kästschen.setOnClickListener(this)
                    idToButton[(i + 1) * 10 + j + 1] = kästschen
                }
            }
        }
        return view
    }

    fun play(player: Player): Future<Int?>? {
        return exec.submit<Int>(Callable {
                player.move(gameController.lastMove)
        })
    }

    override fun onStart() {
        super.onStart()
        println("onStart")
        val gameController = GameController.getGameControler()

        val p1 = Human()
        p1.setBoard(this)
        val p2 = Human()
        p2.setBoard(this)

        val playing = play(p1) ?: throw NullPointerException("Sander ist schuld")

        val mainHandler = Handler(Looper.getMainLooper())

        mainHandler.post(object : Runnable {
            override fun run() {
                if (playing.isDone) {
                    val move = playing.get()
                    gameController.checkMove(move!!)
                    setFlag(playing.get()!!, true)


                }
                mainHandler.postDelayed(this, 10)
            }
        })
    }
    fun gameStep(){

    }


    fun setFlag(i: Int, player: Boolean) {
        if (player) {
            idToButton[i]?.setBackgroundColor(Color.rgb(14, 14, 171))
        } else {
            idToButton[i]?.setBackgroundColor(Color.rgb(171, 14, 14))
        }
    }

    override fun onClick(p0: View?) {
        if (p0 != null) {
            val move = p0.tag as Int
            if (gameController.checkMove(move)) {

                println(p0.tag)
                if (player) {
                    p0.setBackgroundColor(Color.BLUE)
                    idToButton[gameController.lastMove]?.setBackgroundColor(Color.rgb(245, 78, 78))
                } else {
                    p0.setBackgroundColor(Color.RED)
                    idToButton[gameController.lastMove]?.setBackgroundColor(Color.rgb(78, 98, 245))
                }


                GameController.gamebord[move] = if (player) 3 else 5
                when (val res = gameController.checkWin(move)) {
                    -3 -> {
                        println("First Player has won")
                        var Winning_text_view=view?.findViewById<TextView>(R.id.text_winner)
                        Winning_text_view?.setText("Blue Won")
                        Winning_text_view?.visibility = View.VISIBLE
                    }
                    -5 -> {
                        println("Second Player has won")
                        var Winning_text_view=view?.findViewById<TextView>(R.id.text_winner)
                        Winning_text_view?.setText("Red Won")
                        Winning_text_view?.visibility = View.VISIBLE
                    }
                    in 1..9 -> {
                        setFlag(res * 10, player)
                    }
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

