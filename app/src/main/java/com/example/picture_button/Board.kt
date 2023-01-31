package com.example.picture_button

import android.graphics.Color
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import core_code.*
import java.util.concurrent.*

// does alot
// in general this file is the board the user sees
class Board : Fragment(), View.OnClickListener {
    var player = true;
    var lastButId = -1;
    lateinit var test: View;
    val gameController = GameController.getGameControler()
    val idToButton: MutableMap<Int, View> = mutableMapOf<Int, View>()
    val mainHandler = startHandlerThread()


    fun startHandlerThread(): Handler {
        var mHandlerThread: HandlerThread? = null
        mHandlerThread = HandlerThread("R3TBoard")
        mHandlerThread.start()
        return Handler(mHandlerThread.looper)
    }

    //define some colours used later
    private val RED_PRIMARY = Color.rgb(245, 78, 78);
    private val RED_SECONDARY = Color.rgb(171, 14, 14);
    private val BLUE_PRIMARY = Color.rgb(78, 98, 245);
    private val BLUE_SECONDARY = Color.rgb(14, 14, 171);
    private lateinit var firstPlayer: playerType;

    //used for comparison and
    enum class playerType {
        KI,
        HUMAN,
        REMOTE,
        DAISY,
        KI_LEO,
        RANDOM,
        KI_SANDER,
        EVELINE,
        OLOI,
    }

    // val cachefilename = "cachedboard";

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println("onCreate")
        /* Is only needed if the onresume and on stop are active
        val files: Array<out File>? = context?.cacheDir?.listFiles()
        println(files?.size)
        if (files?.size != 0) {
            var cachefile = files?.get(0)
            if(cachefile?.name?.startsWith(cachefilename) == true)
                cachefile.delete()
        }*/
        val view = inflater.inflate(R.layout.grid_test_test, container, false)
        val cancel = view.findViewById<Button>(R.id.Cancel)
        cancel.setOnClickListener() {
            UDPtesting.stopUDPBroadcasting()
            findNavController().navigate(R.id.action_board_to_homeFragment)
        }

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

    // this is a wraper funktion to ensure that a player move may take as long as they nead to
    // without blocking the UI thread
    private val exec = Executors.newSingleThreadExecutor()
    fun activate(player: Player): Future<Int?>? {
        return exec.submit<Int>(Callable {
            player.move(gameController.lastMove)
        })
    }

    //Do general setup code and start the game
    override fun onStart() {
        super.onStart()
        val gameController = GameController.getGameControler()
        gameController.reset()

        //fist grab the extea data send over

        val receivedPlayer1 = arguments?.getSerializable("player1")
        val receivedPlayer2 = arguments?.getSerializable("player2")
        if (receivedPlayer1 == null || receivedPlayer2 == null) {
            throw NullPointerException("Designtime issue: Board received no Player-types")
        }
        var extraInfo = arguments?.getSerializable("remoteInformation")

        //second create the Player objects

        val player1 = playerDeclaration(receivedPlayer1 as playerType)
        firstPlayer = receivedPlayer1
        player1.setBoard(this)
        val player2 = playerDeclaration(receivedPlayer2 as playerType)
        player2.setBoard(this)
        updateBoardColers()
        var overlay = view?.findViewById<ConstraintLayout>(R.id.overlay)!!

        // third set up TCP if needed

        var setup: Future<Boolean>? = null
        if (player1 is RemoteHost) {
            //if the first player is a remote setup a cient system
            overlay.visibility = View.VISIBLE  ///show diferent text maybe
            setup = setupRemote(player1, extraInfo as String)
        } else if (player2 is RemoteHost) {
            // if the second player is a remote setup the a server
            overlay.visibility = View.VISIBLE
            setup = setupRemote(player2, extraInfo as String)
        } else {
            //start the game
            mainLoop(player1, player2)
        }

        // forth wait until the setup is done

        if (setup != null) {
            mainHandler.post(object : Runnable {
                override fun run() {

                    if (setup.isDone) {
                        mainLoop(player1, player2)             //start the game
                    } else {
                        mainHandler.postDelayed(this, 10)
                    }
                }
            })
        }
    }

    // converts the playerType enum into a new object
    private fun playerDeclaration(input: playerType): Player {
        return when (input) {
            playerType.HUMAN -> {
                Human()
            }
            playerType.KI_LEO -> {
                Leo_alg()
            }
            playerType.REMOTE -> {
                RemoteHost()
            }
            playerType.RANDOM -> {
                Random()
            }
            playerType.DAISY -> {
                Liz_alg()
            }
            playerType.EVELINE -> {
                Eveline()
            }
            playerType.OLOI -> {
                Oloi()
            }
            else -> {
                println("ist noch nicht implementiert")
                Human()
            }
        }
    }

    //wrapper to be non ui blocking
    fun setupRemote(player: RemoteHost, extraInfo: String): Future<Boolean>? {
        return exec.submit<Boolean>(Callable {
            player.init(extraInfo)
        })
    }

    // Run the manin part of the game by asking the players for a move
    fun mainLoop(player1: Player, player2: Player) {

        UDPtesting.stopUDPBroadcasting()

        // hide the waiting for client mesage
        activity?.runOnUiThread {
            var overlay = view?.findViewById<ConstraintLayout>(R.id.overlay)!!
            overlay.visibility = View.GONE
        }

        //start by asking the first player to make a move
        var activePlayer = activate(player1)
            ?: throw NullPointerException("Sander fault: Starting game withe the first player faield")

        // run this task non ui blocking
        //this task gets called every 10 millis working effectively as while(true)
        val handel = Handler(Looper.getMainLooper())
        handel.post(object : Runnable {
            override fun run() {
                if (activePlayer.isDone) { // wait until the player is done moving
                    val move = activePlayer.get()
                        ?: throw NullPointerException("Sander fault: move ist NaN")

                    //check if this is a valid move
                    if (!gameController.checkMove(move)) {
                        endOfGameBySurrender()
                        return
                    }
                    // add move to the move list and apply it too the game board
                    gameController.addMove(move, player)
                    // check and display win message if applies
                    if (checkWin(move)) {
                        updateBoardColers()

                        //with TCP we have to send the final move
                        if (player && player2 is RemoteHost) {
                            player2.infoEndOfGame(move);
                        } else if (!player && player1 is RemoteHost) {
                            player1.infoEndOfGame(move);
                        }
                        return
                    }

                    updateBoardColers()

                    //ask the next player for a move
                    player = !player
                    activePlayer = (if (player) {
                        activate(player1)
                    } else {
                        activate(player2)
                    })!!
                }
                // repeate main loop endlessly (until game ends)
                mainHandler.postDelayed(this, 10)
            }
        })
    }



    //region EndScreen Mesages
    // sets surender messages
    fun endOfGameBySurrender() {

        if (player) {
            showEndScreen("Blau gibt auf ")
        } else {
            showEndScreen("Rot gibt auf")
        }
    }

    //calls the gameController checkWin and depending on the result shows a message
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
            -420 -> {// draw
                showEndScreen("Unentschieden")
                return true
            }
            in 1..9 -> {
                //updateCrate(res * 10)
            }
        }
        return false;

    }
    fun showEndScreen(winText: String) {


        activity?.runOnUiThread {

            var Winning_text_view = view?.findViewById<TextView>(R.id.text_winner)
            Winning_text_view?.setText(winText)
            Winning_text_view?.visibility = View.VISIBLE
        }
    }
    //endregion

    // my really stupid solution to implementing the humans ability to click
    //i simply store the last buton the human has clicked on
    // the human.move reset this before asking
    override fun onClick(p0: View) {
        val move = p0.tag as Int
        lastButId = move
    }

    fun updateBoardColers() {

        // at one one point i surrender
        // this function updates EVERY button to make sure they are cooler correctly
        // this is a less eficient aprotch i just hope it gets optimized away

        for (i in 0 until 9) {
            for (j in 0 until 10) {
                val id = ((i + 1) * 10 + j)
                if (j == 0) { // case for Crate \ background
                    if (GameController.gamebord[id] == 0) {
                        idToButton[id]?.setBackgroundColor(Color.rgb(255, 255, 255))
                    } else if (GameController.gamebord[id] == 3) {
                        idToButton[id]?.setBackgroundColor(BLUE_SECONDARY)

                    } else if (GameController.gamebord[id] == 5) {
                        idToButton[id]?.setBackgroundColor(RED_SECONDARY)
                    } else {
                        idToButton[id]?.setBackgroundColor(Color.YELLOW)

                    }


                } else if (GameController.gamebord[id] == 0) {// case no plyer has played here

                    if (gameController.checkMove(id)) {// is it a legal move for the next player

                        idToButton[id]?.setBackgroundColor(Color.GRAY)
                    } else {

                        idToButton[id]?.setBackgroundColor(Color.BLACK)
                    }
                } else {// a player has played here force the colors
                    if (GameController.gamebord[id] == 3) {
                        idToButton[id]?.setBackgroundColor(BLUE_PRIMARY)
                    } else {
                        idToButton[id]?.setBackgroundColor(RED_PRIMARY)
                    }
                }
            }
        }
    }

    //helper function used by ais to determent weather they are the first or second player
    fun getP1(): playerType {
        return firstPlayer;
    }


///ALLES LEOS SCHULD :::

/*  works in theorie but makes the App unstable
    override fun onStop() {
        super.onStop()
        println("onStop")
        val cacheFile = File.createTempFile(cachefilename, null, context?.cacheDir)

        cacheFile.writeText(gameController.board.contentToString())

        println(gameController.board.contentToString())
        cacheFile.appendText(gameController.lastMove.toString())


    }

    override fun onResume() {
        super.onResume()
        println("onResume")
        val files: Array<out File>? = context?.cacheDir?.listFiles()
        if (files?.size == 0) return
        val cacheFile = files?.get(0) ?: return
        if(cacheFile?.name?.startsWith(cachefilename) != true)
            return
        if (!cacheFile.exists()) {
            println("File does not exist")
            return
        }
        val read = cacheFile.readText()
        println(read)
        var array: IntArray = IntArray(100)
        var j = 0
        var i = 0
        var lastMove = 0;
        var afterarray = false
        while (i < read.length - 1) {
            if (!afterarray) {
                when (read[i].code) {
                    48 -> {
                        array[j] = 0;j++
                    }
                    51 -> {
                        array[j] = 3;j++
                    }
                    53 -> {
                        array[j] = 5;j++
                    }
                    93 -> afterarray = true
                }
            } else {
                lastMove = (read[i].code - 48) * 10
                lastMove += read[i + 1].code - 48
                println(lastMove)
            }
            i++
        }
        gameController.lastMove = lastMove
        println(array[lastMove])
        player = array[lastMove] != 3

        gameController.board = array
        cacheFile.delete()

        for (i in 10..99) {
            if (array[i] == 0) continue
            if (array[i] == 3) {
                idToButton[i]?.setBackgroundColor(Color.rgb(78, 98, 245))

            } else {
                idToButton[i]?.setBackgroundColor(Color.rgb(245, 78, 78))

            }
        }
        updateBoardColers()
    }
*/

}


