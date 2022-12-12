package com.example.picture_button

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import core_code.GameController
import core_code.Human
import core_code.Player
import core_code.leo_alg

/*
TODO:
    frontend
        interfaces für verschiedene screen
        chose ai sreen
        chose human player via lokac networc
    backend
        create xml gameorad using kotlin and setup backend conection
        create funktion that updates a buton sprite
            updateButon(PLAYER):void
        rewrite the function checking if a move is valid
        limit user input to valid butons only
            (Plan on how to do [sugestion disabel enabel butons])
        write a funtion that sends a udp brotcast to the local networ and lisens for other devices
            Plan: how shoud the systems interact
* */



class MainActivity : AppCompatActivity(), View.OnClickListener {
    val idTObutton: MutableMap<Int, View> = mutableMapOf<Int, View>()
    //var player = true
    var last_valid_click=0
    private val gameController = GameController.getGameControler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.grid_test_test)

        var p1 = setPlayer(intent.extras?.get("p1") as String)
        var p2 = setPlayer(intent.extras?.get("p2") as String)
        gameController.game_setup(p1,p2)

        val root = findViewById<GridLayout>(R.id.root)

        for (i in 0 until root.childCount) {
            val kasten: GridLayout =
                (root.getChildAt(i) as ConstraintLayout).getChildAt(0) as GridLayout

            for (j in 0 until kasten.childCount) {
                val kästschen = kasten.getChildAt(j)
                kästschen.setBackgroundColor(Color.BLACK)
                kästschen.tag = (i + 1) * 10 + j + 1


                kästschen.setOnClickListener(this)
                idTObutton[(i + 1) * 10 + j + 1] = kästschen
            }
        }

        //val a1 = findViewById<Button>(R.id.a1)
        //a1.setBackgroundColor(0)

        /*


        val kasten2 = findViewById<ConstraintLayout>(R.id.kasten2)
        //   val a1 = kasten2.findViewById<Button>(R.id.a1)
        var i = 5;

        println((kasten2 as ConstraintLayout).childCount)
        for (index in 0 until kasten2.childCount) {
            val nextChild = kasten2.getChildAt(index)
            nextChild.setBackgroundColor(123456)
        }


        val resourceId = kasten2.resources.getIdentifier(
            "a$i",
            "id", this.packageName
        )
        val test = findViewById<View>(resourceId) as Button

        kasten2.setBackgroundColor(34)
        a1.text = "a1";
        a1.setPadding(0)
        a1.setBackgroundColor(0)
        test.setBackgroundColor(1)
*/


    }

  /*  fun gameLoop(p1: Player, p2: Player): Player? {
        var first_player_turn = true
        var move = 0
        // returns the winner
        while (true) {
            if (first_player_turn) {
                move = p1.move(gameController.lastMove)
                println("S1: $move")
            } else {
                move = p2.move(gameController.lastMove)
                println("S2: $move")
            }
            if (move == 100 || !gameController.checkMove(move)) {
                println("move:$move was invalid")
                return if (!first_player_turn) p1 else p2
            }
            gameController.board[move] = if (first_player_turn) 3 else 5
            gameController.add_move(move)
            if (0 != gameController.checkWin(move)) return if (first_player_turn) p1 else p2

            gameController.lastMove = move
            first_player_turn = !first_player_turn
        }
    }*/


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    fun get_vaild_input():kotlin.Int{

        while (true)
        {
            return 0
        }

    }

    override fun onClick(p0: View?) {

        if (p0 != null) {
            println(p0.tag)
            if (gameController.checkMove(p0.tag as Int))

            {
                last_valid_click = p0.tag as Int
            }
            //player = !player
        }



    }

    fun setButton(id: Int, player: Boolean) {
        if (player) {
            idTObutton.get(id)?.setBackgroundColor(Color.RED)
        } else {
            idTObutton.get(id)?.setBackgroundColor(Color.BLUE)

        }
    }

    fun setPlayer( str :String): Player
    {
        return when (str) {
            "Human" -> default_human()
            "leo_alg" -> leo_alg()
            else-> {

                println("well fuck")
                default_human()
            }
        }
    }
    private fun default_human():Human
    {
        var h = Human()
        h.setMainActivity(this)
        return h
    }


}