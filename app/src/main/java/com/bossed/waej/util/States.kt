package com.bossed.waej.util


object States {

    //区分查询单据列表状态
    const val SERVICE = 3//服务中
    const val RECEIVE = 2//接待单

    //    const val LIST = 1//操作中单据
    const val HISTORY = 0//历史单据

    //区分自定义TextView 颜色渐变防线
    const val horizontal = 1
    const val vertical = 0

    //申请外部安装权限
    const val INSTALL_PERMISSION_CODE = 0

    //10以上系统申请外部文件权限
    const val ALL_FILES_ACCESS_PERMISSION = 1

    object OrderPrintType {
        const val frID = 0//按ID
        const val frSN = 1//按单号
    }

    object PrintSettingType {
        const val HEAD = 1
        const val BODY = 2
        const val FOOT = 3
        const val TOTAL = 4
        const val COMPANY = 5
        const val NOTE = 6
        const val CODE = 7
        const val IMG = 8
    }

    /**
     * 小票类型
     */
    object PrinterPageSize {
        const val PRINTER_58mm = "58mm"
        const val PRINTER_80mm = "80mm"
    }

}