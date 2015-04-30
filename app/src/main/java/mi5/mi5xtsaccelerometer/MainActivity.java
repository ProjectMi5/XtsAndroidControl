package mi5.mi5xtsaccelerometer;

import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
    SensorUpdater m_sensorUpdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m_sensorUpdater = new SensorUpdater(getApplicationContext(), MainActivity.this);
    }

    protected void onResume()
    {
        super.onResume();
        m_sensorUpdater.notifyResume();
    }

    protected void onPause() {

        super.onPause();
        m_sensorUpdater.notifyPaused();

    }

    public void updateDisplay(float x, float y, float z)
    {
        TextView tvX = (TextView) findViewById(R.id.textViewValueX);
        TextView tvY = (TextView) findViewById(R.id.textViewValueY);
        TextView tvZ = (TextView) findViewById(R.id.textViewValueZ);

        tvX.setText(Float.toString(x));
        tvY.setText(Float.toString(y));
        tvZ.setText(Float.toString(z));
    }
}
