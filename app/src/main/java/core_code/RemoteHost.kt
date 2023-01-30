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
    private  lateinit var sender: DataOutputStream;
    var done =0;


    fun connectToServer(ip: String) {
        // mainHandler.post {
        socket = Socket(ip, PORT)
        setup()
        var sys = 0;
        sys = 1
        // }

    }

    fun waitForClient() {
        val serverSocket = ServerSocket(PORT)

        mainHandler.post {
            socket = serverSocket.accept()// blocking behavior
            //add confirm dialog
            setup()
            UDPtesting.stopUDPBroadcasting()

        }
    }

    fun setup() {
        reciver = DataInputStream(socket.getInputStream())
        sender = DataOutputStream(socket.getOutputStream())
        done=1
    }


    override fun move(lastMove: Int): Int {
        sender.writeInt(lastMove)
        return reciver.readInt();
    }

    override fun is_beginning(b: Boolean) {
        is_beginning = b;
    }

    override fun setBoard(board: Board?) {

    }

    override fun has_won(): String {
        return "null"
    }
}