package com.deba.elektouch;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.internal.http.multipart.MultipartEntity;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.entity.UrlEncodedFormEntityHC4;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by deba on 21/9/16.
 */
public class SignUpActivity extends AppCompatActivity {

    RelativeLayout signup_layout;
    EditText name,mob_number,uid,password,confirm_password,sec_code;
    Button signup;
    FileReader uidRead;
    String input=null,previousJson;
    int validationErrorName=0,validationErrorMob=0,validationErrorUID=0,validationErrorPass=0,validationErrorCode=0;
    int UID_FOUND_ON_SERVER=0;
    ProgressDialog progress;
    JSONObject newJson;
    ArrayList<String> issuedUids;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signup_layout=(RelativeLayout)findViewById(R.id.rel_layout_signup);
        name=(EditText)findViewById(R.id.name);
        mob_number=(EditText)findViewById(R.id.mob_number);
        uid=(EditText)findViewById(R.id.unique_id);
        password=(EditText)findViewById(R.id.password_signup);
        confirm_password=(EditText)findViewById(R.id.password_signup_confirm);
        sec_code=(EditText)findViewById(R.id.security_code);
        signup=(Button)findViewById(R.id.signupBut);

        DisplayMetrics metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height=(metrics.heightPixels)-getStatusBarHeight();
        int width=metrics.widthPixels;
        signup_layout.setMinimumWidth(width);
        signup_layout.setMinimumHeight(height);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().length() == 0) {
                    name.setError("NAME REQUIRED");
                    validationErrorName = 1;
                } else {
                    validationErrorName = 0;
                }
                if (mob_number.getText().toString().length() == 0) {
                    mob_number.setError("MOBILE NUMBER REQUIRED");
                    validationErrorMob = 1;
                } else {
                    if (mob_number.getText().toString().length() != 10 && mob_number.getText().toString().length() > 0) {
                        mob_number.setError("INVALID MOBILE NUMBER");
                        validationErrorMob = 1;
                    } else {
                        validationErrorMob = 0;
                    }
                }
                if (uid.getText().toString().length() == 0) {
                    uid.setError("UNIQUE ID REQUIRED");
                    validationErrorUID = 1;
                } else {
                    validationErrorUID = 0;
                }
                if (password.getText().toString().length() == 0) {
                    password.setError("SET A PASSWORD");
                    validationErrorPass = 1;
                } else {
                    validationErrorPass = 0;
                }
                if (confirm_password.getText().toString().length() == 0 && password.getText().toString().length() != 0) {
                    confirm_password.setError("CONFIRM YOUR PASSWORD");
                    validationErrorPass = 1;
                } else {
                    if (validationErrorPass != 1)
                        validationErrorPass = 0;
                }
                if (confirm_password.getText().toString().length() == 0 && password.getText().toString().length() == 0) {
                    validationErrorPass = 1;
                }
                if (confirm_password.getText().toString().length() != 0 && password.getText().toString().length() != 0) {
                    if (!confirm_password.getText().toString().equals(password.getText().toString())) {
                        Log.e("Passwords", confirm_password.getText().toString());
                        Log.e("Passwords", password.getText().toString());
                        confirm_password.setError("PASSWORDS DO NOT MATCH");
                        validationErrorPass = 1;
                    } else {
                        if (validationErrorPass != 1)
                            validationErrorPass = 0;
                    }
                    if (password.getText().toString().length() < 8) {
                        password.setError("PASSWORD MUST BE AT LEAST 8 CHARACTERS IN LENGTH");
                        validationErrorPass = 1;
                    } else {
                        if (validationErrorPass != 1)
                            validationErrorPass = 0;
                    }
                }
                if (sec_code.getText().toString().length() == 0) {
                    sec_code.setError("SECURITY CODE REQUIRED");
                    validationErrorCode = 1;
                } else {
                    validationErrorCode = 0;
                }
                new DownloadUids().execute();//Downlaod UIDS
                new SignUp().execute();//JSON formatting
//TODO:Push the new JSON to the server, using php post.

                if(true){Snackbar.make(v,"Signup Successful",Snackbar.LENGTH_SHORT).show();}
                else {
                    Snackbar.make(v,"Signup Unsuccessful",Snackbar.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void formatNewJson(){
        try {
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost request=new HttpPost("http://elektouch.16mb.com/json_put.php");
            List<NameValuePair> params=new ArrayList<NameValuePair>(5);
            params.add(new BasicNameValuePair("uid",uid.getText().toString()));
            params.add(new BasicNameValuePair("name",name.getText().toString()));
            params.add(new BasicNameValuePair("mob_no",mob_number.getText().toString()));
            params.add(new BasicNameValuePair("password",password.getText().toString()));
            params.add(new BasicNameValuePair("sec_code",sec_code.getText().toString()));
            request.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = httpClient.execute(request);
            } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (ClientProtocolException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void getUIDS(){
        if(validationErrorName==0&&validationErrorMob==0&&validationErrorPass==0&&validationErrorUID==0&&validationErrorCode==0){
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
    public int getStatusBarHeight(){
        int result=0;
        int resourceId=getResources().getIdentifier("status_bar_height","dimen","android");
        if(resourceId>0){
            result=getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    class SignUp extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] params) {
            if(UID_FOUND_ON_SERVER==1) {
                try {
                    URL members = new URL("http://elektouch.16mb.com/members.json");
                    BufferedReader in = new BufferedReader(new InputStreamReader(members.openStream()));
                    while ((input = in.readLine()) != null) {
                        Log.e("URL read", input);
                        previousJson = previousJson + input + "\n";
                    }
                    Log.e("JSON", "Connected!");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            formatNewJson();
            return previousJson;
        }

        @Override
        protected void onPostExecute(Object o) {
            progress.dismiss();
        }
    }
    class DownloadUids extends AsyncTask {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progress=new ProgressDialog(SignUpActivity.this);
            progress.setMessage("Signing Up!");
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
}
