package com.steven.mapintegrationassign.model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.steven.mapintegrationassign.R;


public class Utils {

    public static void showDialog(Context context, String title, final String message) {

        AlertDialog.Builder alter = new AlertDialog.Builder(context);
        final AlertDialog alertDialog = alter.setTitle(title).setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        dialog.dismiss();
                    }
                }).show();

    }



    public static String getVersionNo(Context context){
        return context.getResources().getString(R.string.version);
    }


}