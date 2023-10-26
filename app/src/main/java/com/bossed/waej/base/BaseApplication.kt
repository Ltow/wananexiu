package com.bossed.waej.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.IntentFilter
import cn.jpush.android.api.JPushInterface
import com.blankj.utilcode.util.LogUtils
import com.bossed.waej.util.BluetoothReceiver
import io.reactivex.plugins.RxJavaPlugins
import me.jessyan.autosize.AutoSize
import me.jessyan.autosize.AutoSizeConfig
import me.jessyan.autosize.onAdaptListener
import me.jessyan.autosize.utils.AutoSizeLog
import java.util.*


/**
 * Created by Android Studio.
 * User: liulei
 * Date: 2021-07-20
 * Time: 17:42
 */
class BaseApplication : Application() {
    private var socket: BluetoothSocket? = null
    private var bluetoothReceiver: BluetoothReceiver? = null

    fun bluetoothReceiverRegister() {
        if (bluetoothReceiver == null) {
            bluetoothReceiver = BluetoothReceiver()
            val filter = IntentFilter()
            filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
            filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
            filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
            filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
            registerReceiver(bluetoothReceiver, filter)
        }
    }

    fun bluetoothReceiverUnregister() {
        if (bluetoothReceiver != null) {
            unregisterReceiver(bluetoothReceiver)
            bluetoothReceiver = null
        }
    }

    fun getSocket(): BluetoothSocket? {
        return socket
    }

    fun setSocket(socket: BluetoothSocket?) {
        this.socket = socket
    }

    override fun onCreate() {
        super.onCreate()
        RxJavaPlugins.setErrorHandler { throwable: Throwable ->
            LogUtils.e("tag", throwable.message)
        }
        context = applicationContext
        CrashHandler.getInstance(this)//异常捕获
        socket = null
        setSocket(socket)
        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)
        //当 App 中出现多进程, 并且您需要适配所有的进程, 就需要在 App 初始化时调用 initCompatMultiProcess()
        AutoSize.initCompatMultiProcess(this)
        /**
         * 以下是 AndroidAutoSize 可以自定义的参数, {@link AutoSizeConfig} 的每个方法的注释都写的很详细
         * 使用前请一定记得跳进源码，查看方法的注释, 下面的注释只是简单描述!!!
         */
        AutoSizeConfig.getInstance()
            //是否让框架支持自定义 Fragment 的适配参数, 由于这个需求是比较少见的, 所以须要使用者手动开启
            //如果没有这个需求建议不开启
            .setCustomFragment(true).onAdaptListener = object : onAdaptListener {
            override fun onAdaptBefore(target: Any?, activity: Activity?) {

                //使用以下代码, 可以解决横竖屏切换时的屏幕适配问题
                //使用以下代码, 可支持 Android 的分屏或缩放模式, 但前提是在分屏或缩放模式下当用户改变您 App 的窗口大小时
                //系统会重绘当前的页面, 经测试在某些机型, 某些情况下系统不会重绘当前页面, ScreenUtils.getScreenSize(activity) 的参数一定要不要传 Application!!!
                //                        AutoSizeConfig.getInstance().setScreenWidth(ScreenUtils.getScreenSize(activity)[0]);
                //                        AutoSizeConfig.getInstance().setScreenHeight(ScreenUtils.getScreenSize(activity)[1]);
                AutoSizeLog.d(
                    String.format(
                        Locale.ENGLISH,
                        "%s onAdaptBefore!",
                        target?.javaClass!!.name
                    )
                )
            }

            override fun onAdaptAfter(target: Any?, activity: Activity?) {
                AutoSizeLog.d(
                    String.format(
                        Locale.ENGLISH,
                        "%s onAdaptAfter!",
                        target?.javaClass!!.name
                    )
                )
            }

        }
    }

    companion object {
        /**
         * context
         */
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
            private set
    }

}