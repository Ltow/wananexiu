package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.BluetoothDeviceBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class BluetoothDevicesAdapter(mutableList: MutableList<BluetoothDeviceBean>) :
    BaseQuickAdapter<BluetoothDeviceBean, BaseViewHolder>(
        R.layout.layout_item_bluetooth_devices,
        mutableList
    ) {
    override fun convert(helper: BaseViewHolder, item: BluetoothDeviceBean) {
        helper.setText(R.id.tv_devices, item.name)
    }
}