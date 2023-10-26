package com.bossed.waej.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils
import com.bossed.waej.R
import com.bossed.waej.javebean.BookDs1
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class BookItemAdapter(data: MutableList<String>) :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.layout_item_book_item, data) {
    private val allData = ArrayList<BookDs1>()

    fun setAllData(data: ArrayList<BookDs1>) {
        allData.clear()
        allData.addAll(data)
    }

    override fun convert(helper: BaseViewHolder, item: String) {
        val recyclerView = helper.getView<RecyclerView>(R.id.rv_item)
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        val arrayList = ArrayList<String>()
        arrayList.add(item)
        for (i: Int in allData.indices) {
            if (i>0) {
                arrayList.add("")
                if (allData[i].mustList != "") {
                    val must = allData[i].mustList.split(",")
                    for (s: String in must) {
                        if (s == item) {
                            arrayList.removeAt(i)
                            arrayList.add("更换")
                            break
                        }
                    }
                } else {
                    if (allData[i].checkList != "") {
                        val check = allData[i].checkList.split(",")
                        for (s: String in check) {
                            if (s == item) {
                                arrayList.removeAt(i)
                                arrayList.add("检查")
                                break
                            }
                        }
                    }
                }
            }
        }
        val adapter = BookItem2Adapter(arrayList)
        adapter.bindToRecyclerView(recyclerView)
    }
}