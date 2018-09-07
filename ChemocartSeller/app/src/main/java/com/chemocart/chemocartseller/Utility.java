package com.chemocart.chemocartseller;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

public class Utility {
    public static final int REQUEST_PERMISSION_READ_PHONE = 100;

    public static boolean checkPErmissionReadPhone(final Context context){

        int currentAPIVersion = Build.VERSION.SDK_INT;

        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(context,
                    android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                        android.Manifest.permission.READ_PHONE_STATE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("This permission is necessary for this app");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context,
                                    new String[] { android.Manifest.permission.READ_PHONE_STATE }, REQUEST_PERMISSION_READ_PHONE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[] { android.Manifest.permission.READ_PHONE_STATE},
                            REQUEST_PERMISSION_READ_PHONE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }

    }
   /* public static void showDialogOK(final Activity context, String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(message);
        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Utility.checkAndRequestPermissions(context);
            }
        });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }
*/
}
