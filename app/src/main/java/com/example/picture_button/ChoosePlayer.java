///// REMOVE AFTER DONE !!

/*
package com.example.picture_button;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
//import androidx.navigation.fragment.findNavController;

class ChoosePlayer extends AppCompatActivity {
    EditText e1, e2;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        e1 = (EditText) getView().findViewById(R.id.editText1);
        e2 = (EditText) getView().findViewById(R.id.editText2);

        Thread myThread = new Thread(new MyServer());
        myThread.start();
    }
    class MyServer implements Runnable
    {
        //listen for the inncoming messages
        ServerSocket ss;
        Socket mysocket;
        DataInputStream dis;
        String message;
        Handler handler = new Handler();
        @Override
        public void run(){
            try{
                ss= new ServerSocket(9700);
                handler.post(new Runnable() { // Operationen im Ui Thread
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Waiting for Client", Toast.LENGTH_SHORT).show();
                    }
                });
                while (true) //Problematisch Vielleicht
                {
                    mysocket= ss.accept();
                    dis = new DataInputStream(mysocket.getInputStream());

                    message = dis.readUTF();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"message received from client:"+message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    public  void button_click(View v){
        BackgroundTask b = new BackgroundTask();
        b.execute(e1.getText().toString(), e2.getText().toString());
    }
    class BackgroundTask extends AsyncTask<String,Void,String>{
        Socket s;
        DataOutputStream dos;
        String ip,message;

        @Override
        protected String doInBackground(String ... params){
            ip = params[0];
            message = params[1];
            try {
                s = new Socket(ip, 9700); //ip addreesse Port nummer
                dos = new DataOutputStream(s.getOutputStream());
                dos.writeUTF(message);
                dos.close();
                s.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }


            return null;
        }
    }
}*/