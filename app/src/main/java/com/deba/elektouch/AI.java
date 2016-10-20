package com.deba.elektouch;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by root on 7/10/16.
 */

public class AI  extends AsyncTask<String,Void,String>{
    public Activity myactivity;
    AI_actual ai = new AI_actual();
    String input;
    TextView reply_hint;
    com.deba.elektouch.AI.Client client=new com.deba.elektouch.AI.Client("192.168.100.142");

    public AI(Activity myactivity,TextView reply) {
        super();
        this.myactivity=myactivity;
        this.reply_hint=reply;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        client.execute();
    }

    @Override
    protected String doInBackground(String... params) {
        String Pass_input=params[0];
        try {
           input= ai.main(Pass_input);
        } catch (Exception e) {
            e.printStackTrace();
        }
return input;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        TextView textView= (TextView) myactivity.findViewById(R.id.resultText);
        textView.setText(s);
        client.sendMessage(s.toLowerCase());
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
        public void connectToServer() {
            try {
                Log.e("Connection", "Attempting to connect!");
                connection = new Socket(InetAddress.getByName(serverIP), 6789);
                Log.e("Connection", "Connected to " + connection.getInetAddress().getHostName());
            }
            catch(IOException e){
            }
        }
        public void setupStreams()throws IOException{
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            Log.e("Connection","You are now setup!");
            reply_hint.setVisibility(View.INVISIBLE);
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
