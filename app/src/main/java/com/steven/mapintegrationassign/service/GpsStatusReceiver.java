package com.steven.mapintegrationassign.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

import com.steven.mapintegrationassign.model.Utils;

public class GpsStatusReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);


        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            //Toast.makeText(context,"Please Turn ON GPS",Toast.LENGTH_LONG).show();
            Utils.showDialog(context, "Warning", "Please turn on GPS");
        } else {

        }
    }
}
