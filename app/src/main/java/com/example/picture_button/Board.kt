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
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import core_code.GameController
import core_code.Human
import core_code.Player
import core_code.leo_alg
import java.util.concurrent.*


class Board : Fragment(), View.OnClickListener {
    var player = true;
    var lastButId = -1;
    lateinit var test: View;
    val gameController = GameController.getGameControler()
    val idToButton: MutableMap<Int, View> = mutableMapOf<Int, View>()
    private lateinit var executor: Executor
    enum class playerType{
       KI,HUMAN,REMOTE,KI_LIZ,KI_LEO,KI_SANDER
    }

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

    fun activate(player: Player): Future<Int?>? {
        return exec.submit<Int>(Callable {
            player.move(gameController.lastMove)
        })
    }

    override fun onStart() {
        super.onStart()
        val gameController = GameController.getGameControler()
        gameController.reset()

        var receivedPlayer1 = arguments?.getSerializable("player1")
        var receivedPlayer2 = arguments?.getSerializable("player2")
        val player1 = playerDeclaration(receivedPlayer1 as playerType)
        player1.setBoard(this)
        player1.is_beginning(true)
        val player2 = playerDeclaration(receivedPlayer2 as playerType)
        player2.setBoard(this)
        player2.is_beginning(false)
        updateBoardHiliting()

        var activePlayer = activate(player1) ?: throw NullPointerException("Sander fault: Starting game withe the first player faield")

        val mainHandler = Handler(Looper.getMainLooper())

        mainHandler.post(object : Runnable {
            override fun run() {
                if (activePlayer.isDone) {
                    val move = activePlayer.get()?:throw NullPointerException("Sander fault: move ist NaN")

                    if (!gameController.checkMove(move)) {
                        endOfGame()
                        return
                    }
                    update_kästchen(move)
                    gameController.add_move(move, player)
                    updateBoardHiliting()

                    if (checkWin(move)) {
                        update_kasten((move / 10) * 10)
                        return
                    }
                    player = !player
                    activePlayer = (if (player) {
                        activate(player1)
                    } else {
                        activate(player2)
                    })!!
                }
                mainHandler.postDelayed(this, 10)
            }
        })
    }


    fun update_kasten(i: Int) {
        if (player) {
            idToButton[i]?.setBackgroundColor(Color.rgb(14, 14, 171))
        } else {
            idToButton[i]?.setBackgroundColor(Color.rgb(171, 14, 14))
        }
    }

    fun update_kästchen(move: Int) {
        if (player) {
            idToButton[move]?.setBackgroundColor(Color.BLUE)
            idToButton[gameController.lastMove]?.setBackgroundColor(Color.rgb(245, 78, 78))
        } else {
            idToButton[move]?.setBackgroundColor(Color.RED)
            idToButton[gameController.lastMove]?.setBackgroundColor(Color.rgb(78, 98, 245))
        }

    }


    fun checkWin(move: Int): Boolean {
        when (val res = gameController.checkWin(move)) {
            -3 -> {
                showEndScreen("Blue won")
                return true;
            }
            -5 -> {
                showEndScreen("Red won")
                return true;

            }
            in 1..9 -> {
                update_kasten(res * 10)
            }
        }
        return false;

    }


    override fun onClick(p0: View) {
        val move = p0.tag as Int
        lastButId = move
    }

    fun updateBoardHiliting() {
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


    fun endOfGame() {
        if (player) {
            showEndScreen("Blau gewinnt")
        } else {
            showEndScreen("Red gewinnt")
        }
    }

    fun showEndScreen(winText: String) {
        var Winning_text_view = view?.findViewById<TextView>(R.id.text_winner)
        Winning_text_view?.setText(winText)
        Winning_text_view?.visibility = View.VISIBLE
    }

    fun playerDeclaration(playerType: playerType): Player {
        return when(playerType){
            Board.playerType.HUMAN ->{
                Human()
            }
            Board.playerType.KI_LEO ->{
                 leo_alg()
            }
            else->{
                println("ist noch nicht implementiert")
                Human()
            }
        }
    }
}



