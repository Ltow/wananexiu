package com.bossed.waej.contract

import android.app.Activity
import com.bossed.waej.base.BaseContract
import com.bossed.waej.base.BaseResponse
import com.bossed.waej.javebean.MineResponse

interface MineContract {
    interface MineView : BaseContract.BaseView {
        fun onExitSuccess(success: BaseResponse)
        fun onUserInfoSuccess(success: MineResponse)
        fun onReLogin()
    }

    interface MineModel {
        fun exit(activity: Activity)
        fun getUserInfo(activity: Activity)
    }

}