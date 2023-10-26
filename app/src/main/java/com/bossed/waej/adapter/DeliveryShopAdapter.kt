package com.bossed.waej.adapter

import com.bossed.waej.R
import com.bossed.waej.javebean.DeliveryShopBean
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class DeliveryShopAdapter(layoutId: Int, list: ArrayList<DeliveryShopBean>) :
    BaseQuickAdapter<DeliveryShopBean, BaseViewHolder>(layoutId, list) {
    override fun convert(helper: BaseViewHolder, item: DeliveryShopBean) {
        helper.setText(R.id.tv_shop_name, item.fullname)
            .setText(R.id.tv_distance_need, "距离：" + item.distance + "KM")
            .setText(R.id.tv_sum_order_need, "订单数量：" + item.orderNum)
            .addOnClickListener(R.id.rl_need_repair_item)
        Glide.with(mContext).load(item.shopLogo).into(helper.getView(R.id.iv_logo_need_repair))
    }

}
