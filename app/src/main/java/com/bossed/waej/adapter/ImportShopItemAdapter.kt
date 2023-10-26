package com.bossed.waej.adapter

import android.text.TextUtils
import android.text.TextWatcher
import android.widget.EditText
import com.blankj.utilcode.util.ToastUtils
import com.bossed.waej.R
import com.bossed.waej.javebean.ImportItemRow
import com.bossed.waej.util.TextChangedListener
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import java.math.BigDecimal

class ImportShopItemAdapter(mutableList: MutableList<ImportItemRow>) :
    BaseQuickAdapter<ImportItemRow, BaseViewHolder>(
        R.layout.layout_item_import_item, mutableList
    ) {
    override fun convert(helper: BaseViewHolder, item: ImportItemRow) {
        helper.setText(R.id.tv_name, item.serviceItem)
            .setChecked(R.id.ctv_item, item.isSelect)
        if (!TextUtils.isEmpty(item.logo))
            Glide.with(mContext).load(item.logo).into(helper.getView(R.id.iv_logo))
        else
            helper.setImageResource(
                R.id.iv_logo, when (item.categoryId) {
                    1 -> R.mipmap.icon_mrl
                    2 -> R.mipmap.icon_byl
                    3 -> R.mipmap.icon_ltl
                    else -> R.mipmap.icon_car_logo
                }
            )
        val virtualPrice = helper.getView<EditText>(R.id.tv_lsj)//虚拟
        val originalPrice = helper.getView<EditText>(R.id.tv_hyj)//实际
        val priceCash = helper.getView<EditText>(R.id.tv_jsj)//会员
        virtualPrice.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                item.virtualPrice = s
            }
        })
        originalPrice.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                item.originalPrice = s
            }
        })
        priceCash.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                item.priceCash = s
                if (BigDecimal(if (TextUtils.isEmpty(s)) "0" else s) > BigDecimal(
                        if (TextUtils.isEmpty(
                                originalPrice.text.toString()
                            )
                        ) "0" else originalPrice.text.toString()
                    )
                ) {
                    ToastUtils.showShort("会员价不能大于零售价")
                }
            }
        })
    }

}