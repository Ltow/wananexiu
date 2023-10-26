package com.bossed.waej.util

import android.app.Activity
import android.content.Intent
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.javebean.TreeListBean
import com.bossed.waej.javebean.TreeListData
import com.bossed.waej.ui.BuyProductActivity

class CheckPermissionUtils {
    companion object {
        fun checkPermission(menu: String, activity: Activity): Boolean {
            val t =
                GsonUtils.fromJson(
                    SPUtils.getInstance().getString("menus"),
                    TreeListBean::class.java
                )
            for (data: TreeListData in t.data) {
                if (data.menuName == menu && data.isPayPerm) {
                    return true
                }
            }
            if (SPUtils.getInstance().getString("userType") == "qx_admin")
                PopupWindowUtils.get()
                    .showSetConfirmAlertDialog(activity, "暂无使用权限，请开通后使用", "去开通", "") {
                        activity.startActivity(Intent(activity, BuyProductActivity::class.java))
                    }
            else
                ToastUtils.showShort("无当前使用权限")
            return false
        }

        fun hasPermission(menu: String): Boolean {
            val t =
                GsonUtils.fromJson(
                    SPUtils.getInstance().getString("menus"),
                    TreeListBean::class.java
                )
            for (data: TreeListData in t.data) {
                if (data.menuName == menu && data.isPayPerm) {
                    return true
                }
            }
            return false
        }
    }
}