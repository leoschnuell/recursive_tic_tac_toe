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
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.util.*


class UDPtesting : Fragment() {


    lateinit var listOfOthers: LinearLayout
    lateinit var playerName: EditText
    lateinit var searchGame :Button
    lateinit var hostGame :Button

    //  val mainHandler = Handler(Looper.getMainLooper())
    private var mainHandler: Handler = startHandlerThread("R3TNetworking")
    val msgQueue: Queue<DataGroup> = LinkedList()


    fun startHandlerThread(name: String): Handler {
        var mHandlerThread: HandlerThread? = null
        mHandlerThread = HandlerThread(name)
        mHandlerThread.start()
        return Handler(mHandlerThread.looper)
    }


    companion object {
        var broadcastActive = false


        fun stopUDPBroadcasting() {
            broadcastActive = false
        }
    }

    class DataGroup(var name: String, var ip: String, var timeOut: Int = 0)

   fun setBroadcastActiveTrue()
   {
       broadcastActive = true
       hostGame.isEnabled = false
       searchGame.isEnabled = false

   }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_show_player2, container, false)
        playerName = view.findViewById<EditText>(R.id.hostName)
        listOfOthers = view.findViewById<LinearLayout>(R.id.foundPlayers)
        searchGame = view.findViewById<Button>(R.id.search_game)
        hostGame = view.findViewById<Button>(R.id.host_game)
        val reset = view.findViewById<Button>(R.id.resetAll)

        reset.setOnClickListener{
            broadcastActive = false;
            hostGame.isEnabled = true
            searchGame.isEnabled = true
            //TODO: stop all thed maybe ?
        }


        hostGame.setOnClickListener(View.OnClickListener {
            //open a tcp server and send udp broadcasts

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
            return;
        }

        setBroadcastActiveTrue()
        var brotcaster = startHandlerThread("R3TBroadcaster")

        brotcaster.post {  //recive incoming UDP pagages and send tem to a queue
            val socket = DatagramSocket(5005)
            val recvBuf = ByteArray(500)
            val packet = DatagramPacket(recvBuf, recvBuf.size)
            while (broadcastActive) {
                socket.receive(packet)
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
            // wenn es bereits einen aktiven pinger gibt muss dieser geschloßen werden
            // vl solste indiesem val die suche neugstartet werden
            return;
        }


        var messageStr: String = playerName.text.toString();
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val socket = DatagramSocket()
        socket.broadcast = true
        val sendData: ByteArray = messageStr.toByteArray()

        setBroadcastActiveTrue()
        mainHandler.post(object : Runnable {
            override fun run() {
                if (broadcastActive) {
                    val wifiManager = context?.getSystemService(Context.WIFI_SERVICE) as WifiManager
                    var localIP: String =
                        Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)
                    wifiManager.dhcpInfo.netmask
                    localIP = localIP.replaceAfterLast(".", "255")


                    val sendPacket: DatagramPacket = DatagramPacket(
                        sendData,
                        sendData.size,
                        InetAddress.getByName(localIP),
                        5005
                    )
                    socket.send(sendPacket)
                    mainHandler.postDelayed(this, 100)
                }
            }
        })
    }

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