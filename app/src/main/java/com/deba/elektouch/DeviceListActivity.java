package com.deba.elektouch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.io.Serializable;

/**
 * Created by deba on 4/10/16.
 */

public class DeviceListActivity extends AppCompatActivity implements Serializable{

    Button lights,fan,elek,signout;
    RelativeLayout deviceLayout;
//    Client client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devicepage);
        lights=(Button)findViewById(R.id.button_lights);
        fan=(Button)findViewById(R.id.button_fan);
        elek=(Button)findViewById(R.id.button_ai);
        deviceLayout=(RelativeLayout)findViewById(R.id.devicepageLayout);
        signout=(Button)findViewById(R.id.button_signout);
        DisplayMetrics metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height=(metrics.heightPixels)-getStatusBarHeight();
        int width=metrics.widthPixels;
        deviceLayout.setMinimumWidth(width);
        deviceLayout.setMinimumHeight(height);
        SharedPreferences settings=getSharedPreferences("login",0);
        //Start connection to server
//        client=new Client("192.168.13.165");

        lights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bundle sendObj=new Bundle();
//                Serializable clientObj=client;
//                sendObj.putSerializable("client",clientObj);
                Intent lightsIntent=new Intent(DeviceListActivity.this,LightsActivity.class);
//                lightsIntent.putExtra("client",sendObj);
                startActivity(lightsIntent);
            }
        });
        fan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fansIntent=new Intent(DeviceListActivity.this,FansActivity.class);
                startActivity(fansIntent);
            }
        });
        elek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AI=new Intent(DeviceListActivity.this,SpeechRecogActivity.class);
                startActivity(AI);
            }
        });
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor clear=settings.edit();
                clear.putString("uid",null);
                clear.putString("password",null);
                clear.putString("name",null);
                clear.commit();
                Intent mainActivity=new Intent(DeviceListActivity.this,MainActivity.class);
                startActivity(mainActivity);
                finish();
            }
        });
    }
    public int getStatusBarHeight(){
        int result=0;
        int resourceId=getResources().getIdentifier("status_bar_height","dimen","android");
        if(resourceId>0){
            result=getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
