package com.example.a07_facing;
/*
Hussein Alsowadi
Last Updated: 2/05/22
Basic compass app that uses phone sensors and text to speech when fab is pressed.
 */

import com.example.a07_facing.databinding.ActivityMainBinding;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import androidx.appcompat.app.AppCompatActivity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.util.Locale;
public class MainActivity extends AppCompatActivity implements SensorEventListener, TextToSpeech.OnInitListener {
    double azimut;  // View to draw a compass
    private TextToSpeech tts;   //texttospeech
    private ActivityMainBinding binding;
    private float currentDegree = 0f;
    private SensorManager mSensorManager;   //device sensor manager
    Sensor accelerometer;
    Sensor magnetometer;
    float degree;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        tts = new TextToSpeech(this, this);

        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // imageview click callback
        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                binding.textView.setText(R.string.Image_Click);
            }
        });
        //setting fab press
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speakOut();

            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
    }
    float[] mGravity;
    float[] mGeomagnetic;
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                azimut = orientation[0]; // orientation in radians
            }
        }
         degree= (float) (((Math.toDegrees(azimut) +360))%360);     // orientation in degrees(0-360)

        binding.textView.setText("Heading: " + Float.toString(degree ) + " degrees");   //setting text to current degrees read

        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation rotateimage = new RotateAnimation(
                currentDegree,-degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        rotateimage.setDuration(210);

        // set the animation after the end of the reservation status
        rotateimage.setFillAfter(true);

        // Start the animation
        binding.imageView.startAnimation(rotateimage);
        currentDegree = -degree;

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {  }

    // Overriding onCreateoptionMenu() to make Option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflating menu by overriding inflate() method of MenuInflater class.
        //Inflating here means parsing layout XML to views.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            } else {
            }

        } else {
        }
    }
    private void speakOut() {
        if (-currentDegree > 0 && -currentDegree < 30 || -currentDegree > 330 && -currentDegree < 350){

            String text = "You are facing North";
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
        else if (-currentDegree > 30 && -currentDegree <70){

            String text = "You are facing North East" ;
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null,null);
        }
        else if (-currentDegree > 70 && -currentDegree <120){

            String text = "You are facing  East" ;
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null,null);
        }
        else if (-currentDegree > 120 && -currentDegree <160){

            String text = "You are facing South East" ;
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null,null);
        }
        else if (-currentDegree > 160 && -currentDegree <205){

            String text = "You are facing South" ;
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null,null);
        }
        else if (-currentDegree > 205 && -currentDegree <255){

            String text = "You are facing South West" ;
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null,null);
        }
        else if (-currentDegree > 255 && -currentDegree <290){

            String text = "You are facing  West" ;
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null,null);
        }
        else if (-currentDegree > 290 && -currentDegree <330){

            String text = "You are facing  North West" ;
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null,null);
        }

    }
}