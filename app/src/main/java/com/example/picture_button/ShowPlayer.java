package com.example.picture_button;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class ShowPlayer extends Fragment {
    EditText ip,message;
    Button send_btn;
    public ShowPlayer() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_player2, container, false);
        ip = (EditText) view.findViewById(R.id.editText1);
        message = (EditText) view.findViewById(R.id.editText2);
        send_btn = (Button) view.findViewById(R.id.button_send);
        send_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                System.out.println("heul");
            }
        });
        return view;
    }

    /*@Override
    public void onStart() {
        super.onStart();
        Socket socket = null;
        try {
            ServerSocket serverSocket = new ServerSocket(6868);//java.net class that provides a system-independent implementation of the server side of a client/server socket connection
            socket = serverSocket.accept();
            InputStream input = socket.getInputStream();//an abstract class of Byte Stream that describe stream input and it is used for reading and it could be a file, image, audio, video, webpage
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));// Java class to reads the text from an Input stream (like a file) by buffering characters that seamlessly reads characters, arrays or lines.
            String line = reader.readLine();
            System.out.println(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*////

    class BackgroundTask extends AsyncTask<String,Void,String> {
        Socket s;
        DataOutputStream dos;
        String ip, message;

        @Override
        protected String doInBackground(String... params) {
            ip = params[0];
            message = params[1];
            try {
                s = new Socket(ip, 9700); //ip addreesse Port nummer
                dos = new DataOutputStream(s.getOutputStream());
                dos.writeUTF(message);
                dos.close();
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        class MyServer implements Runnable {
            //listen for the inncoming messages
            ServerSocket ss;
            Socket mysocket;
            DataInputStream dis;
            String message;
            Handler handler = new Handler();

            @Override
            public void run() {
                try {
                    ss = new ServerSocket(9700);
                    handler.post(new Runnable() { // Operationen im Ui Thread
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Waiting for Client", Toast.LENGTH_SHORT).show();
                        }
                    });
                    while (true) //Problematisch Vielleicht
                    {
                        mysocket = ss.accept();
                        dis = new DataInputStream(mysocket.getInputStream());

                        message = dis.readUTF();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "message received from client:" + message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
