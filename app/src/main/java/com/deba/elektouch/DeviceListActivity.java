package com.deba.elektouch;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;

/**
 * Created by deba on 4/10/16.
 */

public class DeviceListActivity extends AppCompatActivity {

    Button lights,fan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devicepage);
        lights=(Button)findViewById(R.id.button_lights);
        fan=(Button)findViewById(R.id.button_fan);

        Client client=new Client("192.168.13.225");
        client.startRunning();

        lights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lightsIntent=new Intent(DeviceListActivity.this,LightsActivity.class);
                lightsIntent.putExtra("client",client);
                startActivity(lightsIntent);
            }
        });
    }
}
