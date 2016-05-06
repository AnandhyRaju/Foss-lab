package com.example.crystaleyes.cip;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import android.database.sqlite.*;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    int i=1,j=1;
    static Double[][] d = new Double[20][20];
    int PLACE_PICKER_REQUEST = 1;
    PlacePicker.IntentBuilder builder;
    SharedPreferences shared,general,places;
    SharedPreferences.Editor edit,edit1,edit2;
    Switch mySwitch;
    Button select;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.print("********************************************");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySwitch = (Switch) findViewById(R.id.switch1);
        select=(Button)findViewById(R.id.button);
        shared = getApplicationContext().getSharedPreferences("BlockedLocations", Context.MODE_MULTI_PROCESS);
        edit = shared.edit();
        general = getApplicationContext().getSharedPreferences("General", Context.MODE_MULTI_PROCESS);
        edit1 = general.edit();
        places = getApplicationContext().getSharedPreferences("Places", Context.MODE_MULTI_PROCESS);
        edit2 = places.edit();
        if(!general.contains("app"))
            edit1.putString("app", "off");
        if(general.getString("app","").equals("on"))mySwitch.setChecked(true);
        else mySwitch.setChecked(false);
        if(!general.contains("rad"))
            edit1.putString("rad","100.0").commit();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    if (general.contains("app")) {
                        edit1.remove("app");
                        edit1.commit();
                    }
                    edit1.putString("app", "on");
                    edit1.commit();
                    Toast.makeText(MainActivity.this, "Turned On", Toast.LENGTH_SHORT).show();
                    getApplicationContext().startService(new Intent(getApplicationContext(), MyService.class));

                } else {
                    if (general.contains("app")) {
                        edit1.remove("app");
                        edit1.commit();
                    }
                    edit1.putString("app", "off");
                    edit1.commit();
                    Toast.makeText(MainActivity.this, "Turned Off", Toast.LENGTH_SHORT).show();
                    if (isMyServiceRunning(MyService.class))
                        getApplicationContext().stopService(new Intent(getApplicationContext(), MyService.class));
                }

            }
        });
        select.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(MainActivity.this), PLACE_PICKER_REQUEST);
                } catch (Exception e) {
                }

            }
        });
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public void selectLoc(View v) throws Exception {
       /* if(isMyServiceRunning(MyService.class)){
            Toast.makeText(MainActivity.this, "Service is running", Toast.LENGTH_SHORT).show();}
        Intent iloc=new Intent(this,MapsActivity.class);
        startActivity(iloc);
*/

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                LatLng latLng = place.getLatLng();
                edit.putString(Integer.toString(i++), Double.toString(latLng.latitude));
                edit.commit();
                if (shared.contains(Integer.toString(i - 1)))
                    Toast.makeText(MainActivity.this, "s", Toast.LENGTH_SHORT).show();
                edit.putString(Integer.toString(i++), Double.toString(latLng.longitude));
                edit.commit();
                if (shared.contains(Integer.toString(i - 1)))
                    Toast.makeText(MainActivity.this, "ss", Toast.LENGTH_SHORT).show();
                edit2.putString(Integer.toString(j++), place.getName().toString());
                edit2.commit();
                if (general.getString("app", "").equals("on")) {


               if (i == 3)
                    getApplicationContext().startService(new Intent(getApplicationContext(), MyService.class));
                else {
                    getApplicationContext().stopService(new Intent(getApplicationContext(), MyService.class));
                    getApplicationContext().startService(new Intent(getApplicationContext(), MyService.class));
                }
            }

                //mydatabase.execSQL("CREATE TABLE IF NOT EXISTS BlockedLocations(Place VARCHAR,Latitude DOUBLE,Longitude DOUBLE);");
                //mydatabase.execSQL("INSERT INTO BlockedLocations VALUES('"+place.getName()+"','"+Double.toString(latLng.latitude)+"','"+Double.toString(latLng.longitude)+";");
                /*edit.putString(place.getName() + "lat", Double.toString(latLng.latitude));
                edit.commit();
                edit.putString(place.getName() + "lng", Double.toString(latLng.longitude));
                edit.commit();*/
            }
        }
    }



    /*public static float calc(Double lat1, Double lng1) {
        Double lat, lng;
        float[] distance=new float[1];

            Location.distanceBetween(lat, lng, lat1, lng1, distance);
            // distance[0] is now the distance between these lat/lons in meters

        }
        return distance[0];
    }*/

    public void selectContact(View v) {
        Intent icon = new Intent(this, set_contacts.class);
        startActivity(icon);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_contact_seect, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings) {
            //Toast.makeText(MainActivity.this,shared.toString(), Toast.LENGTH_SHORT).show();
            Intent icon = new Intent(this, unblock_locs.class);
            startActivity(icon);
            /*edit.clear().commit();
            edit1.clear().commit();
            edit2.clear().commit();
            getApplicationContext().stopService(new Intent(getApplicationContext(),MyService.class));
            finish();
            startActivity(getIntent());*/
        }
        else if (id == R.id.set_rad) {
            //Toast.makeText(MainActivity.this,shared.toString(), Toast.LENGTH_SHORT).show();
            Intent icon = new Intent(this, set_radius.class);
            startActivity(icon);
        }

        return super.onOptionsItemSelected(item);
    }
}
