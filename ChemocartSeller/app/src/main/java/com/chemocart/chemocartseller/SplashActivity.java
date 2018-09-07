package com.chemocart.chemocartseller;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_PHONE_STATE;

public class SplashActivity extends AppCompatActivity {

    UserSession session;
    Handler handler;
    ImageView imageView;
    Animation animation;
    Context context;
    Activity activity;
    public static final int REQUEST_PERMISSION = 100;
    String[] PERMISSIONS = {INTERNET,
            READ_PHONE_STATE};
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashfile);
        imageView = findViewById(R.id.image);
        animation = AnimationUtils.loadAnimation(this,R.anim.first);
        imageView.setAnimation(animation);

        context = SplashActivity.this;
        activity = SplashActivity.this;


        if(Utility.checkPErmissionReadPhone(this)){
            callNextActivity();
        }
        /*else{
//            Utility.showDialogOK();
            finishAndRemoveTask();
        }
*/
       /* if(checkPermission()){
            callNextActivity();
        }else{
            requestPermission();
        }*/

        /*if(Build.VERSION.SDK_INT>=23){
            *//*String[] PERMISSIONS = {Manifest.permission.INTERNET,
                    Manifest.permission.READ_PHONE_STATE};*//*

            if(!hasPermission(context, PERMISSIONS)){
                ActivityCompat.requestPermissions(activity,PERMISSIONS, REQUEST_PERMISSION);
            }else{
                callNextActivity();

            }

        }else{
           callNextActivity();
        }*/

    }

   /* private void requestPermission() {
        
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), INTERNET);
        return result==PackageManager.PERMISSION_GRANTED && result1==PackageManager.PERMISSION_GRANTED ;
    }*/

    /*private boolean hasPermission(Context context, String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_PHONE_STATE)){
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Permission necessary");
                        alertBuilder.setMessage("This permission is mandatory for the app!!!");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_PERMISSION);
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();
                    }else{
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_PERMISSION);
                    }
                    return false;
                }
            }
        }
        return true;
    }*/

    private void callNextActivity() {
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                session = new UserSession(getApplicationContext());
                session.checkLogin();
                if(!session.checkLogin())
                {
                    Toast.makeText(SplashActivity.this, "Hello from not logged in", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },3000);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case Utility.REQUEST_PERMISSION_READ_PHONE: {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    Log.d("TAG","@@@ PERMISSIONS grant");
                    callNextActivity();
                } else if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_PHONE_STATE)){
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Permission necessary");
                        alertBuilder.setMessage("This permission is necessary for this app");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) context,
                                        new String[] { android.Manifest.permission.READ_PHONE_STATE }, REQUEST_PERMISSION);
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();

                    } else {
                        ActivityCompat.requestPermissions((Activity) context, new String[] { android.Manifest.permission.READ_PHONE_STATE},
                                REQUEST_PERMISSION);

                    }
                    /*Intent i = new Intent();
                    i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    i.addCategory(Intent.CATEGORY_DEFAULT);
                    i.setData(Uri.parse("package:" + context.getPackageName()));
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    context.startActivity(i);*/
                } else {
                   /* Log.d("TAG","@@@ PERMISSIONS Denied");
                    Toast.makeText(context, "PERMISSIONS Denied", Toast.LENGTH_LONG).show();*/
//                    finish();
//                    System.exit(0);
//                    finishAffinity();
                    finishAndRemoveTask();

                }
            }
            break;
        }
    }
}
