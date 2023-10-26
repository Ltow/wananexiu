package com.bossed.waej.util

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.base.BaseApplication

class BluetoothReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
        when (action) {
            BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                ToastUtils.showShort(device.name + "连接已断开")
                if ((context.applicationContext as BaseApplication).getSocket() != null) {
                    (context.applicationContext as BaseApplication).setSocket(null)
                }
            }
            BluetoothDevice.ACTION_ACL_CONNECTED->{
                if (device != null) {
                    ToastUtils.showShort(device.name + "已连接")
                }
            }
            BluetoothAdapter.ACTION_STATE_CHANGED->{
                val blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0)
                if (blueState == BluetoothAdapter.STATE_OFF) {
                    ToastUtils.showShort("蓝牙已关闭")
                    if ((context.applicationContext as BaseApplication).getSocket() != null) {
                        (context.applicationContext as BaseApplication).setSocket(null)
                    }
                }
            }
            BluetoothDevice.ACTION_BOND_STATE_CHANGED->{
                val state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1)
                when (state) {
                    BluetoothDevice.BOND_BONDING, BluetoothDevice.BOND_BONDED ->
                        // Toasts.showShortToast(context , "配对成功");
                        //                    Toasts.showShortToast(context , "正在配对");
                        if ((context.applicationContext as BaseApplication).getSocket() != null) {
                            (context.applicationContext as BaseApplication).setSocket(null)
                        }
                    else -> {}
                }
            }
        }
    }
}