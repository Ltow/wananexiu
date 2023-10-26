package com.bossed.waej.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.content.DialogInterface
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.provider.Settings
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.BluetoothDevicesAdapter
import com.bossed.waej.base.BaseActivity
import com.bossed.waej.base.BaseApplication
import com.bossed.waej.customview.BottomDialog
import com.bossed.waej.javebean.BluetoothDeviceBean
import com.bossed.waej.util.BluetoothPrintUtils.Companion.printService
import com.chad.library.adapter.base.BaseQuickAdapter
import java.lang.Exception
import java.util.*

class BluetoothUtils {
    companion object {
        private var mBluetoothSocket: BluetoothSocket? = null
        private var mContext: Activity? = null

        //蓝牙对象们
        private val mPairedDeviceList = ArrayList<BluetoothDeviceBean>() //mac列表
        private var mBluetoothAdapter: BluetoothAdapter? = null // 创建蓝牙适配器

        //pop
        private var popupWindow: BottomDialog? = null
        private var progressDialog: ProgressDialog? = null
        private var alertDialog: AlertDialog.Builder? = null
        private var handler: Handler? = null

        /**
         * 初始化打印设置
         *
         * @param context   上下文
         * @param printType 打印类型 按id/按单号
         * @param id        id/单号
         * @param orderType 表头（单据类型）
         */
        @SuppressLint("HandlerLeak")
        fun bluetoothUtils(
            context: Activity,
            printType: Int,
            id: String,
            orderType: Int
        ): Companion {
            mContext = context
            handler = object : Handler() {
                override fun handleMessage(msg: Message) {
                    when (msg.what) {
                        0 -> ToastUtils.showShort("蓝牙连接失败")
                        1 ->
                            try {
                                printService(id, context, printType, orderType) //打印
                            } finally {
                                if (popupWindow != null) if (popupWindow!!.isShowing()) popupWindow!!.dismiss()
                                if (progressDialog!!.isShowing) progressDialog!!.dismiss()
                            }
                        3 -> {
                            if (popupWindow != null && popupWindow!!.isShowing()) popupWindow!!.dismiss()
                            if (progressDialog != null && progressDialog!!.isShowing) progressDialog!!.dismiss()
                            print()
                        }
                    }
                }
            }
            //连接蓝牙进度条
            progressDialog = ProgressDialog(context)
            progressDialog!!.setMessage("连接中...")
            progressDialog!!.setCanceledOnTouchOutside(false)
            progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            //打开本机蓝牙dialog
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter() //获得本设备的蓝牙适配器实例
            setBluetoothStateListener()
            alertDialog = AlertDialog.Builder(context)
            alertDialog!!.setTitle("系统提示:")
            alertDialog!!.setNeutralButton("是") { dialog: DialogInterface?, which: Int ->
                mContext!!.startActivity(
                    Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
                )
            }
            alertDialog!!.setPositiveButton("否") { dialog: DialogInterface, which: Int -> dialog.dismiss() }
            return this
        }

        fun print() {
            if ((mContext!!.application as BaseApplication).getSocket() == null) {
                mPairedDeviceList.clear()
                if (setDeviceData() == 1) { //加载数据
                    showBluetoothConnPop(1)
                }
            } else {
                progressDialog!!.show()
                Thread {
                    try {
                        mBluetoothSocket!!.connect()
                        handler!!.sendEmptyMessage(1)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        (mContext!!.applicationContext as BaseApplication).setSocket(null)
                        handler!!.sendEmptyMessage(3)
                    }
                }.start()
            }
        }

        private fun setDeviceData(): Int {
            if (mBluetoothAdapter == null) {
                return 0
            } else if (mBluetoothAdapter!!.isEnabled) {
                val mPairedDevices = mBluetoothAdapter!!.bondedDevices //获取与本机蓝牙所有绑定的远程蓝牙信息
                for (device in mPairedDevices) {
                    if (device.bluetoothClass.deviceClass == 1664 || device.bluetoothClass.deviceClass == 7936) { //1664为打印机
                        mPairedDeviceList.add(
                            BluetoothDeviceBean(
                                device.name,
                                device.address
                            )
                        ) //获取蓝牙设备的硬件地址(MAC地址)
                    }
                }
                if (mPairedDeviceList.size == 0) {
                    alertDialog!!.setMessage("没有匹配过的打印设备\n是否在设置界面中进行一次匹配")
                    alertDialog!!.create().show()
                    return 0
                }
            } else {
                alertDialog!!.setMessage("蓝牙没有打开\n是否在设置界面中打开？")
                alertDialog!!.create().show()
                return 0
            }
            return 1
        }

        /**
         * 弹出选择蓝牙的pop
         */
        private fun showBluetoothConnPop(msg: Int) {
            popupWindow = BottomDialog(mContext!!).create(R.layout.layout_pop_sel_account)
                .setCanceledOnTouchOutside(true)
            popupWindow!!.setViewInterface { view, dialog ->
                val title = view.findViewById<TextView>(R.id.tv_title)
                val recyclerView: RecyclerView = view.findViewById(R.id.rv_sel_account)
                title.text = "选择打印设备"
                recyclerView.layoutManager = LinearLayoutManager(mContext)
                val adapter = BluetoothDevicesAdapter(mPairedDeviceList)
                adapter.bindToRecyclerView(recyclerView)
                adapter.setOnItemClickListener { adapter1: BaseQuickAdapter<*, *>?, view1: View?, position: Int ->
                    progressDialog!!.show()
                    Thread {
                        handler!!.sendEmptyMessage(
                            connBluetooth(
                                (mContext as BaseActivity?)!!,
                                mPairedDeviceList[position].address,
                                mBluetoothAdapter!!,
                                msg
                            )
                        )
                    }.start()
                }
                view.findViewById<View>(R.id.tv_cancel)
                    .setOnClickListener { v: View? -> dialog.dismiss() }
            }.show()
        }

        /**
         * @param v                 上下文
         * @param temString         设备地址
         * @param mBluetoothAdapter 本机适配器
         */
        private fun connBluetooth(
            v: BaseActivity,
            temString: String?,
            mBluetoothAdapter: BluetoothAdapter,
            msg: Int
        ): Int {
            mBluetoothSocket = (v.application as BaseApplication).getSocket()
            if (mBluetoothSocket != null) {
                try {
                    mBluetoothSocket!!.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    (v.application as BaseApplication).setSocket(null)
                }
                return 0
            }
            return try {
                //以给定的MAC地址去创建一个 BluetoothDevice 类实例(代表远程蓝牙实例)。即使该蓝牙地址不可见，也会产生一个BluetoothDevice 类实例。
                val mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(temString)
                mBluetoothSocket =
                    mBluetoothDevice.createRfcommSocketToServiceRecord(
                        UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
                    )
                mBluetoothSocket!!.connect()
                (v.application as BaseApplication).setSocket(mBluetoothSocket)
                msg
            } catch (e: Exception) {
                0
            }
        }

        private fun setBluetoothStateListener() {
            (mContext!!.application as BaseApplication).bluetoothReceiverRegister()
        }
    }
}