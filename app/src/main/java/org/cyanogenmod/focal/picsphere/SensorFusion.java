package org.cyanogenmod.focal.picsphere;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorFusion implements SensorEventListener {
    private SensorManager mSensorManager = null;
    float[] mOrientation = new float[3];
    float[] mRotationMatrix;

    public SensorFusion(Context context) {
        // get sensorManager and initialise sensor listeners
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        initListeners();
    }

    public void onPauseOrStop() {
        mSensorManager.unregisterListener(this);
    }

    public void onResume() {
        // restore the sensor listeners when user resumes the application.
        initListeners();
    }

    // This function registers sensor listeners for the accelerometer, magnetometer and gyroscope.
    public void initListeners(){
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                20000);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (mRotationMatrix == null) {
            mRotationMatrix = new float[16];
        }

        // Get rotation matrix from angles
        SensorManager.getRotationMatrixFromVector(mRotationMatrix, event.values);

        // Remap the axes
        SensorManager.remapCoordinateSystem(mRotationMatrix, SensorManager.AXIS_MINUS_Z,
                SensorManager.AXIS_X, mRotationMatrix);

        // Get back a remapped orientation vector
        SensorManager.getOrientation(mRotationMatrix, mOrientation);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public float[] getFusedOrientation() {
        return mOrientation;
    }

    public float[] getRotationMatrix() {
        return mRotationMatrix;
    }
}
