package com.example.picture_button

import android.graphics.Color
import android.opengl.Visibility
import android.os.*
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
    val mainHandler = startHandlerThread()
    private lateinit var executor: Executor


    fun startHandlerThread(): Handler {
        var mHandlerThread: HandlerThread? = null
        mHandlerThread = HandlerThread("R3TBoard")
        mHandlerThread.start()
        return Handler(mHandlerThread.looper)
    }

    enum class playerType {
        KI, HUMAN, REMOTE, KI_LIZ, KI_LEO, KI_SANDER
    }


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

    private val exec = Executors.newSingleThreadExecutor()
    fun activate(player: Player): Future<Int?>? {
        return exec.submit<Int>(Callable {
            player.move(gameController.lastMove)
        })
    }

    override fun onStart() {
        super.onStart()
        val gameController = GameController.getGameControler()
        gameController.reset()

        val receivedPlayer1 = arguments?.getSerializable("player1")
        val receivedPlayer2 = arguments?.getSerializable("player2")


        if (receivedPlayer1 == null || receivedPlayer2 == null) {
            throw NullPointerException("Designtime issue: Board received no Player-types")
        }
        var ip: String;

        var extraInfo = arguments?.getSerializable("remoteInformation")


        val player1 = playerDeclaration(receivedPlayer1 as playerType)
        player1.setBoard(this)
        player1.isBeginning(true)
        val player2 = playerDeclaration(receivedPlayer2 as playerType)
        player2.setBoard(this)
        player2.isBeginning(false)
        updateBoardHiliting()
        var overlay = view?.findViewById<ConstraintLayout>(R.id.overlay)!!
        var setup: Future<Boolean>? = null
        if (player1 is RemoteHost) {
            //if the first player is a remote setup a cient system
            overlay.visibility = View.VISIBLE  ///show diferent text maybe
            setup = setupRemote(player1, extraInfo as String)

            //wir sind client


        } else if (player2 is RemoteHost) {
// if the second plyer is a remote setup the a server
            overlay.visibility = View.VISIBLE


            setup = setupRemote(player2, extraInfo as String)


        } else {
            //
            mainLoop(player1, player2)
        }
        if (setup != null) {
            mainHandler.post(object : Runnable {
                override fun run() {

                    if (setup.isDone) {
                        mainLoop(player1, player2)
                    } else {
                        mainHandler.postDelayed(this, 10)
                    }
                }
            })
        }
    }


    fun mainLoop(player1: Player, player2: Player) {
        UDPtesting.stopUDPBroadcasting()

        var activePlayer = activate(player1)
            ?: throw NullPointerException("Sander fault: Starting game withe the first player faield")
        activity?.runOnUiThread {
            var overlay = view?.findViewById<ConstraintLayout>(R.id.overlay)!!
            overlay.visibility = View.GONE
        }
        val handel = Handler(Looper.getMainLooper())
        handel.post(object : Runnable {
            override fun run() {
                if (activePlayer.isDone) {
                    val move = activePlayer.get()
                        ?: throw NullPointerException("Sander fault: move ist NaN")

                    if (!gameController.checkMove(move)) {
                        endOfGame()
                        return
                    }
                    updateCasket(move)
                    gameController.addMove(move, player)

                    if (checkWin(move)) {
                        updateCrate((move / 10) * 10)
                        return
                    }
                    updateBoardHiliting()
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

    fun setupRemote(player: RemoteHost, extraInfo: String): Future<Boolean>? {
        return exec.submit<Boolean>(Callable {
            player.doTheThingJoline(extraInfo)
            // i don't know why but it has to be a separate function
        })
    }


    fun updateCrate(i: Int) {
        activity?.runOnUiThread {
            if (player) {
                idToButton[i]?.setBackgroundColor(Color.rgb(14, 14, 171))
            } else {
                idToButton[i]?.setBackgroundColor(Color.rgb(171, 14, 14))
            }
        }
    }

    fun updateCasket(move: Int) {
        activity?.runOnUiThread {
            if (player) {
                idToButton[move]?.setBackgroundColor(Color.BLUE)
                idToButton[gameController.lastMove]?.setBackgroundColor(Color.rgb(245, 78, 78))
            } else {
                idToButton[move]?.setBackgroundColor(Color.RED)
                idToButton[gameController.lastMove]?.setBackgroundColor(Color.rgb(78, 98, 245))
            }
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
                updateCrate(res * 10)
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
        activity?.runOnUiThread {

            var Winning_text_view = view?.findViewById<TextView>(R.id.text_winner)
            Winning_text_view?.setText(winText)
            Winning_text_view?.visibility = View.VISIBLE
        }
    }

    fun playerDeclaration(input: playerType): Player {
        return when (input) {
            playerType.HUMAN -> {
                Human()
            }
            playerType.KI_LEO -> {
                leo_alg()
            }
            playerType.REMOTE -> {
                RemoteHost()
            }
            else -> {
            Board.playerType.KI_LIZ ->{
                Liz_alg()
            }
            else->{
                println("ist noch nicht implementiert")
                Human()
            }
        }
    }

    fun cancelUDPAndBack() {
        UDPtesting.stopUDPBroadcasting()
        findNavController().navigate(R.id.action_board_to_homeFragment)

    }


}
