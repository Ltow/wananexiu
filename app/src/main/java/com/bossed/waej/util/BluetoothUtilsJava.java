package com.bossed.waej.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.widget.TextView;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.bossed.waej.R;
import com.bossed.waej.adapter.BluetoothDevicesAdapter;
import com.bossed.waej.base.BaseActivity;
import com.bossed.waej.base.BaseApplication;
import com.bossed.waej.customview.BottomDialog;
import com.bossed.waej.javebean.BluetoothDeviceBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothUtilsJava {
    public static BluetoothSocket mBluetoothSocket = null;
    private final Activity mContext;
    //蓝牙对象们
    private final List<BluetoothDeviceBean> mPairedDeviceList = new ArrayList<>();//mac列表
    private final BluetoothAdapter mBluetoothAdapter; // 创建蓝牙适配器
    //pop
    private BottomDialog popupWindow;
    private final ProgressDialog progressDialog;
    private final AlertDialog.Builder alertDialog;
    private final Handler handler;

    /**
     * 初始化打印设置
     *
     * @param context   上下文
     * @param printType 打印类型 按id/按单号
     * @param id        id/单号
     * @param orderType 表头（单据类型）
     */
    @SuppressLint("HandlerLeak")
    public BluetoothUtilsJava(final Activity context, final Integer printType, final String id, final Integer orderType) {
        this.mContext = context;
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        ToastUtils.showShort("蓝牙连接失败");
                        break;
                    case 1:
                        try {
                            BluetoothPrintUtils.Companion.printService(id, context, printType, orderType);//打印
                        } finally {
                            if (popupWindow != null)
                                if (popupWindow.isShowing())
                                    popupWindow.dismiss();
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                        }
                        break;
                    case 3:
                        if (popupWindow != null && popupWindow.isShowing())
                            popupWindow.dismiss();
                        if (progressDialog != null && progressDialog.isShowing())
                            progressDialog.dismiss();
                        print();
                        break;
                }
            }
        };
        //连接蓝牙进度条
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("连接中...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //打开本机蓝牙dialog
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();//获得本设备的蓝牙适配器实例
        setBluetoothStateListener();
        alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("系统提示:");
        alertDialog.setNeutralButton("是", (dialog, which) -> {
            mContext.startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
        });
        alertDialog.setPositiveButton("否", (dialog, which) -> {
            dialog.dismiss();
        });
    }

    public void print() {
        if (((BaseApplication) mContext.getApplication()).getSocket() == null) {
            mPairedDeviceList.clear();
            if (setDeviceData() == 1) {//加载数据
                showBluetoothConnPop(1);
            }
        } else {
            progressDialog.show();
            new Thread(() -> {
                try {
                    mBluetoothSocket.connect();
                    handler.sendEmptyMessage(1);
                } catch (Exception e) {
                    e.printStackTrace();
                    ((BaseApplication) mContext.getApplicationContext()).setSocket(null);
                    handler.sendEmptyMessage(3);
                }
            }).start();
        }
    }

    /**
     * @param v                 上下文
     * @param temString         设备地址
     * @param mBluetoothAdapter 本机适配器
     * @return handler msg 1销售单 2进货单 5调出单
     */
    public int connBluetooth(BaseActivity v, String temString, BluetoothAdapter mBluetoothAdapter, int msg) {
        mBluetoothSocket = ((BaseApplication) v.getApplication()).getSocket();
        if (mBluetoothSocket != null) {
            try {
                mBluetoothSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                ((BaseApplication) v.getApplication()).setSocket(null);
            }
            return 0;
        }
        try {
            //以给定的MAC地址去创建一个 BluetoothDevice 类实例(代表远程蓝牙实例)。即使该蓝牙地址不可见，也会产生一个BluetoothDevice 类实例。
            BluetoothDevice mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(temString);
            mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            mBluetoothSocket.connect();
            ((BaseApplication) v.getApplication()).setSocket(mBluetoothSocket);
            return msg;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 弹出连接蓝牙的pop
     */
    private void showBluetoothConnPop(final int msg) {
        popupWindow = new BottomDialog(mContext).create(R.layout.layout_pop_sel_account).setCanceledOnTouchOutside(false);
        popupWindow.setViewInterface((view, dialog) -> {
            TextView title = view.findViewById(R.id.tv_title);
            RecyclerView recyclerView = view.findViewById(R.id.rv_sel_account);
            title.setText("选择打印设备设备");
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            BluetoothDevicesAdapter adapter = new BluetoothDevicesAdapter(mPairedDeviceList);
            adapter.bindToRecyclerView(recyclerView);
            adapter.setOnItemClickListener((adapter1, view1, position) -> {
                progressDialog.show();
                new Thread(() -> {
                    handler.sendEmptyMessage(connBluetooth((BaseActivity) mContext, mPairedDeviceList.get(position).getAddress(), mBluetoothAdapter, msg));
                }).start();
            });
            view.findViewById(R.id.tv_cancel).setOnClickListener(v -> {
                dialog.dismiss();
            });
        }).show();
    }

    private int setDeviceData() {
        if (mBluetoothAdapter == null) {
            return 0;
        } else if (mBluetoothAdapter.isEnabled()) {
            Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter.getBondedDevices();//获取与本机蓝牙所有绑定的远程蓝牙信息
            for (BluetoothDevice device : mPairedDevices) {
                if (device.getBluetoothClass().getDeviceClass() == 1664 || device.getBluetoothClass().getDeviceClass() == 7936) {//1664为打印机
                    mPairedDeviceList.add(new BluetoothDeviceBean(device.getName(), device.getAddress()));//获取蓝牙设备的硬件地址(MAC地址)
                }
            }
            if (mPairedDeviceList.size() == 0) {
                alertDialog.setMessage("没有匹配过的打印设备\n是否在设置界面中进行一次匹配");
                alertDialog.create().show();
                return 0;
            }
        } else {
            alertDialog.setMessage("蓝牙没有打开\n是否在设置界面中打开？");
            alertDialog.create().show();
            return 0;
        }
        return 1;
    }

    private void setBluetoothStateListener() {
        ((BaseApplication) mContext.getApplication()).bluetoothReceiverRegister();
    }

}
