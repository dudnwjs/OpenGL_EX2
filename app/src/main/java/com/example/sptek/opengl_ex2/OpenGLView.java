package com.example.sptek.opengl_ex2;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class OpenGLView extends GLSurfaceView {
    private OpenGLRenderer mRenderer;
    private Matrix matrix = new Matrix();
    Float scale = 1f;
    ScaleGestureDetector SGD;
    public OpenGLView(Context context) {
        super(context);
        init(context);
    }

    public OpenGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        setEGLContextClientVersion(2);
        setPreserveEGLContextOnPause(true);
        mRenderer = new OpenGLRenderer();
        setRenderer(mRenderer);
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        SGD = new ScaleGestureDetector(context, new ScaleListener());
    }

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;


    public void resetButtonClick(View v)
    {
        mRenderer.resetMove();
    }

    public void plusButtonClick(View v)
    {
        mRenderer.zoomIn();
    }

    public void minusButtonClick(View v)
    {
        mRenderer.zoomOut();
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            if(detector.getScaleFactor() > 1) {
                mRenderer.zoomIn();
            } else {
                mRenderer.zoomOut();
            }
            return true;
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        SGD.onTouchEvent(e);

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                // reverse direction of rotation above the mid-line
                if (y > getHeight() / 2) {
                    dx = dx * -1 ;
                }

                // reverse direction of rotation to left of the mid-line
                if (x < getWidth() / 2) {
                    dy = dy * -1 ;
                }

                mRenderer.setAngle(
                        mRenderer.getAngle() +
                                ((dx + dy) * TOUCH_SCALE_FACTOR));
                float[] move = {dx, -dy};
                mRenderer.setMove(move);
                requestRender();
            case MotionEvent.ACTION_UP:
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }
}
