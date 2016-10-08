package com.deba.elektouch;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author Deba
 */
public class Client extends AsyncTask implements Serializable{

    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String serverIP;
    private Socket connection;
    public String messageToSend="";
    ProgressDialog progress;

    public Client(String host){
        serverIP=host;
    }
    @Override
    protected void onPreExecute(){
        try{
            progress.setMessage("Connecting to server!");
            progress.setCancelable(false);
            progress.show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public void connectToServer()throws IOException{
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
        progress.dismiss();
    }
}