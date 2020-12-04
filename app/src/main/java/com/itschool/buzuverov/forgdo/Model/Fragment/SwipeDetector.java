package com.itschool.buzuverov.forgdo.Model.Fragment;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public abstract class SwipeDetector implements View.OnTouchListener {

    public abstract void onSwipeDetected(Direction direction);

    public enum Direction {
        UNKNOWN,
        LEFT,
        RIGHT
    }

    private final GestureDetector gestureDetector;
    private static final int MIN_SWIPE_LEN = 150;

    public SwipeDetector(Context context) {
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                float dY = e2.getY() - e1.getY();
                float dX = e2.getX() - e1.getX();
                Direction direction = Direction.UNKNOWN;
                if (Math.abs(dX) > Math.abs(dY)) {
                    if (Math.abs(dX) > MIN_SWIPE_LEN) {
                        if (dX > 0) {
                            direction = Direction.LEFT;
                        } else {
                            direction = Direction.RIGHT;
                        }
                    }
                }
                onSwipeDetected(direction);
            } catch (Exception e){
                e.printStackTrace();
            }

            return true;
        }
    }
}

