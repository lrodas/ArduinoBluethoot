package com.cycsystemsgt.arduinobluethoot.Util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Heinz Keydel on 6/01/2017.
 */

public class Util {

    public static void checkForPermission(Activity context, String permissionToCheck, int requestCode) {
        int permissionCheck = ContextCompat.checkSelfPermission(context, permissionToCheck);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);
        }
    }

    public static boolean hasPermission(Context context, String permissionToCheck) {
        int permissionCheck = ContextCompat.checkSelfPermission(context, permissionToCheck);
        return (permissionCheck == PackageManager.PERMISSION_GRANTED);
    }

}
