package core_code

import com.example.picture_button.Board
import com.example.picture_button.UDPtesting
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.ServerSocket
import java.net.Socket

const val PORT = 9700;

class RemoteHost : Player {

    lateinit var socket: Socket;
    private lateinit var reciver: DataInputStream;
    private lateinit var sender: DataOutputStream;


    fun init(ip: String): Boolean {
        if (ip == "Host") {
            return waitForClient()
        } else {
            return connectToServer(ip)
        }
    }

    fun connectToServer(ip: String): Boolean {
        socket = Socket(ip, PORT)
        setup()
        return true;
    }

    fun waitForClient(): Boolean {
        val serverSocket = ServerSocket(PORT)
        socket = serverSocket.accept()// blocking behavior
        //add confirm dialog
        setup()
        UDPtesting.stopUDPBroadcasting()
        return true;
    }

    fun setup() {
        reciver = DataInputStream(socket.getInputStream())
        sender = DataOutputStream(socket.getOutputStream())

    }


    override fun move(lastMove: Int): Int {
        if (lastMove != 1)
            sender.writeInt(lastMove)
        return reciver.readInt();
    }

    fun infoEndOfGame(lastMove: Int)
    {
        sender.writeInt(lastMove)
    }


    override fun setBoard(board: Board?) {

    }

}