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
import android.content.SharedPreferences;

/**
 * Created by deba on 8/10/16.
 */

public class LightsActivity extends AppCompatActivity {

    Switch lights,strobe;
    SeekBar regulator;
    String regulate;
    SharedPreferences lightState;
    SharedPreferences.Editor editState;
    //    Bundle getClient;
    com.deba.elektouch.LightsActivity.Client client=new com.deba.elektouch.LightsActivity.Client("192.168.13.35");
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getClient=getIntent().getBundleExtra("client");
//        client=(Client)getClient.getSerializable("client");
        setContentView(R.layout.activity_lights);
        lights=(Switch)findViewById(R.id.switch_lights);
        strobe=(Switch)findViewById(R.id.switch_strobe);
        regulator=(SeekBar)findViewById(R.id.regulator);
        lightState=getSharedPreferences("lights",0);
        editState=lightState.edit();
        if(lightState.getString("state",null)==null||lightState.getString("state",null).equals("off")){
            editState.putString("state","off");
        }
        else if(lightState.getString("state",null).equals("on")){
            lights.setChecked(true);
        }
        client.execute();
        //TODO: check for on/off
        lights.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    Log.e("Lights","ON");
                    editState.putString("state","on");
                    client.sendMessage("lights on");
                }
                else{
                    Log.e("Lights","OFF");
                    editState.putString("state","off");
                    client.sendMessage("lights off");
                }
            }
        });

        //TODO: check for on/off
        strobe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("Strobe","ON");
                if(buttonView.isChecked()){
                    client.sendMessage("strobe");
                }
                else{
                    client.sendMessage("strobe off");
                }
            }
        });

        //TODO: send regulator callback values to server
        regulator.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.e("Regulator",String.valueOf(progress));
                if(lights.isChecked()) {
                    if(Integer.parseInt(String.valueOf(progress))>=10&&Integer.parseInt(String.valueOf(progress))<=99)
                        regulate = "regulatelights" + String.valueOf(progress);
                    else if(Integer.parseInt(String.valueOf(progress))<=9)
                        regulate = "regulatelights0" + String.valueOf(progress);
                    else if(Integer.parseInt(String.valueOf(progress))==100)
                        regulate = "regulatelights99";
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
        editState.commit();
    }
///////////////////INNER CLASS IF OUTER DOESNT WORK/////////////////////////
    class Client extends AsyncTask implements Serializable{

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
