package com.example.crystaleyes.cip;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import android.database.sqlite.*;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import com.example.crystaleyes.cip.MainActivity;

import static com.example.crystaleyes.cip.MainActivity.*;

public class MyService extends Service
{
    private static final String TAG = "BOOMBOOMTESTGPS";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 10000;
    private static final float LOCATION_DISTANCE =0f;
    SharedPreferences shared,general;
    SharedPreferences.Editor edit;
    Map<String,?> map;
    Object[] ob;
    @Override
    public void onCreate()
    {
        Toast.makeText(MyService.this, "Hello", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "onCreate");
        initializeLocationManager();
        shared=getApplicationContext().getSharedPreferences("BlockedLocations", Context.MODE_MULTI_PROCESS);
        general=getApplicationContext().getSharedPreferences("General",Context.MODE_MULTI_PROCESS);
        edit=general.edit();
        map=shared.getAll();ob=map.keySet().toArray();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }


    private class LocationListener implements android.location.LocationListener {
        int i = 0;

        Location mLastLocation;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            i = 0;
            Double lat2, lng2;

            Log.e(TAG, "onLocationChanged: " + location);
            System.out.print("********************************************2");
            mLastLocation.set(location);
            lat2 = location.getLatitude();
            lng2 = location.getLongitude();
            Toast.makeText(getApplicationContext(), location.toString(), Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "" + map.size(), Toast.LENGTH_SHORT).show();
            Arrays.sort(ob);
            if (!map.isEmpty()) {
                while (i < map.size()) {
                    String lat = map.get(ob[i++]).toString();
                    String lng = map.get(ob[i++]).toString();
                    Double lat1 = Double.parseDouble(lat);
                    Double lng1 = Double.parseDouble(lng);
                    // phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    float[] distance = new float[1];

                    Location.distanceBetween(lat1, lng1, lat2, lng2, distance);
                    Toast.makeText(getApplicationContext(), Double.toString(distance[0]), Toast.LENGTH_SHORT).show();
                    if (general.getString("app", "").equals("on")) {
                        if (distance[0] < 500.0) {
                            // System.out.print("********************************************");
                            Toast.makeText(getApplicationContext(), "haiiiiiii", Toast.LENGTH_SHORT).show();

                            AudioManager audio = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                            audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                            edit.putString("loc", "on");
                            edit.commit();
                        } else {
                            if (!(general.getString("loc", "").equals("on"))) {
                                AudioManager audio = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                                audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                            } else {
                                edit.remove("loc").commit();
                                edit.putString("loc", "off");
                                edit.commit();
                            }

                        }
                    } else {
                        AudioManager audio = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                        audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        if (general.contains("loc")) {
                            edit.remove("loc");
                            edit.commit();
                        }
                    }

                    // MainActivity a=new MainActivity();
                    //dis=MainActivity.calc(location.getLatitude(), location.getLongitude());
                    //Toast.makeText(getApplicationContext(),"hai hello  "+dis, Toast.LENGTH_SHORT).show();

            /*
                float[] distance = new float[1];
                Location.distanceBetween(lat, lng, location.getLatitude(),location.getLongitude() , distance);
                // distance[0] is now the distance between these lat/lons in meters
                Toast.makeText(getApplicationContext(),""+distance[0] , Toast.LENGTH_SHORT).show();
                mydatabase.close();
                /*if (distance[0] < 2.0) {
                    // your code...
                }*/
                    LocationListener[] mLocationListeners = new LocationListener[] {
                            new LocationListener(LocationManager.GPS_PROVIDER),
                            new LocationListener(LocationManager.NETWORK_PROVIDER)
                    };
                }
            }
        }



            @Override
        public void onProviderDisabled(String provider)
        {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

            @Override
        public void onProviderEnabled(String provider)
        {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.e(TAG, "onStatusChanged: " + provider);
        }


    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }



    @Override
    public void onDestroy()
    {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }

    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}
