package com.example.kitcoop.lastproject1.pedometerService;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import com.example.kitcoop.lastproject1.DATA.SharedConstant;
import com.example.kitcoop.lastproject1.DATA.SharedObjct;
import com.example.kitcoop.lastproject1.DB.DBHelper;


public class MyService extends Service implements SensorEventListener, StepListener, SharedConstant, SharedObjct {
    private SimpleStepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private static int numSteps;
    private static int calorie;
    private DBHelper dbHelper;

    public MyService() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i("MyService", "onStartCommand호출");

        if(sensorManager == null) {
            sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            simpleStepDetector = new SimpleStepDetector();
            simpleStepDetector.registerListener(MyService.this);

            sensorManager.registerListener(MyService.this, accel, SensorManager.SENSOR_DELAY_FASTEST);
        }

        if(intent != null) {
            dbHelper = SHARD_DB.getDbHelper();

            switch (intent.getFlags()) {
                case 1:
                    int todayNumStep = dbHelper.selectTodayWalk(loginUserData.getUsernumber());
                    dbHelper.updateTodayDATA(SharedConstant.WALK ,loginUserData.getUsernumber(), 0, 0.0f, 0.0f, todayNumStep + numSteps);
                    numSteps = 0;
                    break;
                case 2:
                    // 서비스에 있던 걸음 수를 전송
                    BusProvider.getInstance().post(new StepEvent());
                    break;
            }
            return START_STICKY;
        }

        // 서비스가 강제 종료될시 다시 시작된다
        return START_STICKY;

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(sensorManager != null) {
            sensorManager.unregisterListener(MyService.this);
            dbHelper = SHARD_DB.getDbHelper();

            // 서비스가 중지될 때 db 저장
            int todayNumStep = dbHelper.selectTodayWalk(loginUserData.getUsernumber());
            dbHelper.updateTodayDATA(SharedConstant.WALK ,loginUserData.getUsernumber(), 0, 0.0f, 0.0f, todayNumStep + numSteps);

            // 걸음 수 초기화
            numSteps = 0;
        }
        Log.i("MyService", "onDestroy호출");
        Log.i("MyService", dbHelper.selectTodayWalk(loginUserData.getUsernumber()) + "");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        calorie = numSteps / 33;
        BusProvider.getInstance().post(new StepEvent());
        Log.i("MyService", numSteps + "");
    }

    public class StepEvent {
        private int mainStep;

        public StepEvent() {
            mainStep = numSteps;
        }

        public int getMainStep() {
            return mainStep;
        }

    }
}
