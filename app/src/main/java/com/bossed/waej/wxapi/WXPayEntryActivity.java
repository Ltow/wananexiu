package com.bossed.waej.wxapi;

import android.app.Activity;
import android.os.Bundle;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bossed.waej.javebean.EBWxPayBean;
import com.bossed.waej.util.wxpay.WeChatService;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

/**
 * 微信回调监听
 *
 * @author liaozan8888@163.com
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private final String Tag = "WXPayEntryActivity";

    //实例化业务层
    private WeChatService wechatService = WeChatService.GetInstance(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // IWXAPI 是第三方app和微信通信的openapi接口
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(this, WeChatService.APP_ID);
        iwxapi.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        LogUtils.d(baseReq.getType());
        switch (baseReq.getType()) {

        }
    }

    //第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp baseResp) {
        LogUtils.d(baseResp.getType());
        switch (baseResp.getType()) {
            case ConstantsAPI.COMMAND_PAY_BY_WX: //支付回调
                if (baseResp.errCode == 0) {
                    EventBus.getDefault().post(new EBWxPayBean(true));
                }
                break;
            case ConstantsAPI.COMMAND_JOINT_PAY:
                break;
        }
        finish();
    }
}
