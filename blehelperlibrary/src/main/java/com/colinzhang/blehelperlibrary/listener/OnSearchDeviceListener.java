package com.colinzhang.blehelperlibrary.listener;

import android.bluetooth.BluetoothDevice;




/**
 * Created by zhang on 2018/12/11.
 * 搜索蓝牙设备监听
 */

public interface OnSearchDeviceListener extends IErrorListener {


    void onNewDeviceFound(BluetoothDevice device);


}
