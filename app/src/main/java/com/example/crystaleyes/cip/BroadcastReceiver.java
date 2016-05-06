package com.example.crystaleyes.cip;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.provider.Telephony;
import android.telephony.TelephonyManager;
import android.util.Log;
public class BroadcastReceiver extends android.content.BroadcastReceiver {


    @Override
    public void onReceive(Context arg0, Intent arg1) {
        AudioManager audio = (AudioManager) arg0.getSystemService(Context.AUDIO_SERVICE);
        if(arg1.getAction().equals("android.intent.action.PHONE_STATE")){

            String state = arg1.getStringExtra(TelephonyManager.EXTRA_STATE);

            if(state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                Log.d("A", "Inside Extra state off hook");
                String number = arg1.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                Log.e("OutNumber", "outgoing number : " + number);

            }


            else if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                Log.e("B", "Inside EXTRA_STATE_RINGING");
                String number = arg1.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                Log.e("InNumber", "incoming number : " + number);
                SharedPreferences sharedpreferences = MyApplication.getAppContext().getSharedPreferences("BlockedContacts", Context.MODE_MULTI_PROCESS);
                SharedPreferences general = MyApplication.getAppContext().getSharedPreferences("General", Context.MODE_MULTI_PROCESS);
                if(general.getString("app","").equals("on")) {
                    if (general.contains("loc") || sharedpreferences.contains(number)) {
                        audio.setRingerMode(0);
                    } else {
                        audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    }
                }


            }
            else if(state.equals(TelephonyManager.EXTRA_STATE_IDLE))
            {
                //AudioManager audio = (AudioManager) arg0.getSystemService(Context.AUDIO_SERVICE);

            }
            else if(state.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)){
                Log.d("C", "Inside EXTRA_STATE_IDLE");
                //AudioManager audio = (AudioManager)arg0.getSystemService(Context.AUDIO_SERVICE);
                //audio.setRingerMode(mode);

            }
        }
    }
}
