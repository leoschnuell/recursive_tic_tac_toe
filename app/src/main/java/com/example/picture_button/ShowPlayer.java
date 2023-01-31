package com.example.picture_button;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ShowPlayer extends Fragment {
    EditText ip, message;
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
        ip = (EditText) view.findViewById(R.id.hostName);
        message = (EditText) view.findViewById(R.id.hostName);
        send_btn = (Button) view.findViewById(R.id.search_game);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackgroundTask b = new BackgroundTask();
                b.execute(ip.getText().toString(), message.getText().toString());
                Bundle bundle = new Bundle();
                bundle.putSerializable("player1", Board.playerType.HUMAN);
                bundle.putSerializable("player2", Board.playerType.REMOTE);

            }
        });
        Thread myThread = new Thread(new MyServer());
        myThread.start();
        return view;
    }

    class MyServer implements Runnable {
        //listen for the inncoming messages
        ServerSocket ss;
        Socket mySocket;
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
                    mySocket = ss.accept();
                    dis = new DataInputStream(mySocket.getInputStream());

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

    class BackgroundTask extends AsyncTask<String, Void, String> {
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
    }
}
