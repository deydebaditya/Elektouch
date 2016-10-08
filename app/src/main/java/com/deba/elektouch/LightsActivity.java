package com.deba.elektouch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;

/**
 * Created by deba on 8/10/16.
 */

public class LightsActivity extends AppCompatActivity {

    Switch lights,strobe;
    SeekBar regulator;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lights);
        lights=(Switch)findViewById(R.id.switch_lights);
        strobe=(Switch)findViewById(R.id.switch_strobe);
        regulator=(SeekBar)findViewById(R.id.regulator);
        Client client= (Client) getIntent().getSerializableExtra("client");
        //TODO: check for on/off
        lights.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Log.e("Lights","ON");
                    client.sendMessage("lights on");
                }
                else{
                    Log.e("Lights","OFF");
                    client.sendMessage("lights off");
                }
            }
        });

        //TODO: check for on/off
        strobe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("Strobe","ON");
            }
        });

        //TODO: send regulator callback values to server
        regulator.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.e("Regulator",String.valueOf(progress));
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
}
