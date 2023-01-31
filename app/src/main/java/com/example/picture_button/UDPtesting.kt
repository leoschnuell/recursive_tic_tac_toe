package com.example.picture_button

import android.content.Context
import android.net.wifi.WifiManager
import android.os.*
import android.os.StrictMode.ThreadPolicy
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.awaitAll
import java.lang.Thread.sleep
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.util.*


// this code does the general searching and finding of other devices
// it is a buggy mes and if you hit som buttons twice or go back and forth
//the application will crash
// i am guessing that this is du to me not cleaning up properly
// or something related to thread still running when they student
// the truth is i simply don't have the knowledge to debug this
// reproduce exambel bug : host game -> cancel -> host game -> pres any square -> TCP ini error
class UDPtesting : Fragment() {

    lateinit var listOfOthers: LinearLayout
    lateinit var playerName: EditText
    lateinit var searchGame: Button
    lateinit var hostGame: Button

    private var mainHandler: Handler = startHandlerThread("R3TNetworking")
    private var brotcaster: Handler = startHandlerThread("R3TBroadcaster")

    val msgQueue: Queue<DataGroup> = LinkedList()

// helper function to start ture new thread and have them run independent
    fun startHandlerThread(name: String): Handler {
        var mHandlerThread: HandlerThread? = null
        mHandlerThread = HandlerThread(name)
        mHandlerThread.start()
        return Handler(mHandlerThread.looper)
    }


    companion object {
        var broadcastActive = false
        // this way i can disable the broadcasting from every where
        fun stopUDPBroadcasting() {
            broadcastActive = false
        }
    }

    //simple grouping of my common data
    class DataGroup(var name: String, var ip: String, var timeOut: Int = 0)

    //these 2 functions are my attempt at improving the stability
    // they enable and disable the buttons
    fun setBroadcastActiveTrue() {
        broadcastActive = true
        hostGame.isEnabled = false
        searchGame.isEnabled = false

    }
    fun reset()
    {
        // i trie to stop l the running thread here
        mainHandler.removeCallbacksAndMessages(null)
        brotcaster.removeCallbacksAndMessages(null)

        broadcastActive = false;
        hostGame.isEnabled = true
        searchGame.isEnabled = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        //first get all the buttons we may need
        val view = inflater.inflate(R.layout.fragment_show_player2, container, false)
        playerName = view.findViewById<EditText>(R.id.hostName)
        listOfOthers = view.findViewById<LinearLayout>(R.id.foundPlayers)
        searchGame = view.findViewById<Button>(R.id.search_game)
        hostGame = view.findViewById<Button>(R.id.host_game)
        reset()

        val reset = view.findViewById<Button>(R.id.resetAll)

        reset.setOnClickListener {
            reset()
        }

        // starts a UDP broadcast  server
        // and the switches to the game immediately
        // this is done so that the TCP server sits within the Board
        hostGame.setOnClickListener(View.OnClickListener {
            startUDPBroadcasting();
            val bundle = Bundle()
            bundle.putSerializable("player1", Board.playerType.HUMAN)
            bundle.putSerializable("player2", Board.playerType.REMOTE)
            bundle.putSerializable("remoteInformation", "Host")
            findNavController().navigate(R.id.action_UDPtesting_to_board, bundle)

        })

        searchGame.setOnClickListener {
            getUDPBroadcastMessages()
            processMessages()
        }

        return view;
    }

    private fun getUDPBroadcastMessages() {
        // this thread waits for a UDP mesage to then ad it to a queue
        // this que gets worked of in the processMessages()
        if (broadcastActive) {
            // ensure that only one broadcaster is running
            return;
        }

        setBroadcastActiveTrue()

        brotcaster.post {  //recive incoming UDP pagages and send tem to a queue
            val socket = DatagramSocket(5005)
            val recvBuf = ByteArray(500)
            val packet = DatagramPacket(recvBuf, recvBuf.size)
            while (broadcastActive) {
                socket.receive(packet) // this may be the root coarse of errors
                val remoteIP: String = packet.address.hostAddress as String
                var data = String(packet.getData()).trim { it <= ' ' }
                println("recived $data")
                msgQueue.add(DataGroup(data, remoteIP))
            }
            socket.close()
        }
    }


    private fun processMessages() {
        // go through the mesaged que
        // check if we have seen this ip alredy (in managedData)
        //     true -> reset the timeout of this ip
        //     false -> add this ip to managedData
        //every run we up the timeout
        //if the timeout reaches 1 second we drop this ip from managedData

        if (broadcastActive) {
            // ensure that only one broadcaster is running
            return;
        }

        val managedData = LinkedList<DataGroup>()

        mainHandler.post(//periodically go through msgQueue
            object : Runnable {
                override fun run() {
                    if (!broadcastActive)
                        return;
                    managedData.forEach {
                        it.timeOut++;
                        if (it.timeOut > 10) {
                            managedData.remove(it)
                            var button = view!!.findViewWithTag<Button>(it.name + it.ip)
                            activity?.runOnUiThread {
                                listOfOthers.removeView(button)
                            }
                        }
                    }
                    while (msgQueue.size > 0) {
                        val dataPoint = msgQueue.poll() ?: break
                        var flag = true
                        managedData.forEach {
                            if (it.ip == dataPoint.ip) {
                                dataPoint.timeOut = 0
                                flag = false
                            }
                        }
                        if (flag) {
                            managedData.add(dataPoint)
                            activity?.runOnUiThread {
                                addBtnToUi(dataPoint)
                            }
                        }
                    }
                    mainHandler.postDelayed(this, 100)
                }
            })
    }



    fun startUDPBroadcasting() {
        if (broadcastActive) {
            // ensure that only one broadcaster is running
            return;
        }

        // do basic setup
        var messageStr: String = playerName.text.toString();
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val socket = DatagramSocket()
        socket.broadcast = true
        val sendData: ByteArray = messageStr.toByteArray()

        // get the local Broadcast address
        val wifiManager = context?.getSystemService(Context.WIFI_SERVICE) as WifiManager
        var localIP: String =
            Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)
       // wifiManager.dhcpInfo.netmask
        localIP = localIP.replaceAfterLast(".", "255")


        // repeatedly send udp packages via UDP
        setBroadcastActiveTrue()
        mainHandler.post(object : Runnable {
            override fun run() {
                if (broadcastActive) {
                    val sendPacket: DatagramPacket = DatagramPacket(
                        sendData,
                        sendData.size,
                        InetAddress.getByName(localIP),
                        5005
                    )
                    socket.send(sendPacket)
                    mainHandler.postDelayed(this, 100)
                } else {
                    socket.close()
                }
            }
        })
    }


    // set up a buton and add it to the ui with the specified data
    fun addBtnToUi(data: DataGroup) {
        val btn = Button(context)
        btn.tag = data.name + data.ip
        btn.layoutParams =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )


        btn.text = data.name
        btn.setOnClickListener {


            val bundle = Bundle()
            bundle.putSerializable("player1", Board.playerType.REMOTE)
            bundle.putSerializable("player2", Board.playerType.HUMAN)
            bundle.putSerializable("remoteInformation", data.ip)
            findNavController().navigate(R.id.action_UDPtesting_to_board, bundle)

        }
        stopUDPBroadcasting()
        listOfOthers.addView(btn)
    }
}