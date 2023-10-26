package com.bossed.waej.http

import com.bossed.waej.base.BaseResponse
import com.bossed.waej.javebean.*
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    /**
     * 统一post Json
     */
    @POST
    fun postJson(@Url url: String, @Body body: RequestBody): Observable<String>

    /**
     * 统一put Json
     */
    @PUT
    fun putJson(@Url url: String, @Body body: RequestBody): Observable<String>

    @PUT(UrlConstants.ContractPostUrl)
    fun putContract(@Path("id") id: Int): Observable<String>

    /**
     * 注册
     */
    @POST(UrlConstants.RegisterUrl)
    fun register(@Body body: RequestBody): Observable<BaseResponse>

    /**
     * 接车单详情
     */
    @GET(UrlConstants.JieCheMsgUrl)
    fun getJieCheMsg(
        @Path("id") id: Int
    ): Observable<JieCheResponse>

    @GET
    fun get(@Url url: String, @QueryMap map: Map<String, String>): Observable<String>

    @POST
    fun post(@Url url: String, @QueryMap map: Map<String, String>): Observable<String>

    /**
     * 项目分类列表
     */
    @GET(UrlConstants.ItemTypeListUrl)
    fun getItemTypeList(): Observable<SelectItemTypeBean>

    /**
     * 项目列表
     */
    @GET(UrlConstants.ItemListUrl)
    fun getItemList(
        @Query("cateId") cateId: Int,
        @Query("status") status: Int,
        @Query("name") name: String,
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: String,
    ): Observable<SelectItemBean>

    @GET(UrlConstants.ItemsHistoryPriceUrl)
    fun getHisItemList(
        @Query("cardNo") cardNo: String,
        @Query("itemNames") itemNames: ArrayList<String>,
    ): Observable<String>

    /**
     * 项目列表--会员卡
     */
    @GET(UrlConstants.ItemListUrl)
    fun getVipCardItemList(@Query("searchValue") searchValue: String): Observable<SelectItemBean>

    /**
     * 项目列表--常用项目
     */
    @GET(UrlConstants.ItemListUrl)
    fun getCommonItemList(@Query("isHot") isHot: Int): Observable<SelectItemBean>

    /**
     * 店铺列表
     */
    @GET(UrlConstants.ShopListUrl)
    fun getShopList(): Observable<MemberShopBean>

    /**
     * 工单列表
     */
    @GET(UrlConstants.OrderListUrl)
    fun getOrderList(
        @Query("searchValue") searchValue: String,
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: String,
        @Query("orderStatus") orderStatus: Int,
        @Query("orderType") orderType: Int,
    ): Observable<OrderListBean>

    /**
     * 员工列表
     */
    @GET(UrlConstants.PersonListUrl)
    fun getPersonList(
        @Query("searchValue") searchValue: String,
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: String,
    ): Observable<PersonResponse>

    /**
     * 供应商列表
     */
    @GET(UrlConstants.SupplierUrl)
    fun getSupplierList(
        @Query("searchValue") searchValue: String,
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: String,
    ): Observable<String>

    /**
     * 好友列表
     */
    @GET(UrlConstants.SupplierListUrl)
    fun getSupplierList(@Query("name") name: String): Observable<SelectFriendsBean>

    /**
     * 品牌
     */
    @GET(UrlConstants.BrandUrl)
    fun getBrandList(@Query("name") name: String): Observable<CarBrandBean>


    /**
     * 车型信息
     */
    @GET(UrlConstants.CarModelMsgUrl)
    fun getCarModelMsgList(@Query("brandName") brandName: String): Observable<CarModelsBean>

    /**
     *询价详情
     */
    @GET(UrlConstants.InquiryMsgUrl)
    fun getInquiryMsg(
        @Path("id") id: Int
    ): Observable<OrderMsgBean>

    /**
     * 工单详情--按id
     */
    @GET(UrlConstants.OrderMsgUrl)
    fun getOrderMsg(
        @Path("id") id: Int
    ): Observable<String>

    /**
     * 线上订单详情
     */
    @GET(UrlConstants.OnlineOrderInfoUrl)
    fun getOnlineOrderMsg(
        @Path("id") id: Int
    ): Observable<String>

    /**
     * 工单详情--按单号
     */
    @GET(UrlConstants.OrderMsgFrSnUrl)
    fun getOrderMsgFrSn(
        @Path("id") id: String
    ): Observable<String>

    /**
     * 员工详情
     */
    @GET(UrlConstants.PersonMsgUrl)
    fun getPersonMsg(
        @Path("id") id: Int
    ): Observable<PersonMsgBean>

    /**
     * 消息详情
     */
    @GET(UrlConstants.NoticeMsgUrl)
    fun getNoticeMsg(
        @Path("noticeId") noticeId: Int
    ): Observable<NoticeMsgBean>

    /**
     * 供应商详情
     */
    @GET(UrlConstants.SupplierMsgUrl)
    fun getSupplierMsg(
        @Path("id") id: Int
    ): Observable<SupplierMsgBean>

    /**
     * 会员卡详情
     */
    @GET(UrlConstants.VipCardMsgUrl)
    fun getVipCardMsg(
        @Path("id") id: Int
    ): Observable<VipCardMsgBean>

    /**
     * 店铺详细信息
     */
    @GET(UrlConstants.ShopMsgUrl)
    fun getShopMsg(
        @Path("id") id: Int
    ): Observable<ShopMsgBean>

    /**
     * 项目详情
     */
    @GET(UrlConstants.ItemMsgUrl)
    fun getItemMsg(
        @Path("id") id: Int
    ): Observable<ItemMsgBean>

    /**
     * 客户详情
     */
    @GET(UrlConstants.CustomerMsgUrl)
    fun getCustomerMsg(
        @Path("id") id: Int
    ): Observable<CustomerMsgBean>

    /**
     * 角色详情
     */
    @GET(UrlConstants.RoleInfoUrl)
    fun getRoleInfo(
        @Path("roleId") id: Int
    ): Observable<String>

    /**
     * 进货单详情--按id
     */
    @GET(UrlConstants.PurchaseInfoUrl)
    fun getPurchaseInfo(
        @Path("id") id: Int
    ): Observable<String>

    /**
     * 获取代理商信息
     */
    @GET(UrlConstants.CheckAgentUrl)
    fun getAgentInfo(
        @Path("code") code: String
    ): Observable<String>

    /**
     * 使用兑换码
     */
    @GET(UrlConstants.ExchangeUrl)
    fun getExchange(
        @Path("code") code: String
    ): Observable<String>

    /**
     * 进货单详情--按单号
     */
    @GET(UrlConstants.PurchaseInfoFrSnUrl)
    fun getPurchaseInfoFrSn(
        @Path("id") id: String
    ): Observable<String>

    /**
     * 收款单详情
     */
    @GET(UrlConstants.ReceivableOrderInfoUrl)
    fun getReceivableOrderInfo(
        @Path("id") id: Int
    ): Observable<String>

    /**
     * 收款单详情
     */
    @GET(UrlConstants.PaymentHisInfoUrl)
    fun getPaymentOrderInfo(
        @Path("id") id: Int
    ): Observable<String>

    /**
     * 配件明细
     */
    @GET(UrlConstants.PartInfoUrl)
    fun getPartInfo(
        @Path("id") id: Int
    ): Observable<String>

    /**
     * 库存明细
     */
    @GET(UrlConstants.StockInfoUrl)
    fun getStockInfo(
        @Path("id") id: Int
    ): Observable<String>

    /**
     * 盘点明细
     */
    @GET(UrlConstants.CheckInfoUrl)
    fun getCheckInfo(
        @Path("id") id: Int
    ): Observable<String>

    /**
     * 预约详情
     */
    @GET(UrlConstants.RecordInfoUrl)
    fun getRecordInfo(
        @Path("id") id: Int
    ): Observable<String>

    /**
     * 店铺信息
     */
    @GET(UrlConstants.ShopInfoUrl)
    fun getShopInfo(
        @Path("id") id: Int
    ): Observable<String>

    /**
     * 商品信息
     */
    @GET(UrlConstants.GoodsInfoUrl)
    fun getGoodsInfo(
        @Path("id") id: Int
    ): Observable<String>

    /**
     * 商户信息
     */
    @GET(UrlConstants.MerchantInfoUrl)
    fun getMerchantInfo(
        @Path("id") id: Int
    ): Observable<String>

    /**
     * 商户银行卡信息
     */
    @GET(UrlConstants.BankCardInfoUrl)
    fun getBankCardInfo(
        @Path("id") id: Int
    ): Observable<String>

    /**
     * 自营项目信息
     */
    @GET(UrlConstants.FreeItemInfoUrl)
    fun getSelfPackageInfo(
        @Path("id") id: Int
    ): Observable<String>

    /**
     * 退货可收款列表
     */
    @GET(UrlConstants.BackCollectionUrl)
    fun getBackCollectionList(
        @Path("supplierId") id: Int
    ): Observable<String>

    /**
     * 提现详情
     */
    @GET(UrlConstants.WithdrawalInfoUrl)
    fun getWithdrawalInfo(
        @Path("id") id: Int
    ): Observable<String>

    /**
     * 待收款单列表
     */
    @GET(UrlConstants.ReceivableOrderUrl)
    fun getReceivableOrder(
        @Path("customerId") id: Int
    ): Observable<String>

    /**
     * 待付款单列表
     */
    @GET(UrlConstants.PaymentOrderUrl)
    fun getPaymentOrder(
        @Path("supplierId") id: Int
    ): Observable<String>

    /**
     * 消息列表
     */
    @GET(UrlConstants.NoticeListUrl)
    fun getNoticeList(
        @Query("noticeTitle") noticeTitle: String, @Query("noticeType") noticeType: String
    ): Observable<NoticeListBean>

    /**
     * 会员卡列表
     */
    @GET(UrlConstants.VipCardListUrl)
    fun getVipCardList(@Query("status") status: Int): Observable<VipManageBean>

    /**
     * 记事本列表
     */
    @GET(UrlConstants.NotepadListUrl)
    fun getNotepadList(
        @Query("userId") userId: Int,
        @Query("isAsc") isAsc: String
    ): Observable<NotepadBean>

    /**
     * 删除工单
     */
    @DELETE(UrlConstants.DeleteOrder)
    fun deleteOrder(@Path("ids") id: Int): Observable<BaseResponse>

    /**
     * 删除备忘录
     */
    @DELETE(UrlConstants.DeleteNotepadUrl)
    fun deleteNotepad(@Path("ids") id: Int): Observable<BaseResponse>

    /**
     * 删除客户
     */
    @DELETE(UrlConstants.DeleteCustomer)
    fun deleteCustomer(@Path("ids") id: Int): Observable<BaseResponse>

    /**
     * 删除员工
     */
    @DELETE(UrlConstants.DeletePersonUrl)
    fun deletePerson(@Path("ids") id: Int): Observable<BaseResponse>

    /**
     * 删除部门
     */
    @DELETE(UrlConstants.DeleteDepartUrl)
    fun deleteDepart(@Path("deptId") id: Int): Observable<BaseResponse>

    /**
     * 角色
     */
    @DELETE(UrlConstants.DeleteRoleUrl)
    fun deleteRole(@Path("roleIds") id: Int): Observable<BaseResponse>

    /**
     * 施工图
     */
    @DELETE(UrlConstants.DeleteWorkPicUrl)
    fun deletePic(@Path("ids") id: Int): Observable<BaseResponse>

    /**
     * 删除商品分类
     */
    @DELETE(UrlConstants.DeleteGoodsSortUrl)
    fun deleteGoodsSort(@Path("ids") id: Int): Observable<BaseResponse>

    /**
     * 删除项目分类
     */
    @DELETE(UrlConstants.DeleteSelfPackageUrl)
    fun deleteSelfPackage(@Path("ids") id: Int): Observable<BaseResponse>

    /**
     * 删除项目分类
     */
    @DELETE(UrlConstants.DeleteBankCardUrl)
    fun deleteBankCard(@Path("ids") id: Int): Observable<BaseResponse>


}