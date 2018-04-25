package com.example.sptek.opengl_ex2;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGLRenderer implements GLSurfaceView.Renderer {
    private Triangle mTriangle;
    private Square[]  mSquare = new Square[30];
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
    volatile float mAngle;
    public float getAngle() {
        return mAngle;
    }

    public void setAngle(float angle) {
        mAngle = angle;
    }
    private volatile float mMove[] = {0f,0f};
    public void resetMove() {
        mMove[0] = 0f;
        mMove[1] = 0f;
        moveZ = 0f;
    }

    public void setMove(float[] move) {
        mMove[0] = mMove[0] + move[0]/100.0f;
        mMove[1] = mMove[1] + move[1]/100.0f;
    }

    private float[][] structure_pos = {
            { -10.5f, 15.5f, 2.0f, 10.0f },
            { -10.5f, 3.0f, 2.0f, 10.0f },
            { 8.5f, 15.5f, 2.0f, 10.0f },
            { 8.5f, 3.0f, 2.0f, 10.0f },
            { -0.2f, 14.5f, 0.4f, 9.0f },
            { -0.2f, 3.0f, 0.4f, 9.0f },
            {-8.5f, 10.0f, 3.0f, 0.4f},
            {6.0f, 10.0f, 3.0f, 0.4f},
            {-2.5f, 10.0f, 5.0f, 0.4f},
    };
    private Square[]  mStructureSquare = new Square[structure_pos.length];
    private float[][] parking_pos = {
            { -8.4f, 14.5f, 2.0f, 1.4f },
            { -2.1f, 14.5f, 2.0f, 1.4f },
            { 0.1f, 14.5f, 2.0f, 1.4f },
            { 6.4f, 14.5f, 2.0f, 1.4f },

            { -8.4f, 13.0f, 2.0f, 1.4f },
            { -2.1f, 13.0f, 2.0f, 1.4f },
            { 0.1f, 13.0f, 2.0f, 1.4f },
            { 6.4f, 13.0f, 2.0f, 1.4f },

            { -8.4f, 11.5f, 2.0f, 1.4f },
            { -2.1f, 11.5f, 2.0f, 1.4f },
            { 0.1f, 11.5f, 2.0f, 1.4f },
            { 6.4f, 11.5f, 2.0f, 1.4f },

            { -8.4f, 9.5f, 2.0f, 1.4f },
            { -2.1f, 9.5f, 2.0f, 1.4f },
            { 0.1f, 9.5f, 2.0f, 1.4f },
            { 6.4f, 9.5f, 2.0f, 1.4f },

            { -8.4f, 8.0f, 2.0f, 1.4f },
            { -2.1f, 8.0f, 2.0f, 1.4f },
            { 0.1f, 8.0f, 2.0f, 1.4f },
            { 6.4f, 8.0f, 2.0f, 1.4f },

            { -8.4f, 3.0f, 2.0f, 1.4f },
            { -2.1f, 3.0f, 2.0f, 1.4f },
            { 0.1f, 3.0f, 2.0f, 1.4f },
            { 6.4f, 3.0f, 2.0f, 1.4f },

            { -8.4f, 1.5f, 2.0f, 1.4f },
            { -2.1f, 1.5f, 2.0f, 1.4f },
            { 0.1f, 1.5f, 2.0f, 1.4f },
            { 6.4f, 1.5f, 2.0f, 1.4f },

            { -8.4f, 0.0f, 2.0f, 1.4f },
            { -2.1f, 0.0f, 2.0f, 1.4f },
            { 0.1f, 0.0f, 2.0f, 1.4f },
            { 6.4f, 0.0f, 2.0f, 1.4f },

            { -8.4f, -1.5f, 2.0f, 1.4f },
            { -2.1f, -1.5f, 2.0f, 1.4f },
            { 0.1f, -1.5f, 2.0f, 1.4f },
            { 6.4f, -1.5f, 2.0f, 1.4f },

            { -8.4f, -3.0f, 2.0f, 1.4f },
            { -2.1f, -3.0f, 2.0f, 1.4f },
            { 0.1f, -3.0f, 2.0f, 1.4f },
            { 6.4f, -3.0f, 2.0f, 1.4f },

            { -8.4f, -4.5f, 2.0f, 1.4f },
            { -2.1f, -4.5f, 2.0f, 1.4f },
            { 0.1f, -4.5f, 2.0f, 1.4f },
            { 6.4f, -4.5f, 2.0f, 1.4f },
    };
    private Square[]  mParkingSquare = new Square[parking_pos.length];
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1f, 1f, 1f,1f);
        //mTriangle = new Triangle(1f,0f);
        for(int i = 0;i < parking_pos.length; i++) {
            mParkingSquare[i] =  new Square(parking_pos[i], "parking");
        }
        for(int i = 0;i < structure_pos.length; i++) {
            mStructureSquare[i] =  new Square(structure_pos[i], "structure");
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 100);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        float[] scratch = new float[16];
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0,  mMove[0], mMove[1], 50 - moveZ, mMove[0], mMove[1], 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        // Create a rotation transformation for the triangle
        Matrix.setRotateM(mRotationMatrix, 0, 0, 0, 0, -1.0f);
        //Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, -1.0f);

        // Combine the rotation matrix with the projection and camera view
        // Note that the mMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

        //mTriangle.draw(scratch);
        for(int i = 0;i < parking_pos.length; i++) {
            mParkingSquare[i].draw(scratch);
        }
        for(int i = 0;i < structure_pos.length; i++) {
            mStructureSquare[i].draw(scratch);
        }
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
