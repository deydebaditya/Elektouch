package com.deba.elektouch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView signup,reset_password;
    Button login;
    EditText uid,password;
    RelativeLayout loginLayout;
    int UID_FOUND_ON_SERVER=0;
    String previousJson=null,input;
    ProgressDialog progress;
    ArrayList<String> issuedUids;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        signup=(TextView)findViewById(R.id.signup);
        reset_password=(TextView)findViewById(R.id.password_reset);
        login=(Button)findViewById(R.id.loginBut);
        uid=(EditText)findViewById(R.id.uidInput);
        password=(EditText)findViewById(R.id.passwordInput);
        loginLayout=(RelativeLayout)findViewById(R.id.loginLayout);

        DisplayMetrics metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height=(metrics.heightPixels)-getStatusBarHeight();
        int width=metrics.widthPixels;
        loginLayout.setMinimumWidth(width);
        loginLayout.setMinimumHeight(height);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uid.getText().toString().length()==0){
                    uid.setError("UNIQUE ID REQUIRED");
                    if(password.getText().toString().length()==0){
                        password.setError("PASSWORD IS REQUIRED");
                    }
                }
                else if(password.getText().toString().length()==0){
                    password.setError("PASSWORD IS REQUIRED");
                    if(uid.getText().toString().length()==0){
                        uid.setError("UNIQUE ID REQUIRED");
                    }
                }
                else{
                    new DownloadUids().execute();
                    new downloadJSON().execute();
//                    if(UID_FOUND_ON_SERVER==1)
//                    }
//                    else{
//                        Snackbar.make(v,"UID not registered!",Snackbar.LENGTH_SHORT).show();
//                    }
                    Toast.makeText(getApplicationContext(),"Logged In Successfully!",Toast.LENGTH_SHORT).show();
                    //TODO:If validation is completely OK, rest of the login code here.
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Links", "Signup Clicked");
//                Snackbar.make(v,"Signup",Snackbar.LENGTH_SHORT).show();
                Intent signupintent=new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(signupintent);
            }
        });
        reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Links", "Reset Clicked");
//                Snackbar.make(v,"Reset Password",Snackbar.LENGTH_SHORT).show();
                Intent resetPasswordIntent=new Intent(MainActivity.this,ResetPasswordActivity.class);
                startActivity(resetPasswordIntent);
            }
        });
    }
    private void logMeIn(){
        try {
            Log.e("UI Thread:",previousJson);
            JSONObject loginArray = new JSONObject(previousJson);
            Log.e("User", String.valueOf(loginArray.getJSONObject(uid.getText().toString()).get("password")));
//            for (int i = 0; i < loginArray.length(); i++) {
                if (loginArray.getJSONObject(uid.getText().toString()).get("password").equals(password.getText().toString())) {
                        Intent deviceListintent=new Intent(MainActivity.this,DeviceListActivity.class);
                        startActivity(deviceListintent);
                    }
//                }
            } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }
    public int getStatusBarHeight(){
        int result=0;
        int resourceId=getResources().getIdentifier("status_bar_height","dimen","android");
        if(resourceId>0){
            result=getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    class DownloadUids extends AsyncTask {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progress=new ProgressDialog(MainActivity.this);
            progress.setMessage("Logging In!");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }
        @Override
        protected Object doInBackground(Object[] params) {
            issuedUids=new ArrayList<String>();
            URL issue_uid;
            try {
                issue_uid=new URL("http://elektouch.16mb.com/issued_uids.dat");
                BufferedReader br=new BufferedReader(new InputStreamReader(issue_uid.openStream()));
                String uid_get;
                while((uid_get=br.readLine())!=null){
                    issuedUids.add(uid_get);
                    Log.e("UID",uid_get);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return issuedUids;
        }
        @Override
        protected void onPostExecute(Object list){
            getUIDS();
        }
    }
    private void getUIDS(){
        if(true){
            URL issue_uid;
            Log.e("UID_FOUND",String.valueOf(UID_FOUND_ON_SERVER));
            int i=0;
            while(i<issuedUids.size()){
                if(uid.getText().toString().equals(issuedUids.get(i))){
                    UID_FOUND_ON_SERVER=1;
                }
                i++;
            }
            Log.e("UID_FOUND_AFTER",String.valueOf(UID_FOUND_ON_SERVER));
        }
    }
    class downloadJSON extends AsyncTask{

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
