package com.colinzhang.blehelperlibrary.listener;

/**
 * Created by zhang on 2018/12/12.
 * 蓝牙连接监听
 */

public interface OnConnectListener extends IErrorListener {

    /**
     * 设备连接失败
     */
    void onConnectFailed();

    /**
     * 设备连接成功
     */
    void onConnectSuccess();

    /**
     * 设备通知开启成功
     */
    void onNotificationOpenSuccess();
}
