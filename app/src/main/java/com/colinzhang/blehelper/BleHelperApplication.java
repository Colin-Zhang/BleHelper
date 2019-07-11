package com.colinzhang.blehelper;

import android.app.Application;
import com.colinzhang.blehelperlibrary.BleHelper;

public class BleHelperApplication  extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BleHelper.Companion.getInstance().init(getApplicationContext());
    }
}
