package com.deba.elektouch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.RelativeLayout;

/**
 * Created by deba on 23/9/16.
 */
public class ResetPasswordActivity extends AppCompatActivity {

    RelativeLayout resetPasswordLayout;

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
}
