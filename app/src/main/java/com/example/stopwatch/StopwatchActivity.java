package com.example.stopwatch;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

public class StopwatchActivity extends Activity {
    //number of seconds which will be displayed in the Stopwatch UI
    private int seconds = 0;
    //to control and indicate whether the stopwatch is running or not.
    private boolean running;
    //to control and indicate whether the stopwatch was running or not.
    private boolean wasRunning;

    //as suggested by the professor
    private static final int SECS_PER_MIN = 60;
    private static final int MINS_PER_HOUR = 60;
    private static final int SECS_PER_HOUR = SECS_PER_MIN * MINS_PER_HOUR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);
        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
            //log data on Logcat
            Log.d(this.getLocalClassName(), "onCreate");
        }
        runTimer();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        savedInstanceState.putInt("seconds", seconds);
        savedInstanceState.putBoolean("running", running);
        savedInstanceState.putBoolean("wasRunning", wasRunning);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        wasRunning = running;
        running = false;
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if (wasRunning) {
            running = true;
        }
    }

//includes Toast
    @Override
    protected void onPause(){
        super.onPause();
        wasRunning = running;
        running = false;
        Toast.makeText(this, "Paused", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(wasRunning){
            running = true;
            Toast.makeText(this, "stopped", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickStart(View view) {
        running = true;
    }

    public void onClickStop(View view) {
        running = false;
    }

    public void onClickReset(View view) {
        running = false;
        seconds = 0;
    }

    //modified to output time via logcat
    private void runTimer() {
        final TextView timeView = (TextView)findViewById(R.id.time_view);
        final Handler handler = new Handler();

        handler.post(new Runnable() {

            @Override
            public void run() {
                int hours = seconds / SECS_PER_HOUR;
                int minutes = (seconds % SECS_PER_HOUR) / MINS_PER_HOUR;
                int secs = seconds % SECS_PER_MIN;
                String time = String.format("%d:%02d:%02d", hours, minutes, secs);
                timeView.setText(time);
                if (running) {
                    seconds++;
                    //This line will output the time information into Logcat, supposedly in
                    //intervals of 1000 milliseconds as that’s when each time run() is called.
                    Log.d("seconds", "run() method is running: time: " + time);
                }
                handler.postDelayed(this, 1000);
            }
        });
    }
}