package com.colinzhang.blehelperlibrary.listener;

import android.bluetooth.BluetoothDevice;
import com.colinzhang.blehelperlibrary.SearchResult;


import java.util.List;

/**
 * Created by zhang on 2018/12/11.
 * 搜索蓝牙设备监听
 */

public interface OnSearchDeviceListener extends IErrorListener {

    void onStartDiscovery();


    void onNewDeviceFound(BluetoothDevice device);


    void onSearchCompleted(List<SearchResult> bondedList, List<SearchResult> newList);
}
