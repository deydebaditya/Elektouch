package com.deba.elektouch;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by deba on 15/10/16.
 */

public class SpeechRecogActivity extends AppCompatActivity {

    TextView reply,replyhint;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elek);
        reply=(TextView)findViewById(R.id.resultText);
        replyhint=(TextView)findViewById(R.id.resultText_hint);
    }
    public void setButtonclick(View view) {
        if (view.getId() == R.id.speech)
        promptsearchinput();
    }
    public void promptsearchinput() {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.EXTRA_LANGUAGE_MODEL);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak My Lord!");
        try {
            startActivityForResult(i, 100);
        } catch (Exception e) {
            Toast.makeText(SpeechRecogActivity.this, "Sorry! Your device doesn't support this feature!", Toast.LENGTH_SHORT).show();
        }
    }
    public void onActivityResult(int request_code, int result_code, Intent i) {
        String input ;
        super.onActivityResult(request_code, result_code, i);
        switch (request_code) {
            case 100:
                if (result_code == RESULT_OK && i != null) {
                    ArrayList<String> string_result = i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    reply.setText(string_result.get(0));
                    input=string_result.get(0);
                    AI child= new AI(this,replyhint);
                    child.execute(input);
                }
                break;
        }
    }

}
