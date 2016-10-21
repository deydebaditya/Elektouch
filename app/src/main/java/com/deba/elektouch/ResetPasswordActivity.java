package com.deba.elektouch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.EditText;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by deba on 23/9/16.
 */
public class ResetPasswordActivity extends AppCompatActivity {

    RelativeLayout resetPasswordLayout;
    String previousJson,input;
    ProgressDialog progress;
    EditText uid,sec_code,password,confirm_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);
        resetPasswordLayout=(RelativeLayout)findViewById(R.id.rel_layout_resetpass);

        DisplayMetrics metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height=(metrics.heightPixels)-getStatusBarHeight();
        int width=metrics.widthPixels;
        resetPasswordLayout.setMinimumWidth(width);
        resetPasswordLayout.setMinimumHeight(height);
    }

    public int getStatusBarHeight(){
        int result=0;
        int resourceId=getResources().getIdentifier("status_bar_height","dimen","android");
        if(resourceId>0){
            result=getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void logMeIn(){
        try {
            Log.e("UI Reset Thread:",previousJson);
            JSONObject loginArray = new JSONObject(previousJson);
            Log.e("User", String.valueOf(loginArray.getJSONObject(uid.getText().toString()).get("password")));//TODO:get security code
//            for (int i = 0; i < loginArray.length(); i++) {
            if (loginArray.getJSONObject(uid.getText().toString()).get("password").equals(password.getText().toString())) {
                Intent deviceListintent=new Intent(ResetPasswordActivity.this,MainActivity.class);
                startActivity(deviceListintent);
            }
//                }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }

    class downloadJSON extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            previousJson = "";
            try {
                URL members = new URL("http://elektouch.16mb.com/members.json");
                BufferedReader in = new BufferedReader(new InputStreamReader(members.openStream()));
                while ((input = in.readLine()) != null) {
                    Log.e("URL read", input);
                    previousJson = previousJson + input + "\n";
                }
                Log.e("JSON", "Connected!");
                logMeIn();//Logging in
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return previousJson;
        }
        @Override
        protected void onPostExecute(Object list){
            progress.dismiss();
        }
    }
}
