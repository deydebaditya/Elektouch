package com.deba.elektouch;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by deba on 14/10/16.
 */

public class FansActivity extends AppCompatActivity {

    Switch fans;
    SeekBar regulator;
    String regulate;
    com.deba.elektouch.FansActivity.Client client=new com.deba.elektouch.FansActivity.Client("192.168.100.142");
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fan);
        fans=(Switch)findViewById(R.id.switch_fans);
        regulator=(SeekBar)findViewById(R.id.regulator_fan);
        client.execute();

        fans.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    Log.e("Lights","ON");
                    client.sendMessage("fans on");
                }
                else{
                    Log.e("Lights","OFF");
                    client.sendMessage("fans off");//
                }
            }
        });

        regulator.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.e("Regulator",String.valueOf(progress));
                if(fans.isChecked()) {
                    regulate = "regulatefans" + String.valueOf(progress);
                    client.sendMessage(regulate);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.e("Regulator","Hands down");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e("Regulator","Hands up");
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        client.sendMessage("close");
    }
    class Client extends AsyncTask implements Serializable {

        private ObjectOutputStream output;
        private ObjectInputStream input;
        private String serverIP;
        private Socket connection;
        public String messageToSend="";

        public Client(String host){
            serverIP=host;
        }
        @Override
        protected void onPreExecute(){
            try{
                super.onPreExecute();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        public void connectToServer()throws IOException {
            Log.e("Connection","Attempting to connect!");
            connection=new Socket(InetAddress.getByName(serverIP),6789);
            Log.e("Connection","Connected to "+connection.getInetAddress().getHostName());
        }
        public void setupStreams()throws IOException{
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            Log.e("Connection","You are now setup!");
        }
        private void whileRunning()throws IOException{

        }
        public void sendMessage(String message){
            try{
                output.writeObject(message);
                output.flush();
            }catch(Exception e5){
                e5.printStackTrace();
            }
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                connectToServer();//Connection
                setupStreams();
                whileRunning();
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object list){
        }
    }
}
