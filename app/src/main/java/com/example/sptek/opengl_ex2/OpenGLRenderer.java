package com.example.sptek.opengl_ex2;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGLRenderer implements GLSurfaceView.Renderer {
    private Triangle mTriangle;
    private Square   mSquare;
    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private float[] mRotationMatrix = new float[16];
    private  float moveZ = 0f;
    public void zoomIn()
    {
        moveZ = moveZ + 0.3f;
    }
    public void zoomOut()
    {
        moveZ = moveZ - 0.3f;
    }
    public volatile float mAngle;
    public float getAngle() {
        return mAngle;
    }

    public void setAngle(float angle) {
        mAngle = angle;
    }
    public volatile float mMove[] = {0f,0f};
    public void resetMove() {
        mMove[0] = 0f;
        mMove[1] = 0f;
        moveZ = 0f;
    }

    public void setMove(float[] move) {
        mMove[0] = mMove[0] + move[0]/100.0f;
        mMove[1] = mMove[1] + move[1]/100.0f;
    }
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1f, 1f, 1f,1f);
        mTriangle = new Triangle(1f,0f);
        mSquare = new Square(0f,-2.5f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 30);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        float[] scratch = new float[16];
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -15 + moveZ, mMove[0], mMove[1], 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        // Create a rotation transformation for the triangle
        long time = SystemClock.uptimeMillis() % 4000L;
        float angle = 0.090f * ((int) time);
        Matrix.setRotateM(mRotationMatrix, 0, angle, 0, 0, -1.0f);
        //Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, -1.0f);

        // Combine the rotation matrix with the projection and camera view
        // Note that the mMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

        mTriangle.draw(scratch);
        mSquare.draw(scratch);
    }

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
}
