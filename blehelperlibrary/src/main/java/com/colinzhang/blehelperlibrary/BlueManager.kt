package com.colinzhang.blehelperlibrary

import android.bluetooth.*

import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.ContentValues.TAG

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.text.TextUtils
import android.util.Log
import com.colinzhang.blehelperlibrary.listener.OnConnectListener
import com.colinzhang.blehelperlibrary.listener.OnReceiveMessageListener

import com.colinzhang.blehelperlibrary.listener.OnSearchDeviceListener
import java.lang.Exception
import java.util.*
import java.util.Objects.requireNonNull
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService


/**
 * Created by zhang on 2018/12/11.
 */

internal class BlueManager : BluetoothAdapter.LeScanCallback, BluetoothLeClass.OnConnectListener,
    BluetoothLeClass.OnDataAvailableListener {

    private val NOT_BLUETOOTH_MODULE = "device has not bluetooth module!"

    /**
     * 连接蓝牙需要用到ApplicationContext
     */
    var context: Context? = null
    /**
     * 搜索到的设备list
     */
    private var deviceList: MutableList<BluetoothDevice> = mutableListOf()

    /**
     * 蓝牙搜素回调
     */
    private var onSearchDeviceListener: OnSearchDeviceListener? = null
    /**
     * 蓝牙适配器
     */
    private var mBluetoothAdapter: BluetoothAdapter? = null
    /**
     * 连接操作类
     */
    private var mBLE: BluetoothLeClass? = null
    /**
     * 手机连接蓝牙状态监听
     */
    private var onConnectListener: OnConnectListener? = null
    /**
     * 收到设备发送的消息监听
     */
    var onReceiveMessageListener: OnReceiveMessageListener? = null
    /**
     * 通知蓝牙特征
     */
    private var notifyCharacter: BluetoothGattCharacteristic? = null
    /**
     * 写入蓝牙设备特征
     */
    private var writeCharacter: BluetoothGattCharacteristic? = null
    /**
     * 设备UUID
     */
    private var bleUUID: BleUUID? = null
    /**
     * 设备连接状态监听
     */
    private val mOnServiceDiscover =
        BluetoothLeClass.OnServiceDiscoverListener { displayGattServices(mBLE?.supportedGattServices) }

    init {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    }

    companion object {
        val instance: BlueManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            BlueManager()
        }
    }

    /**
     * 关闭蓝牙
     */
    private fun requestEnableBt() {
        if (mBluetoothAdapter == null) {
            throw NullPointerException(NOT_BLUETOOTH_MODULE)
        }
        if (!mBluetoothAdapter!!.isEnabled)
            mBluetoothAdapter!!.enable()
    }

    /**
     * 搜索蓝牙设备
     * @param onSearchDeviceListener 搜索蓝牙回调
     */
    fun searchDevices(onSearchDeviceListener: OnSearchDeviceListener) {
        requireNonNull(onSearchDeviceListener, "SearchDeviceListener is Null")
        deviceList.clear()
        this.onSearchDeviceListener = onSearchDeviceListener
        if (mBluetoothAdapter == null)
            onSearchDeviceListener.onError(NullPointerException(NOT_BLUETOOTH_MODULE))
        else {
            if (Build.VERSION.SDK_INT >= 21) {
                val scanner = mBluetoothAdapter?.bluetoothLeScanner
                if (mBluetoothAdapter!!.isDiscovering)
                    scanner?.stopScan(mBleScanCallBack)
                scanner?.startScan(mBleScanCallBack)
            } else {
                if (mBluetoothAdapter!!.isDiscovering)
                    mBluetoothAdapter!!.stopLeScan(this)
                mBluetoothAdapter!!.startLeScan(this)
            }
        }
    }

    /**
     * 通过mac地址连接到设备
     * @param mac 物理地址
     * @param onConnectListener 连接回调
     */
    fun connectDevice(mac: String, onConnectListener: OnConnectListener) {
        requireNotNull(context)
        mBLE = BluetoothLeClass(context)
        mBLE?.setOnConnectListener(this)
        mBLE?.setOnServiceDiscoverListener(mOnServiceDiscover)
        mBLE?.setOnDataAvailableListener(this)
        this.onConnectListener = onConnectListener
        mBLE?.setBleAdapter(mBluetoothAdapter)
        mBLE?.connect(mac)
    }

    /**
     * 停止搜索设备
     */
    fun stopSearchDevices() {
        requireNonNull(onSearchDeviceListener, "SearchDeviceListener is Null")
        requireNonNull(mBluetoothAdapter)
        deviceList.clear()
        if (mBluetoothAdapter!!.isDiscovering) {
            if (Build.VERSION.SDK_INT >= 21)
                mBluetoothAdapter!!.bluetoothLeScanner.stopScan(mBleScanCallBack)
            else
                mBluetoothAdapter!!.stopLeScan(this)
        }
    }

    /**
     * API Level小于21的蓝牙搜素回调
     * @param device 搜索到的设备
     */
    override fun onLeScan(device: BluetoothDevice?, rssi: Int, scanRecord: ByteArray?) {
        if (device?.name.isNullOrEmpty()) {
            if (deviceList.none { TextUtils.equals(it.name, device?.name) }) {
                device?.let { deviceList.add(it) }
                onSearchDeviceListener!!.onNewDeviceFound(device)
            }
        }
    }

    /**
     * API Level大于 21的蓝牙搜素回调
     * 过滤没有名字与重复的设备
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private val mBleScanCallBack = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            if (!result?.device?.name.isNullOrEmpty()) {
                if (deviceList.none { TextUtils.equals(it.name, result?.device?.name) }) {
                    result?.device?.let { deviceList.add(it) }
                    onSearchDeviceListener!!.onNewDeviceFound(result!!.device)
                }
            }
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            onSearchDeviceListener!!.onError(Exception("Search Failed"))
        }
    }

    /**
     * 连接设备成功停止搜素回调
     */
    override fun onConnect(gatt: BluetoothGatt?) {
        stopSearchDevices()
        onConnectListener?.onConnectSuccess()
    }

    /**
     * @Date : 2018/12/6
     * @Description :开启通知
     */
    private fun displayGattServices(gattServices: List<BluetoothGattService>?) {
        requireNotNull(gattServices)
        requireNonNull(onConnectListener, "onConnectListener is null")
        initCharacteristicUuid(gattServices)
        writeCharacter = getCharacteristic(bleUUID?.ServiceUUID, bleUUID?.CharacterUUID_WRITE)
        notifyCharacter = getCharacteristic(bleUUID?.ServiceUUID, bleUUID?.CharacterUUID_NOTIFY)
        mBLE!!.setCharacteristicNotification(notifyCharacter, true)
        notifyCharacter?.descriptors?.forEach {
            setDescriptorValue(
                notifyCharacter,
                it.uuid.toString(),
                BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            )
        }
        onConnectListener?.onNotificationOpenSuccess()
    }


    /**
     * 读取设备数据
     */
    override fun onCharacteristicRead(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {

    }

    /**
     * 收到设备数据
     * @param gatt 设备特征
     * @param characteristic 设备发送过来的数据
     */
    override fun onCharacteristicWrite(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {
        requireNonNull(onReceiveMessageListener, "onReceiveMessageListener is Null")
        onReceiveMessageListener?.onReceiveMessage(characteristic?.value)
    }

    /**
     * 向设备写入数据
     * @param bytes 需要写入设备的数据
     * @return 是否发送成功
     */
    fun writeDataToDevice(bytes: ByteArray): Boolean {
        requireNonNull(bytes,"SendData is Null")
        mBLE!!.setCharacteristicNotification(notifyCharacter, true)
        writeCharacter!!.value = bytes
        return mBLE!!.writeCharacteristic(writeCharacter)
    }

    /**
     * 获取特征
     *
     * @param serviceID
     * @param characteristicID
     * @return
     */
    private fun getCharacteristic(serviceID: String?, characteristicID: String?): BluetoothGattCharacteristic {
        val linkLossService = mBLE?.getmBluetoothGatt()?.getService(UUID.fromString(serviceID))
        return linkLossService?.getCharacteristic(UUID.fromString(characteristicID))!!
    }

    /**
     * 设置描述
     * @param characteristic
     * @param descID
     * @param value
     */
    private fun setDescriptorValue(
        characteristic: BluetoothGattCharacteristic?,
        descID: String,
        value: ByteArray
    ): Boolean? {
        val desc = characteristic?.getDescriptor(UUID.fromString(descID))
        desc?.value = value
        return mBLE?.getmBluetoothGatt()?.writeDescriptor(desc)
    }

    /**
     * 获取设备特征uuid
     * @param gattServices 服务列表
     */
    private fun initCharacteristicUuid(gattServices: List<BluetoothGattService>) {
        bleUUID = BleUUID()
        for (bluetoothGattService in gattServices) {
            val characteristics = bluetoothGattService.characteristics
            for (characteristic in characteristics) {
                val charaProp = characteristic.properties
                if (charaProp and BluetoothGattCharacteristic.PROPERTY_READ > 0) {
                    bleUUID?.CharacterUUID_READ = characteristic.uuid.toString()
                    bleUUID?.ServiceUUID = bluetoothGattService.uuid.toString()
                }
                if (charaProp and BluetoothGattCharacteristic.PROPERTY_WRITE > 0) {
                    bleUUID?.CharacterUUID_WRITE = characteristic.uuid.toString()
                    bleUUID?.ServiceUUID = bluetoothGattService.uuid.toString()
                }
                if (charaProp and BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE > 0) {
                    bleUUID?.CharacterUUID_WRITE = characteristic.uuid.toString()
                    bleUUID?.ServiceUUID = bluetoothGattService.uuid.toString()
                }
                if (charaProp and BluetoothGattCharacteristic.PROPERTY_NOTIFY > 0) {
                    bleUUID?.CharacterUUID_NOTIFY = characteristic.uuid.toString()
                    bleUUID?.ServiceUUID = bluetoothGattService.uuid.toString()
                }
            }
        }
    }

    /**
     * 断开当前连接
     */
    fun stopConnection() {
        mBLE?.disconnect()
        mBLE?.close()
        mBLE = null
        deviceList.clear()
    }
}
