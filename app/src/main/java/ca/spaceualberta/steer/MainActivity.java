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
                    c.send(cv.getControlLeft(), cv.getControlRight(), cv.getControlWheelie());
                }
                handler.postDelayed(this, 100);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        cv.setActive(true);
        handler.post(r);
    }

    @Override
    protected void onStop() {
        super.onStop();
        cv.setActive(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        c.clean();
    }
}

