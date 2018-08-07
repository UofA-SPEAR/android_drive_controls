package ca.spaceualberta.steer;


import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {
    public static final String SERVER_URL = "ws://192.168.0.61:9090/websocket";

    Communicator c;
    ControlView cv;

    Handler handler;
    Runnable r;

    boolean done = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cv = new ControlView(this);
        c = new Communicator();

        setContentView(cv);
        
        handler = new Handler();
        r =  new Runnable() {
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        done = false;
        handler.post(r);
    }

    @Override
    protected void onStop() {
        super.onStop();
        for(int i = 0; i < 100; i++){
            c.send(0,0);
        }
        done = true;
    }
}

