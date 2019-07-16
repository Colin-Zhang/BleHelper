package com.colinzhang.blehelperlibrary


import android.content.Context
import com.colinzhang.blehelperlibrary.listener.OnConnectListener
import com.colinzhang.blehelperlibrary.listener.OnReceiveMessageListener
import com.colinzhang.blehelperlibrary.listener.OnSearchDeviceListener

class BleHelper private constructor() {
    companion object {
        val instance: BleHelper by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            BleHelper()
        }
    }

    fun init(context: Context) {
        BlueManager.instance.context = context.applicationContext
    }

    /**
     * 搜索设备
     */
    fun searchBleDevices(onSearchDeviceListener: OnSearchDeviceListener) {
        BlueManager.instance.searchDevices(onSearchDeviceListener)
    }

    /**
     * 断开链接
     */
    fun stopConnection() {
        BlueManager.instance.stopConnection()
    }

    /**
     * 连接设备
     * @param mac mac地址
     * @param onConnectListener 连接设备监听
     */
    fun connectDevice(mac: String, onConnectListener: OnConnectListener) {
        BlueManager.instance.connectDevice(mac, onConnectListener)
    }

    /**
     * 设置接收数据监听
     */
    fun setReceivedMessageListener(onReceiveMessageListener: OnReceiveMessageListener) {
        BlueManager.instance.onReceiveMessageListener = onReceiveMessageListener
    }
}
