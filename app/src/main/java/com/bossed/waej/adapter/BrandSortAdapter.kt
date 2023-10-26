package com.bossed.waej.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bossed.waej.R
import com.bossed.waej.javebean.Brand
import com.bossed.waej.util.GlideUtils
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_letter_layout.view.*
import kotlinx.android.synthetic.main.layout_item_all_brand.view.*
import com.bumptech.glide.request.RequestOptions


class BrandSortAdapter : RecyclerView.Adapter<BrandSortAdapter.BrandHolder>() {
    var mData: MutableList<Brand>? = null

    inner class BrandHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(position: Int) = with(itemView) {
            val brand = mData!![position]
            name.text = brand.name
            GlideUtils.get().loadCarLogo(context, brand.logo, iv_car_brand)
            rl_pin_pai.setOnClickListener {
                onItemClickListener!!.onClick(position)
            }
            if (!compareSection(position)) {
                tv_letter?.visibility = View.VISIBLE
                tv_letter?.text = brand.letter
                line_view?.visibility = View.GONE
                ll_top_title.visibility = View.VISIBLE
            } else {
                tv_letter?.visibility = View.GONE
                line_view?.visibility = View.VISIBLE
                ll_top_title.visibility = View.GONE
            }
        }
    }

    fun compareSection(position: Int): Boolean {
        return if (position == 0) {
            false
        } else {
            val current = getSectionForPosition(position)
            val previous = getSectionForPosition(position - 1)
            current == previous
        }

    }

    // 获取当前位置的首字母(int表示ascii码)
    private fun getSectionForPosition(position: Int): Int {
        return mData!![position].letter[0].toInt()
    }

    // 获取字母首次出现的位置
    fun getPositionForSection(section: Int): Int {
        for (i in 0 until itemCount) {
            val s = mData!![i].letter
            val firstChar = s.toUpperCase()[0]
            if (firstChar.toInt() == section) {
                return i
            }
        }
        return -1
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initData(data: MutableList<Brand>?) {
        this.mData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandHolder {
        val view = View.inflate(parent.context, R.layout.layout_item_all_brand, null)
        return BrandHolder(view)
    }

    override fun onBindViewHolder(holder: BrandHolder, position: Int) {
        holder.bindData(position)
    }

    override fun getItemCount(): Int {
        return if (mData == null) 0 else mData!!.size
    }

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onClick(position: Int)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }
}