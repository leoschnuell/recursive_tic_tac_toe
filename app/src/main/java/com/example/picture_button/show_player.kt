package com.example.picture_button

import android.content.Intent
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import android.os.AsyncTask
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.ServerSocket
import java.net.Socket

class show_player : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_show_player, container, false)
        val e1 = view.findViewById<EditText>(R.id.editText1)
        val e2 = view.findViewById<EditText>(R.id.editText2)
        val button = view.findViewById<Button>(R.id.button_send)
        button.setOnClickListener {
            //val b = BackgroundTask()
           // b.execute(e1!!.text.toString(), e2!!.text.toString())
            findNavController().navigate(R.id.action_player_vs_player_to_board)
        }

            return view
    }
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        val myThread = Thread(MyServer()) // bringt die App zum Abstürzen ist das Toast dran schuld
        myThread.start()
    }
    internal inner class MyServer : Runnable {
        //listen for the inncoming messages
        var ss: ServerSocket? = null
        var mysocket: Socket? = null
        var dis: DataInputStream? = null
        var message: String? = null
        var handler = Handler()
        override fun run() {
            try {
                ss = ServerSocket(9700)
                handler.post {
                    /*Toast.makeText(
                        AppCompatActivity(),
                        "Waiting for Client",
                        Toast.LENGTH_SHORT
                    ).show()*/
                }
                while (true) {
                    mysocket = ss!!.accept()
                    dis = DataInputStream(mysocket!!.getInputStream())
                    message = dis!!.readUTF()
                    handler.post {
                        /*Toast.makeText(
                            AppCompatActivity(),
                            "message received from client:$message",
                            Toast.LENGTH_SHORT
                        ).show()*/
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private class BackgroungTask : AsyncTask<String, Void, String>() { //Problematisch
        var s: Socket? = null
        var dos: DataOutputStream? = null
        var ip: String? = null
        var message: String? = null
        override fun doInBackground(vararg params: String): String? {
            ip = params[0]
            message = params[1]
            try {
                s = Socket(ip, 9700) //ip addreesse Port nummer
                dos = DataOutputStream(s!!.getOutputStream())
                dos!!.writeUTF(message)
                dos!!.close()
                s!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }
}
}