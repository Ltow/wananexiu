package com.bossed.waej.util.wxpay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.blankj.utilcode.util.LogUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信业务层
 *
 * @author liaozan8888@163.com
 */
public class WeChatService {

    //APPID
    public static final String APP_ID = "wx5a0bba3d04b4e7a0";

    // IWXAPI 是第三方app和微信通信的openapi接口
    private final IWXAPI iwxapi;

    private static WeChatService instance;

    /**
     * 实例化
     */
    public static WeChatService GetInstance(Context context) {
        if (instance == null) {
            instance = new WeChatService(context);
        }
        return instance;
    }

    /**
     * 初始化
     */
    private WeChatService(Context context) {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        iwxapi = WXAPIFactory.createWXAPI(context, APP_ID, false);
        iwxapi.registerApp(APP_ID);// 将该app注册到微信
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // 将该app注册到微信
                iwxapi.registerApp(APP_ID);
            }
        }, new IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP));
    }

    /**
     * 微信登陆
     */
    public void openWeChatLogin() {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk";
        //调起微信接口
        iwxapi.sendReq(req);
    }

    /**
     * 调起微信支付
     *
     * @param appId
     * @param partnerId
     * @param prepayId
     * @param nonceStr
     * @param timeStamp
     * @param sign
     */
    public void openWeChatPay(String appId, String partnerId, String prepayId, String nonceStr, String timeStamp, String sign) {
        //支付请求类
        PayReq request = new PayReq();
        request.appId = appId;
        request.partnerId = partnerId;
        request.prepayId = prepayId;
        request.packageValue = "Sign=WXPay";
        request.nonceStr = nonceStr;
        request.timeStamp = timeStamp;
        request.sign = sign;
        //调起微信支付
        iwxapi.sendReq(request);
    }

    /**
     * 调起微信分享 （h5链接分享）
     *
     * @param title
     * @param description
     * @param scene       分享场景 Session微信好友  Timeline朋友圈
     */
    public void openWeChatShare(String title, String description, String scene) {
        //初始化一个WXWebpageObject，填写url
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://cms.iclickdesign.com/shareWx.html";
        //用 WXWebpageObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;
        //链接压缩图
        //Bitmap thumbBmp = BitmapFactory.decodeResource(getResources(), R.drawable.send_music_thumb);
        //msg.thumbData =Util.bmpToByteArray(thumbBmp, true);
        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = msg;
        //分享场景
        if (scene.equals("Session")) {
            req.scene = SendMessageToWX.Req.WXSceneSession;
        } else if (scene.equals("Timeline")) {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        }
        //调用 api 接口，发送数据到微信
        iwxapi.sendReq(req);
    }
}
