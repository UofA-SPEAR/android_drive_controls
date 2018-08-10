package ca.spaceualberta.steer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

final class ControlView extends View {
    int w, h;
    Paint paint;

    int left, right;
    int wheelie_amount;
    Rect r, l;
    Rect wheelie;

    boolean active;
    int touches;


    public ControlView(Context context) {
       super(context);
       paint = new Paint();
       r = new Rect();
       l = new Rect();
       wheelie = new Rect();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int num = event.getPointerCount();
        try{
        for (int a = 0; a < num; a++) {
            if (event.getPointerCount() > a) {
                int v = event.getPointerId(a);
                int x = (int) event.getX(v);
                int y = (int) event.getY(v);
                if (l.contains(x, y)) {
                    left = y - h / 2;
                }

                if (r.contains(x, y)) {
                    right = y - h / 2;
                }

                if (wheelie.contains(x, y)) {
                    wheelie_amount = y - h / 2;
                }


            }
        }
        }catch(IllegalArgumentException e){


        }


        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touches++;
                break;
            case MotionEvent.ACTION_UP:
                touches--;
                break;
        }
        if (touches <= 0){
            left = right = touches = 0;
        }

        invalidate();
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;
        l.set(w/4 - w/10,h/10, w/4 + w/10, 9*h/10);
        r.set(3*w/4 - w/10,h/10, 3*w/4 + w/10, 9*h/10);
        wheelie.set(w*4/10, h*2/10, w*6/10, h*8/10);

        setActive(true);

        super.onSizeChanged(w, h, oldw, oldh);
    }

   @Override
   protected void onDraw(Canvas canvas) {
       super.onDraw(canvas);

       // draw background
       paint.setStyle(Paint.Style.FILL);
       paint.setColor(Color.WHITE);
       canvas.drawPaint(paint);
       paint.setColor(Color.parseColor(active ? "#CD5C5C" : "#CCCCCC"));
       canvas.drawRect(0,0, w, h, paint);

       // draw triggers
       drawController(canvas, l, left);
       drawController(canvas, r, right);
       drawController(canvas, wheelie, wheelie_amount);

   }

   void drawController(Canvas canvas, Rect rect, int height){
        // draw bar
       paint.setStyle(Paint.Style.FILL);
       paint.setColor(Color.parseColor("#EEEEEE" ));
       canvas.drawRect(rect, paint);


       // draw nub
       paint.setStyle(Paint.Style.FILL);
       paint.setColor(Color.parseColor("#222222" ));
       canvas.drawRect(rect.left,h/2+height-h/20, rect.right, h/2+height+h/20, paint);

    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public float getControlWheelie() {
        return 2*(((float)wheelie_amount)/(wheelie.top-wheelie.bottom));
    }


    public float getControlLeft() {
        return 2*(((float)left)/(l.top-l.bottom));
    }

    public float getControlRight() {
        return 2*(((float)right)/(r.top-r.bottom));
    }

}
