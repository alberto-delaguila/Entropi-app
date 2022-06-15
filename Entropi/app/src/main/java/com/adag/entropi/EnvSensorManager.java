package com.adag.entropi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.w3c.dom.ls.LSException;

import java.util.List;

public class EnvSensorManager implements LocationListener {

    private SensorManager sm;
    private LocationManager lm;
    private Boolean location_allowed;
    private List<String> sensorValues;

    private Sensor light_sensor;
    private SensorEventListener sel_light = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            sensorValues.set(0, String.valueOf(event.values[0]));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private Sensor amb_temp_sensor;
    private SensorEventListener sel_amb = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            sensorValues.set(1, String.valueOf(event.values[0]));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private Sensor hum_sensor;
    private SensorEventListener sel_hum = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            sensorValues.set(2, String.valueOf(event.values[0]));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    public EnvSensorManager(SensorManager sensorManager, LocationManager locationManager, int location_permission){

        location_allowed = (location_permission == PackageManager.PERMISSION_GRANTED);

        sm = sensorManager;
        light_sensor = sm.getDefaultSensor(Sensor.TYPE_LIGHT);
        amb_temp_sensor = sm.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        hum_sensor = sm.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);

        sm.registerListener(sel_light, light_sensor, SensorManager.SENSOR_DELAY_FASTEST);
        sm.registerListener(sel_amb, amb_temp_sensor, SensorManager.SENSOR_DELAY_FASTEST);
        sm.registerListener(sel_hum, hum_sensor, SensorManager.SENSOR_DELAY_FASTEST);

        lm = locationManager;

    }

    public List<String> getSensorValues(){
        return sensorValues;
    }

    @SuppressLint("MissingPermission")
    public String[] getLocationValues(){

        if(lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER) && location_allowed) {
            Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            return new String[]{String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude())};
        }else{
            return new String[]{"37", "-2"}; //Default location in Parque Natural Cabo de Gata - Níjar
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }
}
