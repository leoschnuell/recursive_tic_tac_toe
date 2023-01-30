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
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import core_code.RemoteHost
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future


class UDPtesting : Fragment() {


    lateinit var listOfOthers: LinearLayout
    lateinit var playerName: EditText
  //  val mainHandler = Handler(Looper.getMainLooper())
    private var mainHandler: Handler =startHandlerThread("R3TNetworking")


    fun startHandlerThread(name :String):Handler {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_show_player2, container, false)
        playerName = view.findViewById<EditText>(R.id.editText2)
        listOfOthers = view.findViewById<LinearLayout>(R.id.foundPlayers)
        val searchGame = view.findViewById<Button>(R.id.search_game)
        val hostGame = view.findViewById<Button>(R.id.host_game)
        hostGame.setOnClickListener(View.OnClickListener {
            //open a tcp server and send udp broadcasts

            startUDPBroadcasting();
            val bundle = Bundle()
            bundle.putSerializable("player1", Board.playerType.HUMAN)
            bundle.putSerializable("player2", Board.playerType.REMOTE)
            bundle.putSerializable("remoteInformation", "Host")
            findNavController().navigate(R.id.action_UDPtesting_to_board, bundle)
        })

        searchGame.setOnClickListener(
            View.OnClickListener {
                //look for udp brotcasts
                // check if we have sean this ip before

                //on click-> Sed recon request
                println("ON CLICK")
                println("ON CLICK")
                println("ON CLICK")
                println("ON CLICK")

                val msgQueue: Queue<dataGroop> = LinkedList()
                broadcastActive = true
                var brotcaster = startHandlerThread("R3Tbrotcaster")
                brotcaster.post {  //recive incoming UDP pagages and send tem to a queue
                    val socket = DatagramSocket(5005)
                    val recvBuf = ByteArray(500)
                    val packet = DatagramPacket(recvBuf, recvBuf.size)
                    while (broadcastActive) {
                        socket.receive(packet)
                        val remoteIP: String = packet.address.hostAddress as String
                        var data = String(packet.getData()).trim { it <= ' ' }
                        println("recived $data")
                        msgQueue.add(dataGroop(data, remoteIP))
                    }
                }
                val managedData = LinkedList<dataGroop>()

                mainHandler.post(//periodically go through msgQueue
                    object : Runnable {
                        override fun run() {
                            managedData.forEach {
                                it.timeOut++;
                                if (it.timeOut > 10) {
                                    managedData.remove(it)
                                    var button =
                                        view.findViewWithTag<Button>(it.name + it.ip)
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
                                    activity?.runOnUiThread{
                                        addBtnToUi(dataPoint)
                                    }
                                }
                            }

                            mainHandler.postDelayed(this, 100)
                        }
                    })

            }
        )


        return view;
    }

    class dataGroop(var name: String, var ip: String, var timeOut: Int = 0) {
    }


    fun setupTCPserver() {
        var host = RemoteHost();
        host.waitForClient()

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

        broadcastActive = true
        mainHandler.post(object : Runnable {
            override fun run() {
                if (broadcastActive) {
                    val sendPacket: DatagramPacket = DatagramPacket(
                        sendData,
                        sendData.size,
                        InetAddress.getByName("192.168.188.255"),
                        5005
                    )
                    socket.send(sendPacket)
                    mainHandler.postDelayed(this, 100)
                }
            }
        })
    }

    fun addBtnToUi(data: dataGroop) {

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
            bundle.putSerializable("player1", Board.playerType.HUMAN)
            bundle.putSerializable("player2", Board.playerType.REMOTE)
            bundle.putSerializable("remoteInformation", data.ip)
            findNavController().navigate(R.id.action_UDPtesting_to_board, bundle)

        }
        stopUDPBroadcasting()
        listOfOthers.addView(btn)
    }


    fun findOthers() {

        var activeThread = search()
        mainHandler.post(
            object : Runnable {
                override fun run() {
                    if (activeThread!!.isDone) {
                        val packet = activeThread.get()
                            ?: throw NullPointerException("Sander fault: move ist NaN")

                        println("Packet received from: " + packet.address.hostAddress)
                        var data = String(packet.getData()).trim { it <= ' ' }
                        println("Packet received; data: $data")
                        Toast.makeText(
                            activity,
                            "found" + data + "\n" + packet.address.hostAddress,
                            Toast.LENGTH_LONG
                        ).show()


                        addBtnToUi(dataGroop(data, packet.address.hostAddress))

                    } else {
                        mainHandler.postDelayed(this, 100)
                    }
                }
            })


    }

    private val exec = Executors.newSingleThreadExecutor()
    private fun search(): Future<DatagramPacket?>? {
        return exec.submit<DatagramPacket?>(Callable {
            getMSG()
        })
    }


    fun getMSG(): DatagramPacket? {
        var data: String = "no data"
        try {
            val socket = DatagramSocket(5005)
            socket.broadcast = true
            while (true) {

                println("Ready to receive broadcast packets!")
                val recvBuf = ByteArray(500)
                val packet = DatagramPacket(recvBuf, recvBuf.size)
                socket.receive(packet)
                val wifiManager = context?.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val localIP: String =
                    Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)
                val remoteIP: String = packet.address.hostAddress as String
                println("Local address: $localIP")
                println("remote address: $remoteIP")
                if (localIP == remoteIP)
                    continue;

                println("Packet received from: " + packet.address.hostAddress)
                data = String(packet.getData()).trim { it <= ' ' }
                println("Packet received; data: $data")
                socket.close()
                return packet
                /*
                    val localIntent: Intent = Intent(Constants.BROADCAST_ACTION)
                        .putExtra(Constants.EXTENDED_DATA_STATUS, data)
                    LocalBroadcastManager
                        .getInstance(this)
                        .sendBroadcast(localIntent)
                        */
            }
        } catch (ex: IOException) {
            println("Oops" + ex.message)
        }
        return null;
    }


}