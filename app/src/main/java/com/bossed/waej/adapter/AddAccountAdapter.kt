package com.bossed.waej.adapter

import android.widget.EditText
import com.bossed.waej.R
import com.bossed.waej.javebean.AccountBean
import com.bossed.waej.util.TextChangedListener
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class AddAccountAdapter(data: MutableList<AccountBean>, private val type: Int) :
    BaseQuickAdapter<AccountBean, BaseViewHolder>(R.layout.layout_item_collection_settle, data) {
    override fun convert(helper: BaseViewHolder, item: AccountBean) {
        helper.setText(R.id.tv_name, item.accountName)
            .setText(R.id.tv_type, item.accountType)
            .setText(R.id.tv_account, if (type == 1) "收款账号" else "付款账号")
            .setText(R.id.tv_amount, if (type == 1) "收款金额" else "付款金额")
            .setText(R.id.et_amount, item.money)
            .addOnClickListener(R.id.iv_delete)
            .addOnClickListener(R.id.tv_name)
        val editText = helper.getView<EditText>(R.id.et_amount)
        editText.addTextChangedListener(object : TextChangedListener {
            override fun afterTextChange(s: String) {
                if (editText.isFocused) {
                    if (s == "-")
                        return
                    item.money = s
                    listener!!.invoke()
                }
            }
        })
    }

    fun setOnTextChangeListener(listener: (() -> Unit)) {
        this.listener = listener
    }

    private var listener: (() -> Unit)? = null
}