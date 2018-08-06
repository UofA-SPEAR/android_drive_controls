package ca.spaceualberta.steer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MainActivity extends AppCompatActivity {
    public static final String SERVER_URL = "ws://192.168.0.61:9090/websocket";

    Handler handler;
    Communicator c;
    ControlView cv;

    boolean done = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cv = new ControlView(this);
        c = new Communicator();

        setContentView(cv);


        handler = new Handler();
        Runnable r =  new Runnable() {
            @Override
            public void run() {
                if(cv.isActive() && c.isActive()) {
                    c.send(cv.getControlLeft(), cv.getControlRight());
                }
                if(!done) {
                    handler.postDelayed(this, 16);
                }
            }
        };
        handler.post(r);
    }

    @Override
    protected void onStop() {
        super.onStop();
        done = true;
    }
}

