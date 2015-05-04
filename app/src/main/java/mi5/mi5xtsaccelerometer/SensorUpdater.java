package mi5.mi5xtsaccelerometer;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

/**
 * Created by ow on 30.04.15.
 */
public class SensorUpdater implements SensorEventListener {
    Context m_context;
    MainActivity m_activity;
    private float m_lastX, m_lastY, m_lastZ;
    private boolean m_initialized;
    private SensorManager m_sensorManager;
    private Sensor m_accelerometer;
    private OpcUaUpdater m_opcuaUpdater;

    // Filter out noise.
    private final float NOISE = (float) 0.01;

    public SensorUpdater(Context context, MainActivity activity)
    {
        this.m_context = context;
        this.m_activity = activity;
        m_initialized = false;
        m_sensorManager = (SensorManager) m_context.getSystemService(Context.SENSOR_SERVICE);
        m_accelerometer = m_sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        m_sensorManager.registerListener(this, m_accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        m_opcuaUpdater = new OpcUaUpdater("opc.tcp://192.168.192.52:48010");
        m_opcuaUpdater.execute(new String[] {});
    }

    public void notifyResume()
    {
        m_sensorManager.registerListener(this, m_accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }
    public void notifyPaused()
    {
        m_sensorManager.unregisterListener(this);

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // ..
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        if (!m_initialized) {
            m_lastX = x;
            m_lastY = y;
            m_lastZ = z;
            m_initialized = true;
            x = (float) 0.0;
            y = (float) 0.0;
            z = (float) 0.0;
        }
        else
        {
            float deltaX = Math.abs(m_lastX - x);
            float deltaY = Math.abs(m_lastY - y);
            float deltaZ = Math.abs(m_lastZ - z);

            if (deltaX < NOISE)
                x = (float) 0.0;
            if (deltaY < NOISE)
                y = (float) 0.0;
            if (deltaZ < NOISE)
                z = (float) 0.0;
        }
        m_activity.updateDisplay(x,y,z);
        m_opcuaUpdater.updateValues(x,y,z);
    }
}
