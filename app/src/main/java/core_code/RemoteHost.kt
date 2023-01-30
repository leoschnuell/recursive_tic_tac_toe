package core_code

import android.os.Looper
import com.example.picture_button.Board
import com.example.picture_button.UDPtesting
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.ServerSocket
import java.net.Socket

const val PORT = 9700;

class RemoteHost : Player {

    var is_beginning: Boolean = true;
    lateinit var socket: Socket;
    private val mainHandler = android.os.Handler(Looper.getMainLooper())
    private lateinit var reciver: DataInputStream;
    private lateinit var sender: DataOutputStream;


    fun doTheThingJoline(ip: String): Boolean {
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

    override fun isBeginning(b: Boolean) {
        is_beginning = b;
    }

    override fun setBoard(board: Board?) {

    }

    override fun hasWon(): String {
        return "null"
    }
}