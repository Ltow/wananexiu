package com.bossed.waej.adapter

import android.widget.ImageView
import com.bossed.waej.R
import com.bossed.waej.javebean.ShopBean
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by Android Studio.
 * User: Administrator
 * Date: 2021-11-19
 * Time: 18:19
 */
class ShopInformationAdapter(layoutId: Int, list: MutableList<ShopBean>) :
    BaseQuickAdapter<ShopBean, BaseViewHolder>(layoutId, list) {

    override fun convert(holder: BaseViewHolder, item: ShopBean) {
        holder.setText(R.id.tv_shop_name, item.fullname)
            .setText(R.id.tv_sum_shop_information, "距离：" + item.distance + "km")
            .setText(R.id.tv_phone_order_information, item.shopPhone)
            .addOnClickListener(R.id.iv_call_phone)
            .addOnClickListener(R.id.rl_shop_information_item)
        val imageView = holder.getView<ImageView>(R.id.iv_logo_shop_information)
        Glide.with(mContext).load(item.doorPhoto).into(imageView)
    }
}