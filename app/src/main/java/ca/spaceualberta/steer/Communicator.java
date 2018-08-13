package ca.spaceualberta.steer;

import android.os.Handler;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

final class Communicator{
    WebSocket webSocket;
    private OkHttpClient client;
    boolean active;


    public Communicator(){
        active = false;
        client = new OkHttpClient.Builder().readTimeout(3,  TimeUnit.SECONDS).build();
        Request request = new Request.Builder().url(MainActivity.SERVER_URL).build();
        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onMessage(WebSocket webSocket, String text) {
                Log.d("websocket", "received: "+ text);
            }
        });


        final Handler handler = new Handler();
        Runnable r =  new Runnable() {
            @Override
            public void run() {
                active = true;
                Log.i("websocket", "CONNECTED TO SERVER AT: "+MainActivity.SERVER_URL);
            }
        };
        handler.postDelayed(r, 1000);



    }

    void clean(){
        active = false;
        webSocket.close(1000, "App is closing");
        client.dispatcher().executorService().shutdown();
        client.dispatcher().cancelAll();
    }


    public void send(float left, float right, float wheelie_amount){
        String msg = "{\"type\":\"drive\", \"left\": "
                +Math.max(Math.min(left, 1), -1)+", \"right\": "+Math.max(Math.min(right, 1), -1)
                + ", \"wheelie\": " + wheelie_amount
                +"}";
        Log.v(webSocket.request().url().host(), "loc: "+webSocket.request().url().port());
        Log.v("websocket", "sent: "+msg);
        webSocket.send(msg);
    }

    public boolean isActive() {
        return active;
    }
}
