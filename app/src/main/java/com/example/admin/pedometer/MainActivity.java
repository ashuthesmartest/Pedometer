package com.example.admin.pedometer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.text.DecimalFormat;


public class MainActivity extends Activity implements SensorEventListener
{

    private TextView textView , textView1 ;
    private SensorManager mSensorManager ;
    private Sensor mStepCounterSensor ;
    private Sensor mStepDetectorSensor ;
    public int ctr = 1 ;
    public int value = -1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textview) ;
        textView1 = (TextView) findViewById(R.id.textView1) ;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE) ;
        mStepCounterSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) ;
        mStepDetectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) ;
        textView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String s ;

                switch(ctr)
                {
                    case 1 : DecimalFormat df = new DecimalFormat("#.00") ;
                             s = String.valueOf(df.format(value * 0.000762)) + "\nkms" ;
                             SpannableString ss1 = new SpannableString(s) ;
                             ss1.setSpan(new RelativeSizeSpan(1.60f), 0, 5, 0);
                             textView.setText(ss1);
                             ctr++ ;
                             break ;
                    case 2 : s = value + "\n Steps " ;
                             ss1 = new SpannableString(s) ;
                             ss1.setSpan(new RelativeSizeSpan(1.60f), 0, 5, 0);
                             textView.setText(ss1);
                             ctr-- ;
                             break ;
                    default : // do nothing
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        Sensor sensor = event.sensor ;
        float[] values = event.values ;

        if(values.length > 0)
        {
            value = (int) values[0] ;
        }

        if(sensor.getType() == Sensor.TYPE_STEP_COUNTER)
        {
            String s = value + "\n Steps " ;
            SpannableString ss1 = new SpannableString(s) ;
            ss1.setSpan(new RelativeSizeSpan(1.60f),0,5,0);
            textView.setText(ss1);
            textView.setGravity(Gravity.CENTER);
            textView1.setGravity(Gravity.CENTER);
            textView1.setText(String.valueOf((int)(value * 0.045)) + "\ncalories" + "\nburnt");
        }
        else if(sensor.getType() == Sensor.TYPE_STEP_DETECTOR)
        {
            textView.setText(value + " Steps" );
        }
    }

    protected void onResume()
    {
        super.onResume();
        mSensorManager.registerListener(this, mStepCounterSensor, SensorManager.SENSOR_DELAY_FASTEST) ;
        mSensorManager.registerListener(this, mStepDetectorSensor, SensorManager.SENSOR_DELAY_FASTEST) ;
    }

    protected void onStop()
    {
        super.onStop();
        mSensorManager.unregisterListener(this, mStepCounterSensor);
        mSensorManager.unregisterListener(this, mStepDetectorSensor);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
