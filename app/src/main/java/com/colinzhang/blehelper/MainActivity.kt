package com.colinzhang.blehelper

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat.requestPermissions
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.Log
import com.colinzhang.blehelperlibrary.BleHelper
import com.colinzhang.blehelperlibrary.SearchResult
import com.colinzhang.blehelperlibrary.listener.OnConnectListener
import com.colinzhang.blehelperlibrary.listener.OnReceiveMessageListener
import com.colinzhang.blehelperlibrary.listener.OnSearchDeviceListener
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity(), OnSearchDeviceListener, OnConnectListener, OnReceiveMessageListener {
    override fun onReceiveMessage(s: ByteArray?) {
        Log.e("message", s?.let { String(it) })
    }

    override fun onNotificationOpenSuccess() {
        Log.e("message", "通知开启成功")
    }

    override fun onConnectFailed() {
        Log.e("message", "连接失败")
    }

    override fun onConnectSuccess() {
        Log.e("message", "连接成功")
    }

    private var adapter: BleDeviceAdapter? = null
    private val PERMISSION_REQUEST_COARSE_LOCATION = 1
    private var deviceList: MutableList<BluetoothDevice> = mutableListOf()
    override fun onStartDiscovery() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onNewDeviceFound(device: BluetoothDevice?) {
        if (!device?.name.isNullOrEmpty()) {
            if (deviceList.none { TextUtils.equals(it.name, device?.name) }) {
                deviceList.add(device!!)
                adapter?.notifyDataSetChanged()
                Log.e("搜素到设备 :", device.name)
            }
        }
    }

    override fun onSearchCompleted(bondedList: MutableList<SearchResult>?, newList: MutableList<SearchResult>?) {

    }

    override fun onError(e: Exception?) {

        Log.e("Main", e.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecycle()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    PERMISSION_REQUEST_COARSE_LOCATION
                )
            }
        }
        search.setOnClickListener {
            BleHelper.instance.searchBleDevices(this)
        }
        stopConnection.setOnClickListener {
            deviceList.clear()
            BleHelper.instance.stopConnection()
        }
    }

    private fun initRecycle() {
        adapter = BleDeviceAdapter(deviceList)
        val layoutManager = LinearLayoutManager(applicationContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        deviceRecycler.layoutManager = layoutManager
        deviceRecycler.adapter = adapter
        adapter?.setItemClickListener {
            BleHelper.instance.connectDevice(deviceList[it].address, this)
        }
        BleHelper.instance.setReceivedMessageListener(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_COARSE_LOCATION ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
        }
    }
}
