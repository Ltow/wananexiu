package com.bossed.waej.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.*
import com.blankj.utilcode.util.KeyboardUtils
import com.bossed.waej.R
import com.bossed.waej.adapter.*
import com.bossed.waej.customview.PopWindow
import com.bossed.waej.http.UrlConstants
import com.bossed.waej.javebean.*
import com.loper7.date_time_picker.DateTimeConfig
import com.loper7.date_time_picker.DateTimePicker
import kotlinx.android.synthetic.main.activity_car_wash.*
import kotlinx.android.synthetic.main.activity_new_customer.*
import kotlinx.android.synthetic.main.activity_new_jieche.*
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bossed.waej.customview.BottomDialog
import com.bossed.waej.customview.MyAlertDialog
import com.bossed.waej.customview.PopWindow.Companion.CENTER_BOTTOM
import com.bossed.waej.http.Api
import com.bossed.waej.http.BaseResourceObserver
import com.bossed.waej.http.UrlConstants.SupplierInfoUrl
import com.bossed.waej.ui.*
import com.luck.picture.lib.photoview.PhotoView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.activity_work_pic.*
import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class PopupWindowUtils private constructor() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: PopupWindowUtils? = null
            get() {
                if (field == null) instance = PopupWindowUtils()
                return field
            }

        @Synchronized
        fun get(): PopupWindowUtils {
            return instance!!
        }
    }

    private var popWindow: PopWindow? = null

    val showDoubtPop: (Activity, View, String) -> Unit = { context, view, msg ->
        popWindow = PopWindow.Builder(context).setView(R.layout.layout_doubt_pop)
            .setWidthAndHeight(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            ).setOutsideTouchable(true)
            .setChildrenView { contentView, pop ->
                val tv_content = contentView.findViewById<TextView>(R.id.tv_content)
                tv_content.text = msg
                when (msg) {
                    "深度保养" -> tv_content.setBackgroundResource(R.drawable.shape_corners_eb6100_dp5)
                    "常规保养" -> tv_content.setBackgroundResource(R.drawable.shape_corners_f39800_dp5)
                }
            }.create()
        popWindow!!.isClippingEnabled = false
        popWindow!!.isFocusable = true
        popWindow!!.showOnTargetBottom(view, CENTER_BOTTOM, 0, 0)
    }

    /**
     *带输入框pop
     */
    fun showEditPop(
        context: Context, parentView: View, title: String, content: String,
        hint: String, listener: (string: String) -> Unit
    ) {
        val popWindow =
            PopWindow.Builder(context).setView(R.layout.layout_pop_base_cofirm_edit)
                .setWidthAndHeight(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                )
                .setAnimStyle(R.style.CenterAnimation)
                .setOutsideTouchable(true)
                .setBackGroundLevel(0.5f)
                .setChildrenView { contentView, pop ->
                    var agentID = ""
                    contentView.findViewById<TextView>(R.id.tv_title).text = title
                    contentView.findViewById<TextView>(R.id.tv_content).text = content
                    val tv_name =
                        contentView.findViewById<TextView>(R.id.tv_name)
                    val tv_phone =
                        contentView.findViewById<TextView>(R.id.tv_phone)
                    val editText = contentView.findViewById<EditText>(R.id.et_content)
                    editText.hint = hint
                    if (content == "邀请码") {
                        editText.addTextChangedListener(object : TextChangedListener {
                            override fun afterTextChange(s: String) {
                                if (s.length == 6) {
                                    LoadingUtils.showLoading(context as Activity, "加载中...")
                                    Api.getInstance().getApiService().getAgentInfo(s)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(object : BaseResourceObserver<String>() {
                                            override fun onComplete() {
                                                LoadingUtils.dismissLoading()
                                            }

                                            override fun onSubscribe(d: Disposable) {
                                            }

                                            override fun onNext(s: String) {
                                                LogUtils.d("tag", s)
                                                val t =
                                                    GsonUtils.fromJson(s, AgentResponse::class.java)
                                                when (t.code) {
                                                    200 -> {
                                                        if (t.data != null) {
                                                            tv_name.visibility = View.VISIBLE
                                                            tv_phone.visibility = View.VISIBLE
                                                            tv_name.text =
                                                                "代理商：${t.data!!.agentName}"
                                                            tv_phone.text =
                                                                "电话：${t.data!!.phonenumber}"
                                                            agentID = t.data!!.id
                                                        }
                                                    }

                                                    401 -> showLoginOutTimePop(context)
                                                    else -> ToastUtils.showShort(t.msg)
                                                }
                                            }

                                            override fun onError(throwable: Throwable) {
                                                ToastUtils.showShort(throwable.message)
                                                LoadingUtils.dismissLoading()
                                            }
                                        })
                                } else {
                                    tv_name.visibility = View.GONE
                                    tv_phone.visibility = View.GONE
                                    agentID = ""
                                }
                            }
                        })
                    }
                    if (content == "兑换码") {
                        contentView.findViewById<TextView>(R.id.tv_confirm)!!.text = "兑换"
                    }
                    contentView.findViewById<TextView>(R.id.tv_confirm)!!
                        .setOnClickListener {
                            if (content == "邀请码")
                                if (TextUtils.isEmpty(agentID)) {
                                    ToastUtils.showShort("请输入正确的邀请码")
                                    return@setOnClickListener
                                }
                            listener.invoke(editText.text.toString())
                            pop.dismiss()
                        }
                    contentView.findViewById<TextView>(R.id.tv_cancel)!!
                        .setOnClickListener {
                            pop.dismiss()
                        }
                }.create()
        popWindow.isClippingEnabled = false
        popWindow.showAtLocation(parentView, Gravity.CENTER, 0, 0)
    }

    /**
     * 登陆超时pop（重新登录）
     */
    val showLoginOutTimePop: (Activity) -> Unit = {
        val dialog = MyAlertDialog(it, "登录超时，请重新登录")
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.setShowCancel(false)
        dialog.setOnDialogListener {
            onConfirm {
                it.startActivity(Intent(it, LoginActivity::class.java))
                it.finish()
            }
        }
        dialog.show()
    }

    /**
     * 没有取消按钮pop
     */
    fun showOnlyConfirmPop(it: Activity, title: String, msg: String, listener: () -> Unit) {
        val dialog = MyAlertDialog(it, msg)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.setShowCancel(false)
        dialog.setTitle(title)
        dialog.setOnDialogListener {
            onConfirm {
                listener.invoke()
                it.finish()
            }
        }
        dialog.show()
    }

    /**
     * 通用只回调确认pop
     */
    fun showConfirmPop(activity: Activity, message: String, listener: () -> Unit) {
        val dialog = MyAlertDialog(activity, message)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setOnDialogListener {
            onConfirm {
                listener.invoke()
                dialog.dismiss()
            }
            onCancel { dialog.dismiss() }
        }
        dialog.show()
    }

    /**
     * 可以设置确认按钮字体和颜色的提示框
     * @param confirmText 确认按钮内容
     * @param confirmTextColor 确认按钮文字颜色
     */
    fun showSetConfirmAlertDialog(
        context: Context,
        content: String,
        confirmText: String,
        confirmTextColor: String,
        listener: () -> Unit
    ) {
        val dialog = MyAlertDialog(context, content)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.setConfirm(confirmText)
        if (!TextUtils.isEmpty(confirmTextColor))
            dialog.setConfirmTextColor(confirmTextColor)
        dialog.setOnDialogListener {
            onConfirm {
                listener.invoke()
                dialog.dismiss()
            }
            onCancel {
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    /**
     * 通用带取消和确认回调pop
     * @param message 提示内容
     * @param listener 确认和取消回调
     */
    val showConfirmAndCancelPop: (Activity, String, OnConfirmAndCancelListener) -> Unit =
        { activity, message, listener ->
            val dialog = MyAlertDialog(activity, message)
            if (message.subSequence(0, 4) == "本次查询")
                dialog.setShowCTV(true)
            dialog.setCanceledOnTouchOutside(false)
            dialog.setOnDialogListener {
                onConfirm {
                    listener.onConfirm()
                    dialog.dismiss()
                }
                onCancel {
                    listener.onCancel()
                    dialog.dismiss()
                }
            }
            dialog.show()
        }

    /**
     * 新版本提示框
     */
    fun showNewVersionPop(
        content: String,
        forcedToup: Int,
        activity: Activity,
        listener: () -> Unit
    ) {
        val dialog = Dialog(activity, R.style.Dialog)
        val view = View.inflate(activity, R.layout.layout_pop_new_version, null)
        view.findViewById<TextView>(R.id.tv_content).text = content
        view.findViewById<View>(R.id.tv_down_now).setOnClickListener {
            listener.invoke()
            dialog.dismiss()
        }
        view.findViewById<View>(R.id.iv_close).setOnClickListener {
            if (forcedToup == 1) activity.finish()
            dialog.dismiss()
        }
        dialog.setContentView(view)
        if (forcedToup == 1) {
            dialog.setCanceledOnTouchOutside(false)
            dialog.setCancelable(false)
        }
        dialog.show()
    }

    /**
     * 未完工单据项目预览
     */
    fun showSelectedItemPop(
        mutableList: MutableList<ItemBean>,
        activity: Activity,
        itemTotal: String,
        receivable: String
    ) {
        showSelectedItemPop(mutableList, activity, itemTotal, "", "", "", receivable)
    }

    /**
     * 已完工项目预览
     */
    fun showSelectedItemPop(
        selected: MutableList<ItemBean>,
        activity: Activity,
        itemTotal: String,
        priceTotal: String,
        discount: String,
        payment: String,
        receivable: String
    ) {
        BottomDialog(activity).create(R.layout.layout_pop_selected_item)
            .setCanceledOnTouchOutside(true).setViewInterface { view, dialog ->
                val ll_bottom = view.findViewById<LinearLayout>(R.id.ll_bottom)
                if (BarUtils.isNavBarVisible(activity)) {
                    BarUtils.setNavBarLightMode(activity, true)
                    val layoutParams = ll_bottom.layoutParams as LinearLayout.LayoutParams
                    layoutParams.bottomMargin = BarUtils.getNavBarHeight()
                    ll_bottom.layoutParams = layoutParams
                }
                val recyclerView = view.findViewById<RecyclerView>(R.id.rv_selected_item)
                if (!TextUtils.isEmpty(discount)) {
                    view.findViewById<RelativeLayout>(R.id.rl_ys).visibility = View.VISIBLE
                    view.findViewById<RelativeLayout>(R.id.rl_ss).visibility = View.VISIBLE
                    view.findViewById<RelativeLayout>(R.id.rl_yh).visibility = View.VISIBLE
                    view.findViewById<TextView>(R.id.tv_receivable_name).text = "剩余应收"
                }
                val tv_total_pop = view.findViewById<TextView>(R.id.tv_total_pop)
                tv_total_pop.text = itemTotal
                val tv_payment = view.findViewById<TextView>(R.id.tv_payment)
                tv_payment.text = payment
                val tv_total_price_pop = view.findViewById<TextView>(R.id.tv_total_price_pop)
                tv_total_price_pop.text = priceTotal
                val tvDiscount = view.findViewById<TextView>(R.id.tv_discount)
                tvDiscount.text = discount
                val tvReceivable = view.findViewById<TextView>(R.id.tv_receivable)
                tvReceivable.text = receivable
                recyclerView.layoutManager = LinearLayoutManager(activity)
                itemSelectedAdapter = ItemSelectedAdapter(selected)
                itemSelectedAdapter.bindToRecyclerView(recyclerView)
                itemSelectedAdapter.emptyView =
                    activity.layoutInflater.inflate(R.layout.layout_empty_view, null)
                if (selected.size > 3) recyclerView.layoutParams.height =
                    ConvertUtils.dp2px(345f)
                view.findViewById<View>(R.id.tv_total_pop).setOnClickListener {
                    dialog.dismiss()
                }
            }.show()
    }

    private var kehuPop: PopupWindow? = null
    private var gongyingshangPop: PopupWindow? = null
    private var partPop: PopupWindow? = null
    private var partTypePop: PopupWindow? = null
    private var keHuAdapter: KeHuAdapter
    private val keHuBean = ArrayList<KeHuData>()
    private var checkItemAdapter: CheckItemAdapter
    private val items = ArrayList<CheckItemData>()
    private var checkPartAdapter: CheckPartAdapter
    private val parts = ArrayList<CheckPartData>()
    private var partTypeAdapter: PartTypeAdapter
    private val partTypes = ArrayList<PartTypeListRow>()
    private var itemTypeListAdapter: ItemTypeListAdapter
    private val itemTypes = ArrayList<TypeData>()
    private lateinit var itemSelectedAdapter: ItemSelectedAdapter
    private val supplierBean = ArrayList<SupplierInfoData>()
    private var supplierAdapter: SupplierAdapter

    init {
        keHuAdapter = KeHuAdapter(keHuBean)
        checkItemAdapter = CheckItemAdapter(items)
        checkPartAdapter = CheckPartAdapter(parts)
        partTypeAdapter = PartTypeAdapter(partTypes)
        itemTypeListAdapter = ItemTypeListAdapter(itemTypes)
        supplierAdapter = SupplierAdapter(supplierBean)
    }

    private var isCheckPart = true

    /**
     * 添加项目pop
     */
    val showAddPartPop = { activity: Activity,
                           view: View,
                           cardNo: String,
                           isEdit: Boolean,
                           itemBean: ItemBean,
                           listener: (
                               name: String,
                               type: String,
                               specs: String,
                               remark: String,
                               price: String,
                               num: String,
                               money: String,
                               cateId: Int,
                               cost: String
                           ) -> Unit ->
        popWindow = PopWindow.Builder(activity).setView(R.layout.layout_pop_base_add_part)
            .setWidthAndHeight(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            ).setOutsideTouchable(true).setAnimStyle(R.style.BottomAnimation)
            .setBackGroundLevel(0.5f)
            .setChildrenView { contentView, pop ->
                val ll_bottom = contentView.findViewById<LinearLayout>(R.id.ll_bottom)
                val layoutParams = ll_bottom.layoutParams as RelativeLayout.LayoutParams
                if (BarUtils.isNavBarVisible(activity)) {
                    BarUtils.setNavBarLightMode(activity, true)
                    layoutParams.bottomMargin = BarUtils.getNavBarHeight()
                    ll_bottom.layoutParams = layoutParams
                }
                SoftKeyBoardUtils.setOnKeyBoardChangeListener(activity,
                    object : SoftKeyBoardUtils.OnSoftKeyBoardChangeListener {
                        override fun keyBoardShow(height: Int) {
                            layoutParams.bottomMargin = height
                            ll_bottom.layoutParams = layoutParams
                        }

                        override fun keyBoardHide(height: Int) {
                            layoutParams.bottomMargin = if (BarUtils.isNavBarVisible(activity))
                                BarUtils.getNavBarHeight()
                            else
                                0
                            ll_bottom.layoutParams = layoutParams
                        }
                    })
                val name = contentView.findViewById<EditText>(R.id.et_name)//名称
                val type = contentView.findViewById<TextView>(R.id.tv_type)//类别
                val specs = contentView.findViewById<EditText>(R.id.et_specs)//规格
//                val oem = contentView.findViewById<EditText>(R.id.et_oem)//OE
                val cost = contentView.findViewById<EditText>(R.id.et_cost)
                val price = contentView.findViewById<EditText>(R.id.et_price)//单价
                val num = contentView.findViewById<EditText>(R.id.et_num)//数量
                val money = contentView.findViewById<TextView>(R.id.tv_money)//金额
                var cateId = -1
                if (isEdit) {
                    name.setText(itemBean.itemName)
                    type.text = itemBean.cateName
                    specs.setText(itemBean.attrName)
//                    oem.setText(itemBean.oem)
                    cost.setText(itemBean.serviceFee.toString())
                    price.setText(itemBean.unitPrice.toString())
                    num.setText(itemBean.num.toString())
                    money.text =
                        BigDecimal(itemBean.num).multiply(BigDecimal(itemBean.unitPrice))
                            .setScale(2, BigDecimal.ROUND_HALF_DOWN).toString()
                    cateId = itemBean.cateId
                }
                contentView.findViewById<View>(R.id.tv_confirm)
                    .setOnClickListener(object : OnNoRepeatClickListener {
                        override fun onRepeatClick(v: View?) {
                            if (DoubleClicksUtils.get().isFastDoubleClick)
                                return
                            if (TextUtils.isEmpty(name.text.toString())) return
                            listener.invoke(
                                name.text.toString(),
                                type.text.toString(),
                                specs.text.toString(),
//                                oem.text.toString(),
                                "",
                                if (TextUtils.isEmpty(price.text.toString())) "0" else price.text.toString(),
                                if (TextUtils.isEmpty(num.text.toString())) "1" else num.text.toString(),
                                if (TextUtils.isEmpty(money.text.toString())) "0" else money.text.toString(),
                                cateId,
                                if (TextUtils.isEmpty(cost.text.toString())) "0" else cost.text.toString()
                            )
                            KeyboardUtils.hideSoftInput(contentView)
                            pop.dismiss()
                        }
                    })
                type.setOnClickListener {
                    getPartTypeList(
                        activity, contentView.findViewById(R.id.ll_part_type)
                    ) {
                        type.text = it.name
                        cateId = it.id
                    }
                }
                contentView.findViewById<View>(R.id.tv_cancel).setOnClickListener {
                    pop.dismiss()
                }
                name.addTextChangedListener(object : TextChangedListener {
                    override fun afterTextChange(s: String) {
                        if (isCheckPart) checkPartsList(name, activity) {
                            isCheckPart = false
                            cateId = it.cateId
                            name.setText(it.name)
                            specs.setText(it.specName)
//                            oem.setText(it.oem)
                            num.setText("1.00")
                            if (it.serviceFee == 0f && it.marketPrice == 0f)
                                getItemHisPrice(it.name, activity, cardNo, price, cost, num)
                            else {
                                cost.setText(it.serviceFee.toString())
                                price.setText(it.marketPrice.toString())
                            }
                        }
                        else isCheckPart = true
                    }
                })
                price.addTextChangedListener(object : TextChangedListener {
                    @SuppressLint("SetTextI18n")
                    override fun afterTextChange(s: String) {
                        if (TextUtils.isEmpty(s) || TextUtils.isEmpty(num.text.toString())) return
                        money.text =
                            BigDecimal(price.text.toString()).multiply(BigDecimal(num.text.toString()))
                                .add(BigDecimal("0")).setScale(2, BigDecimal.ROUND_HALF_DOWN)
                                .toString()
                    }
                })
                num.addTextChangedListener(object : TextChangedListener {
                    @SuppressLint("SetTextI18n")
                    override fun afterTextChange(s: String) {
                        if (TextUtils.isEmpty(s) || TextUtils.isEmpty(price.text.toString())) return
                        money.text =
                            BigDecimal(num.text.toString()).multiply(BigDecimal(price.text.toString()))
                                .add(BigDecimal("0")).setScale(2, BigDecimal.ROUND_HALF_DOWN)
                                .toString()
                    }
                })
            }.create()
        popWindow!!.isClippingEnabled = false
        popWindow!!.showAtLocation(view, Gravity.BOTTOM, 0, 0)
    }

    /**
     * 选择人员pop
     */
    fun showSelectPersonPop(
        activity: Activity,
        title: String,
        listener: (ArrayList<PersonRow>) -> Unit
    ) {
        LoadingUtils.showLoading(activity, "加载中...")
        val params = HashMap<String, String>()
        params["searchValue"] = ""
        params["pageNum"] = ""
        params["pageSize"] = ""
        params["disableFlag"] = "0"
        RetrofitUtils.get().getJson(
            UrlConstants.PersonListUrl, params, activity,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, PersonResponse::class.java)
                    if (t.rows == null || t.rows.isEmpty()) {
                        showSetConfirmAlertDialog(
                            activity,
                            "员工档案为空，请添加后继续操作！",
                            "去添加",
                            ""
                        ) {
                            val intent = Intent(activity, PersonDataActivity::class.java)
                            intent.putExtra("personType", "new")
                            activity.startActivity(intent)
                        }
                    } else {
                        BottomDialog(activity).create(R.layout.layout_pop_select_personnel)
                            .setCanceledOnTouchOutside(true).setViewInterface { view, dialog ->
                                view.findViewById<TextView>(R.id.tv_title).text = title
                                val cancel = view.findViewById<TextView>(R.id.tv_cancel)
                                cancel.visibility = when (title) {
                                    "选择总负责人" -> View.GONE
                                    else -> View.VISIBLE
                                }
                                val recyclerView = view.findViewById<RecyclerView>(R.id.rv_proposal)
                                recyclerView.layoutManager = LinearLayoutManager(activity)
                                val dispatchAdapter = Dispatch2Adapter(t.rows as ArrayList)
                                dispatchAdapter.bindToRecyclerView(recyclerView)
                                dispatchAdapter.emptyView = activity.layoutInflater.inflate(
                                    R.layout.layout_empty_view,
                                    null
                                )
                                view.findViewById<TextView>(R.id.tv_confirm).setOnClickListener {
                                    val sel = ArrayList<PersonRow>()
                                    for (row: PersonRow in t.rows) {
                                        if (row.isSelect) sel.add(row)
                                    }
                                    if (sel.isEmpty()) return@setOnClickListener
                                    listener.invoke(sel)
                                    dialog.dismiss()
                                }
                                view.findViewById<TextView>(R.id.tv_new).setOnClickListener {
                                    val intent = Intent(activity, PersonDataActivity::class.java)
                                    intent.putExtra("personType", "new")
                                    activity.startActivity(intent)
                                    dialog.dismiss()
                                }
                                cancel.setOnClickListener {
                                    dialog.dismiss()
                                }
                            }.show()
                    }
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    /**
     *日期选择器
     * @param activity 上下文
     * @param showType 1:带时间 2:不带时间
     */
    fun showSelDateTimePop(activity: Activity, showType: Int, listener: (Long) -> Unit) {
        BottomDialog(activity).create(R.layout.layout_pop_datetime)
            .setCanceledOnTouchOutside(true)
            .setViewInterface { view, dialog ->
                val ll_bottom = view.findViewById<LinearLayout>(R.id.ll_bottom)
                if (BarUtils.isNavBarVisible(activity)) {
                    BarUtils.setNavBarLightMode(activity, true)
                    val layoutParams = ll_bottom.layoutParams as RelativeLayout.LayoutParams
                    layoutParams.bottomMargin = BarUtils.getNavBarHeight()
                    ll_bottom.layoutParams = layoutParams
                }
                val picker = view.findViewById<DateTimePicker>(R.id.date_picker)
                when (showType) {
                    1 -> picker.setDisplayType(
                        intArrayOf(
                            DateTimeConfig.YEAR,
                            DateTimeConfig.MONTH,
                            DateTimeConfig.DAY,
                            DateTimeConfig.HOUR,
                            DateTimeConfig.MIN,
                            DateTimeConfig.SECOND
                        )
                    )

                    2 -> picker.setDisplayType(
                        intArrayOf(
                            DateTimeConfig.YEAR, DateTimeConfig.MONTH, DateTimeConfig.DAY
                        )
                    )
                }
                picker.setTextColor(Color.parseColor("#333333"))
                picker.setTextSize(15, 18)
                picker.setGlobal(DateTimeConfig.GLOBAL_CHINA)
                view.findViewById<View>(R.id.tv_confirm).setOnClickListener {
                    listener.invoke(picker.getMillisecond())
                    dialog.dismiss()
                }
                view.findViewById<View>(R.id.tv_cancel).setOnClickListener {
                    dialog.dismiss()
                }
            }.show()
    }

    /**
     * 选择OE号
     */
    val showSelOEPop: (Activity, ArrayList<PartSearchDs1>, listener: (oe: String, price: String) -> Unit) -> Unit =
        { activity, oeList, listener ->
            BottomDialog(activity).create(R.layout.layout_pop_sel_oe)
                .setCanceledOnTouchOutside(false).setViewInterface { view, dialog ->
                    var oe = ""
                    val recyclerView = view.findViewById<RecyclerView>(R.id.rv_oe)
                    recyclerView.layoutManager = LinearLayoutManager(activity)
                    val adapter = SelOEPopAdapter(oeList)
                    adapter.bindToRecyclerView(recyclerView)
                    adapter.emptyView =
                        activity.layoutInflater.inflate(R.layout.layout_empty_view, null)
                    adapter.setOnItemClickListener { adapter, view, position ->
                        if (oeList[position].oeDetails != "1") {
                            ToastUtils.showShort("此条暂无详细信息")
                            return@setOnItemClickListener
                        }
                        oeList[position].isSel = !oeList[position].isSel
                        oe = oeList[position].oe
                        for (i: Int in oeList.indices) {
                            if (i != position)
                                oeList[i].isSel = false
                        }
                        adapter.setNewData(oeList)
                    }
                    view.findViewById<View>(R.id.tv_confirm).setOnClickListener {
                        getBalance(activity, object : OnConfirmAndCancelListener {
                            override fun onConfirm() {
                                getOEDetail(oe, activity, listener)
                                dialog.dismiss()
                            }

                            override fun onCancel() {
                            }
                        })
                    }
                    view.findViewById<View>(R.id.tv_cancel).setOnClickListener {
                        dialog.dismiss()
                    }
                }.show()
        }

    /**
     * 获取价格
     */
    private val getOEDetail: (String, Activity, listener: (oe: String, price: String) -> Unit) -> Unit =
        { oe, activity, listener ->
            LoadingUtils.showLoading(activity, "加载中...")
            val params = HashMap<String, String>()
            params["method"] = "GetBVData"
            params["bvdcUserID"] = SPUtils.getInstance().getString("bvdcUserID")
            params["inKind"] = "1101"
            params["inStr1"] = oe
            params["setName"] = getIMEI(activity)
            params["Czy"] = SPUtils.getInstance().getString("username")
            RetrofitUtils.get().getBVDCJson(UrlConstants.BvdcUrl,
                params,
                object : RetrofitUtils.OnCallBackListener {
                    override fun onSuccess(s: String) {
                        val t = GsonUtils.fromJson(s, OESearchResponse::class.java)
                        BottomDialog(activity).create(R.layout.layout_pop_sel_oe)
                            .setCanceledOnTouchOutside(false).setViewInterface { view, dialog ->
                                view.findViewById<TextView>(R.id.tv_title).text = "选择一个指导价"
                                view.findViewById<TextView>(R.id.tv_confirm).text = "确定"
                                val recyclerView = view.findViewById<RecyclerView>(R.id.rv_oe)
                                recyclerView.layoutManager = LinearLayoutManager(activity)
                                val adapter = SelOEPriceAdapter(t.result.ds1 as ArrayList)
                                adapter.bindToRecyclerView(recyclerView)
                                adapter.emptyView = activity.layoutInflater.inflate(
                                    R.layout.layout_empty_view,
                                    null
                                )
                                var price = ""
                                adapter.setOnItemClickListener { adapter, view, position ->
                                    t.result.ds1[position].isSel = !t.result.ds1[position].isSel
                                    price = t.result.ds1[position].指导价格
                                    for (i: Int in t.result.ds1.indices) {
                                        if (i != position)
                                            t.result.ds1[i].isSel = false
                                    }
                                    adapter.setNewData(t.result.ds1)
                                }
                                view.findViewById<View>(R.id.tv_confirm).setOnClickListener {
                                    listener.invoke(oe, price)
                                    dialog.dismiss()
                                }
                                view.findViewById<View>(R.id.tv_cancel).setOnClickListener {
                                    dialog.dismiss()
                                }
                            }.show()
                    }

                    override fun onFailed(e: String) {
                        ToastUtils.showShort(e)
                    }
                })
        }

    /**
     * 检测客户是否存在
     */
    fun checkKeHuList(editText: EditText, activity: Activity, listener: (KeHuData) -> Unit) {
        val params = HashMap<String, String>()
        params["search"] = editText.text.toString()
        RetrofitUtils.get().getJson(UrlConstants.CustomerInfoUrl, params, activity,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, KeHuBean::class.java)
                    if (t.data.isEmpty()) return
                    if (kehuPop == null) {
                        val contentView = View.inflate(
                            activity, R.layout.layout_pop_check_kehu, null
                        )
                        val lView = contentView.findViewById<RecyclerView>(R.id.rv_ke_hu_list)
                        lView.layoutManager = LinearLayoutManager(activity)
                        keHuAdapter.bindToRecyclerView(lView)
                        keHuAdapter.emptyView =
                            activity.layoutInflater.inflate(R.layout.layout_empty_view, null)
                        kehuPop = PopupWindow(
                            contentView,
                            editText.width + ConvertUtils.dp2px(25f),
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            true
                        )
                    }
                    keHuBean.clear()
                    keHuBean.addAll(t.data)
                    keHuAdapter.setNewData(keHuBean)
                    kehuPop!!.isOutsideTouchable = true
                    kehuPop!!.isFocusable = false
                    kehuPop!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    kehuPop!!.showAsDropDown(editText, 0, 0)
                    if (keHuBean.size >= 5) {
                        kehuPop!!.update(
                            editText.width + ConvertUtils.dp2px(25f),
                            ConvertUtils.dp2px(198f)
                        )
                    } else {
                        kehuPop!!.update(
                            editText.width + ConvertUtils.dp2px(25f),
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                    }
                    keHuAdapter.setOnItemClickListener { adapter, view, position ->
                        listener.invoke(keHuBean[position])
                        kehuPop!!.dismiss()
                    }
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    /**
     * 检索供应商
     */
    fun checkGongYingShangPop(
        editText: EditText,
        activity: Activity,
        listener: (SupplierInfoData) -> Unit
    ) {
        val params = HashMap<String, String>()
        params["search"] = editText.text.toString()
        RetrofitUtils.get().getJson(SupplierInfoUrl, params, activity,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    LogUtils.d("tag", s)
                    val t = GsonUtils.fromJson(s, SupplierInfoResponse::class.java)
                    if (t.data.isEmpty()) return
                    if (gongyingshangPop == null) {
                        val contentView = View.inflate(
                            activity, R.layout.layout_pop_check_kehu, null
                        )
                        val lView = contentView.findViewById<RecyclerView>(R.id.rv_ke_hu_list)
                        lView.layoutManager = LinearLayoutManager(activity)
                        supplierAdapter.bindToRecyclerView(lView)
                        supplierAdapter.emptyView =
                            activity.layoutInflater.inflate(R.layout.layout_empty_view, null)
                        gongyingshangPop = PopupWindow(
                            contentView,
                            editText.width,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            true
                        )
                    }
                    supplierBean.clear()
                    if (editText.id == R.id.et_gys) {
                        val bean = SupplierInfoData()
                        bean.name = "公司库存"
                        supplierBean.add(bean)
                    }
                    supplierBean.addAll(t.data)
                    supplierAdapter.setNewData(supplierBean)
                    gongyingshangPop!!.isOutsideTouchable = true
                    gongyingshangPop!!.isFocusable = false
                    gongyingshangPop!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    gongyingshangPop!!.showAsDropDown(editText, 0, 0)
                    if (supplierBean.size >= 5) {
                        gongyingshangPop!!.update(editText.width, ConvertUtils.dp2px(198f))
                    } else {
                        gongyingshangPop!!.update(
                            editText.width, ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                    }
                    supplierAdapter.setOnItemClickListener { adapter, view, position ->
                        listener.invoke(supplierBean[position])
                        gongyingshangPop!!.dismiss()
                    }
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    /**
     * 检测配件是否存在
     */
    val checkPartsList =
        { editText: EditText, activity: Activity, listener: (CheckPartData) -> Unit ->
            val params = HashMap<String, String>()
            params["search"] = editText.text.toString()
            RetrofitUtils.get().getJson(UrlConstants.CheckPartsUrl, params, activity,
                object : RetrofitUtils.OnCallBackListener {
                    override fun onSuccess(s: String) {
                        val t = GsonUtils.fromJson(s, CheckPartBean::class.java)
                        if (t?.data == null) return
                        if (partPop == null) {
                            val contentView = View.inflate(
                                activity, R.layout.layout_pop_check_kehu, null
                            )
                            val lView = contentView.findViewById<RecyclerView>(R.id.rv_ke_hu_list)
                            lView.layoutManager = LinearLayoutManager(activity)
                            checkPartAdapter.bindToRecyclerView(lView)
                            partPop = PopupWindow(
                                contentView,
                                editText.width,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                true
                            )
                        }
                        parts.clear()
                        parts.addAll(t.data)
                        checkPartAdapter.setNewData(parts)
                        partPop!!.isOutsideTouchable = true
                        partPop!!.isFocusable = false
                        partPop!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        if (parts.isNotEmpty()) partPop!!.showAsDropDown(editText, 0, 0)
                        else partPop!!.dismiss()
                        if (parts.size >= 5) {
                            partPop!!.update(editText.width, ConvertUtils.dp2px(198f))
                        } else if (parts.size > 0) {
                            partPop!!.update(
                                editText.width, ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                        }
                        checkPartAdapter.setOnItemClickListener { adapter, view, position ->
                            listener.invoke(parts[position])
                            partPop!!.dismiss()
                        }
                    }

                    override fun onFailed(e: String) {
                        ToastUtils.showShort(e)
                    }
                })
        }

    /**
     * 获取配件类别列表
     */
    private fun getPartTypeList(
        activity: Activity,
        view: View,
        listener: (PartTypeListRow) -> Unit
    ) {
        LoadingUtils.showLoading(activity, "加载中...")
        val params = HashMap<String, String>()
        RetrofitUtils.get().getJson(UrlConstants.PartTypeListUrl, params, activity,
            object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, PartTypeListBean::class.java)
                    if (t.rows == null) return
                    if (partTypePop == null) {
                        val contentView = View.inflate(
                            activity, R.layout.layout_pop_check_kehu, null
                        )
                        val lView = contentView.findViewById<RecyclerView>(R.id.rv_ke_hu_list)
                        lView.layoutManager = LinearLayoutManager(activity)
                        partTypeAdapter.bindToRecyclerView(lView)
                        partTypePop = PopupWindow(
                            contentView, view.width, ViewGroup.LayoutParams.WRAP_CONTENT, true
                        )
                    }
                    partTypes.clear()
                    partTypes.addAll(t.rows)
                    partTypeAdapter.setNewData(partTypes)
                    partTypePop!!.isOutsideTouchable = true
                    partTypePop!!.isFocusable = false
                    partTypePop!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    partTypePop!!.showAsDropDown(view, 0, 0)
                    if (partTypes.size >= 5) {
                        partTypePop!!.update(view.width, ConvertUtils.dp2px(285f))
                    } else {
                        partTypePop!!.update(
                            view.width, ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                    }
                    partTypeAdapter.setOnItemClickListener { adapter, view, position ->
                        listener.invoke(partTypes[position])
                        partTypePop!!.dismiss()
                    }
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    /**
     * 获取BVDC余额
     */
    val getBalance: (Activity, OnConfirmAndCancelListener) -> Unit = { activity, listener ->
        LoadingUtils.showLoading(activity, "加载中...")
        val params = HashMap<String, String>()
        params["method"] = "GetBVData"
        params["bvdcUserID"] = SPUtils.getInstance().getString("bvdcUserID")
        params["inKind"] = "2201"
        params["inKind2"] = "2"
        params["instr1"] = "cCardnumber"
        RetrofitUtils.get()
            .getBVDCJson(UrlConstants.BvdcUrl, params, object : RetrofitUtils.OnCallBackListener {
                override fun onSuccess(s: String) {
                    val t = GsonUtils.fromJson(s, BalanceResponse::class.java)
                    if (t.code == "1") {
                        val balance =
                            t.result.ds1[0].充值金额剩余.toFloat() + t.result.ds1[0].免费金额剩余.toFloat()
                        if (TextUtils.isEmpty(t.result.ds1[0].车型解析))
                            if (balance <= 0.0) showSetConfirmAlertDialog(
                                activity,
                                "本次查询需支付0.2元，将从余额扣除,当前余额${balance}元，余额不足，请及时充值。",
                                "充值",
                                ""
                            ) {
                                activity.startActivity(
                                    Intent(
                                        activity,
                                        RechargeActivity::class.java
                                    )
                                )
                            }
                            else {
                                if (SPUtils.getInstance()
                                        .getLong("today", 0) - TimeUtils.getNowMills() > 0
                                )
                                    listener.onConfirm()
                                else
                                    showConfirmAndCancelPop(
                                        activity,
                                        "本次查询需支付0.2元，将从余额扣除,当前余额${balance}元，扣除后剩余${
                                            NumberFormatUtils.getFloatTwoDigits(((balance - 0.2).toFloat()))
                                        }元，未查询到数据不扣除费用。",
                                        listener
                                    )
                            }
                        else
                            listener.onConfirm()
                    } else ToastUtils.showShort(t.message)
                }

                override fun onFailed(e: String) {
                    ToastUtils.showShort(e)
                }
            })
    }

    /**
     * 获取项目最近一次历史价格
     */
    private val getItemHisPrice: (String, Activity, String, EditText, EditText, EditText) -> Unit =
        { itemName, activity, cardNo, price, serviceFee, num ->
            val params = HashMap<String, String>()
            params["cardNo"] = cardNo
            params["itemName"] = itemName
            RetrofitUtils.get().getJson(UrlConstants.ItemHistoryPriceUrl, params, activity,
                object : RetrofitUtils.OnCallBackListener {
                    override fun onSuccess(s: String) {
                        val t = GsonUtils.fromJson(s, ItemHisPriceResponse::class.java)
                        price.setText(t.data.unitPrice.toString())
                        serviceFee.setText(t.data.serviceFee.toString())
                        num.setText(t.data.num.toString())
                    }

                    override fun onFailed(e: String) {
                        price.setText("0")
                        serviceFee.setText("0")
                        num.setText("1.0")
                    }
                })
        }

    /**
     * 查看大图pop
     */
    fun showPhotoPop(url: String, context: Context, view: View) {
        val popWindow = PopWindow.Builder(context).setView(R.layout.layout_pop_pic)
            .setWidthAndHeight(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            ).setOutsideTouchable(true)
            .setBackGroundLevel(0.5f)
            .setAnimStyle(R.style.CenterAnimation)
            .setChildrenView { contentView, pop ->
                val photoView = contentView.findViewById<PhotoView>(R.id.pv_pic)
                GlideUtils.get().loadHead(context, url, photoView)
                photoView.setOnClickListener {
                    pop.dismiss()
                }
            }.create()
        popWindow.isClippingEnabled = false
        popWindow.isFocusable = true
        popWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
    }

    /**
     * 开通站点提示
     */
    fun showBuySiteAlert(context: Context, view: View) {
        val popWindow = PopWindow.Builder(context).setView(R.layout.layout_pop_alert_account)
            .setWidthAndHeight(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            ).setOutsideTouchable(true)
            .setAnimStyle(R.style.CenterAnimation)
            .setChildrenView { contentView, pop ->
                contentView.setOnClickListener {
                    pop.dismiss()
                }
            }.create()
        popWindow.isClippingEnabled = false
        popWindow.isFocusable = true
        popWindow.showOnTargetTop(
            view,
            PopWindow.CENTER_TOP,
            ConvertUtils.dp2px(-25f),
            ConvertUtils.dp2px(5f)
        )
    }

    /**
     * 展示html pop
     */
    @SuppressLint("SetJavaScriptEnabled")
    fun showHtmlPop(url: String, context: Context, view: View) {
        val popWindow = PopWindow.Builder(context).setView(R.layout.layout_pop_html)
            .setWidthAndHeight(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            ).setOutsideTouchable(true)
            .setBackGroundLevel(0.5f)
            .setChildrenView { contentView, pop ->
                val webView = contentView.findViewById<WebView>(R.id.webView)
                webView.settings.javaScriptEnabled = true
                webView.settings.allowFileAccess = true
                webView.loadUrl(url)
                contentView.findViewById<View>(R.id.tv_cancel).setOnClickListener {
                    pop.dismiss()
                }
            }.create()
        popWindow.isClippingEnabled = false
        popWindow.isFocusable = true
        popWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
    }

    /**
     * 选择银行账号pop
     */
    fun showSelAccountPop(context: Context, listener: (row: BankAccountRow) -> Unit) {
        val params = HashMap<String, String>()
        params["status"] = "1"
        RetrofitUtils.get()
            .getJson(
                UrlConstants.SettleAccountListUrl, params, context as Activity,
                object : RetrofitUtils.OnCallBackListener {
                    override fun onSuccess(s: String) {
                        val t = GsonUtils.fromJson(s, BankAccountResponse::class.java)
                        if (t.rows == null || t.rows.isEmpty()) {
                            showSetConfirmAlertDialog(
                                context,
                                "银行账号为空，请添加后继续操作！",
                                "去添加",
                                ""
                            ) {
                                val intent = Intent(context, BankAccountUpholdActivity::class.java)
                                intent.putExtra("type", "2")
                                context.startActivity(intent)
                            }
                        } else {
                            val list = ArrayList<SelAccountBean>()
                            val listWX = ArrayList<BankAccountRow>()
                            val listZFB = ArrayList<BankAccountRow>()
                            val listYHK = ArrayList<BankAccountRow>()
                            val listXJ = ArrayList<BankAccountRow>()
                            for (row: BankAccountRow in t.rows) {
                                when (row.methodName) {
                                    "微信" -> listWX.add(row)
                                    "支付宝" -> listZFB.add(row)
                                    "银行卡" -> listYHK.add(row)
                                    "现金" -> listXJ.add(row)
                                }
                            }
                            if (listWX.isNotEmpty())
                                list.add(SelAccountBean("微信", listWX))
                            if (listZFB.isNotEmpty())
                                list.add(SelAccountBean("支付宝", listZFB))
                            if (listYHK.isNotEmpty())
                                list.add(SelAccountBean("银行卡", listYHK))
                            if (listXJ.isNotEmpty())
                                list.add(SelAccountBean("现金", listXJ))
                            val adapter = SelAccountAdapter(list)
                            BottomDialog(context).create(R.layout.layout_pop_sel_account)
                                .setCanceledOnTouchOutside(true)
                                .setViewInterface { view, dialog ->
                                    val ll_bottom =
                                        view.findViewById<RelativeLayout>(R.id.rl_bottom)
                                    if (BarUtils.isNavBarVisible(context)) {
                                        BarUtils.setNavBarLightMode(context, true)
                                        val layoutParams =
                                            ll_bottom.layoutParams as RelativeLayout.LayoutParams
                                        layoutParams.bottomMargin = BarUtils.getNavBarHeight()
                                        ll_bottom.layoutParams = layoutParams
                                    }
                                    val recyclerView =
                                        view.findViewById<RecyclerView>(R.id.rv_sel_account)
                                    recyclerView.layoutManager = LinearLayoutManager(context)
                                    if (t.rows.size > 5) {
                                        val layoutParams =
                                            recyclerView.layoutParams as RelativeLayout.LayoutParams
                                        layoutParams.height = ConvertUtils.dp2px(330f)
                                        recyclerView.layoutParams = layoutParams
                                    }
                                    adapter.bindToRecyclerView(recyclerView)
                                    adapter.setOnItemClickListener {
                                        listener.invoke(it)
                                        dialog.dismiss()
                                    }
                                    view.findViewById<View>(R.id.tv_cancel).setOnClickListener {
                                        dialog.dismiss()
                                    }
                                }.show()
                        }
                    }

                    override fun onFailed(e: String) {
                        ToastUtils.showShort(e)
                    }
                })
    }

    /**
     * 选择费用类别
     */
    fun showSelCostTypePop(context: Context, listener: (row: CostTypeRow) -> Unit) {
        LoadingUtils.showLoading(context as Activity, "加载中...")
        val params = HashMap<String, String>()
        params["status"] = "1"
        RetrofitUtils.get()
            .getJson(
                UrlConstants.CateTypeListUrl, params, context,
                object : RetrofitUtils.OnCallBackListener {
                    override fun onSuccess(s: String) {
                        val t = GsonUtils.fromJson(s, CostTypeResponse::class.java)
                        if (t.rows == null || t.rows.isEmpty()) {
                            showSetConfirmAlertDialog(
                                context,
                                "费用类别为空，请添加后继续操作!",
                                "去添加",
                                ""
                            ) {
                                context.startActivity(Intent(context, CostTypeActivity::class.java))
                            }
                        } else {
                            BottomDialog(context).create(R.layout.layout_pop_sel_account)
                                .setCanceledOnTouchOutside(true)
                                .setViewInterface { view, dialog ->
                                    view.findViewById<TextView>(R.id.tv_title).text = "选择费用类别"
                                    val ll_bottom =
                                        view.findViewById<RelativeLayout>(R.id.rl_bottom)
                                    if (BarUtils.isNavBarVisible(context)) {
                                        BarUtils.setNavBarLightMode(context, true)
                                        val layoutParams =
                                            ll_bottom.layoutParams as RelativeLayout.LayoutParams
                                        layoutParams.bottomMargin = BarUtils.getNavBarHeight()
                                        ll_bottom.layoutParams = layoutParams
                                    }
                                    val recyclerView =
                                        view.findViewById<RecyclerView>(R.id.rv_sel_account)
                                    recyclerView.layoutManager = LinearLayoutManager(context)
                                    val adapter = SelCostTypeAdapter(t.rows as ArrayList)
                                    adapter.bindToRecyclerView(recyclerView)
                                    adapter.setOnItemClickListener { adapter, view, position ->
                                        listener.invoke(t.rows[position])
                                        dialog.dismiss()
                                    }
                                    view.findViewById<View>(R.id.tv_cancel).setOnClickListener {
                                        dialog.dismiss()
                                    }
                                }.show()
                        }
                    }

                    override fun onFailed(e: String) {
                        ToastUtils.showShort(e)
                    }
                })
    }

    interface OnConfirmAndCancelListener {
        fun onConfirm()
        fun onCancel()
    }

    interface OnSelectedPersonnelListener {
        fun onConfirm(arr: MutableList<PersonRow>)
        fun onCancel()
    }

    fun dismiss() {
        if (popWindow != null && popWindow!!.isShowing)
            popWindow!!.dismiss()
        if (kehuPop != null)
            kehuPop!!.dismiss()
        if (gongyingshangPop != null)
            gongyingshangPop!!.dismiss()
    }

    fun getPopWindow(): PopWindow? {
        return if (popWindow != null) popWindow!! else null
    }

    fun isShowing(): Boolean {
        return if (popWindow != null) popWindow!!.isShowing
        else false
    }

    @SuppressLint("HardwareIds", "MissingPermission")
    private fun getIMEI(context: Context): String {
        val tm = context.getSystemService(AppCompatActivity.TELEPHONY_SERVICE) as TelephonyManager
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> Settings.System.getString(
                context.contentResolver, Settings.Secure.ANDROID_ID
            )

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                tm.imei
            }

            else -> {
                tm.deviceId
            }
        }
    }

}