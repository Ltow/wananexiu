package com.bossed.waej.util;


import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;


import com.blankj.utilcode.util.SPUtils;
import com.bossed.waej.javebean.PrintBodyBean;
import com.bossed.waej.javebean.PrintSettingBean;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PrintFormat {
    private Context mContext;

    private final PrintSettings printSettings;//打印设置对象
    private final OutputStream outputStream;
    //总计
    private String zonge;
    //打印字段
    private List<PrintSettingBean> mPrintBodySettings = new ArrayList<>();
    private List<PrintSettingBean> mPrintHeadSettings;
    private final List<PrintBodyBean> mPrintBodyDatas;
    private List<PrintSettingBean> mPrintFootSettings;
    private final List<PrintSettingBean> mPrintTotalSettings;

    private byte[] columnType;
    private byte[] footType;
    private String divider;

    /**
     * 五列制表--58mm 32字节 80mm 48字节
     */
    static final byte[] TABLE_5_58 = new byte[]{(byte) 10, (byte) 16, (byte) 22, 28, 0};
    static final byte[] TABLE_5_80 = new byte[]{(byte) 18, (byte) 24, (byte) 32, 40, 0};
    /**
     * 四列制表
     */
    static final byte[] TABLE_4_58 = new byte[]{(byte) 14, (byte) 20, (byte) 26, 0};
    static final byte[] TABLE_4_80 = new byte[]{(byte) 15, (byte) 26, (byte) 37, 0};
    /**
     * 三列制表
     */
    static final byte[] TABLE_3_58 = new byte[]{(byte) 15, (byte) 25, 0};
    static final byte[] TABLE_3_80 = new byte[]{(byte) 22, (byte) 37, 0};
    /**
     * 两列制表
     */
    static final byte[] TABLE_2_58 = new byte[]{(byte) 15, 0};
    static final byte[] TABLE_2_80 = new byte[]{(byte) 18, (byte) 35, 0};

    public PrintFormat(Context context, BluetoothSocket mBluetoothSocket, List<PrintSettingBean> printHeadSettings,
                       List<PrintSettingBean> printBodySettings, List<PrintSettingBean> printFootSettings, List<PrintBodyBean> printBodyDatas,
                       List<PrintSettingBean> printTotalSettings) throws IOException {
        mContext = context;
        switch (SPUtils.getInstance().getString("PRINTER_NORMS", States.PrinterPageSize.PRINTER_58mm)) {
            case States.PrinterPageSize.PRINTER_58mm:
                columnType = TABLE_4_58;
                footType = TABLE_2_58;
                divider = "--------------------------------";
                break;
            case States.PrinterPageSize.PRINTER_80mm:
                columnType = TABLE_5_80;
                footType = TABLE_2_80;
                divider = "------------------------------------------------";
                break;
        }
        mPrintHeadSettings = printHeadSettings;
        mPrintBodyDatas = printBodyDatas;
        mPrintFootSettings = printFootSettings;
        mPrintTotalSettings = printTotalSettings;
        outputStream = mBluetoothSocket.getOutputStream();
        printSettings = new PrintSettings(outputStream);
        printSettings.initPrinter();
        initData(printBodySettings);
    }

    private void initData(List<PrintSettingBean> printBodySettings) {
        mPrintBodySettings = getPrintField(printBodySettings);
        mPrintHeadSettings = getPrintField(mPrintHeadSettings);
        mPrintFootSettings = getPrintField(mPrintFootSettings);
        for (int i = 0; i < mPrintTotalSettings.size(); i++) {
            PrintSettingBean printTotalSetting = mPrintTotalSettings.get(i);
            if (printTotalSetting.getText().equals("合计"))
                zonge = printTotalSetting.getContent();
        }
    }

    /**
     * 得到需要显示的字段
     *
     * @param settings
     * @return
     */
    public static List<PrintSettingBean> getPrintField(List<PrintSettingBean> settings) {
        List<PrintSettingBean> printSettings = new ArrayList<>();
        for (int i = 0; i < settings.size(); i++) {
            if (settings.get(i).isChecked()) {
                printSettings.add(settings.get(i));
            }
        }
        return printSettings;
    }

    public void print() throws IOException {
        head();
    }

    private void head() throws IOException {
        printSettings.initPrinter();
        printSettings.setLineGap(0x21);
        printSettings.setAlignPosition(1);
        printSettings.setBigSize(0);
        printSettings.printText("****" + SPUtils.getInstance().getString("shopName") + "****");
        printSettings.nextLine();
        printSettings.nextLine();
        printSettings.nextLine();
        printSettings.nextLine();
        for (int i = 0; i < mPrintHeadSettings.size(); i++) {
            PrintSettingBean mPrintHeadSetting = mPrintHeadSettings.get(i);
            if (mPrintHeadSetting.getText().equals("表头")) {
                printSettings.setAlignPosition(1);
                printSettings.setBigSize(1);
                printSettings.setLineGap(0x41);
                printSettings.printText(mPrintHeadSetting.getContent());
                printSettings.nextLine();
                printSettings.nextLine();
                printSettings.setBigSize(0);
                printSettings.setAlignPosition(0);
                printSettings.setLineGap(0x21);
            }
//            else if (mPrintHeadSetting.getText().equals("图片")) {
//                Bitmap bitmap = SharedSetting.getLogo();
//                if (bitmap != null)
//                    printSettings.draw2PxPoint(compressPic(SharedSetting.getLogo()));
//            }
            else if (mPrintHeadSetting.getText().equals("二维码")) {
                printSettings.nextLine();
                if (!TextUtils.isEmpty(mPrintHeadSetting.getContent())) {
//                    printSettings.draw2PxPoint(compressPic(generateBitmap(mPrintHeadSetting.getContent(), 230, 230, 0xffffffff, 0xff000000)));
//                    printSettings.draw2PxPoint(compressPic(syncEncodeQRCode(mPrintHeadSetting.getContent(), 350, 0xff000000)));
                }
            } else if (!mPrintHeadSetting.getText().equals("表头") && mPrintHeadSetting.isChecked()) {
                printSettings.printText(mPrintHeadSetting.getText() + "：" + mPrintHeadSetting.getContent());
                printSettings.nextLine();
            }
        }
        body();
    }

    private void body() throws IOException {
        printSettings.printText(divider);
        outputStream.write("\r\n".getBytes("GBK"));
        printSettings.printTable(columnType);
        printSettings.printText("服务项目");
        printSettings.printHT();
        printSettings.printText("数量");
        printSettings.printHT();
        printSettings.printText("单价");
        printSettings.printHT();
        printSettings.printText("金额");
        if (columnType == TABLE_5_80) {
            printSettings.printHT();
            printSettings.printText("工时费");
        }
        outputStream.write("\r\n".getBytes("GBK"));
        printSettings.printText(divider);
        outputStream.write("\r\n".getBytes("GBK"));
        BigDecimal total = new BigDecimal("0.0");
        BigDecimal fee = new BigDecimal("0.0");
        for (int i = 0; i < mPrintBodyDatas.size(); i++) {
            PrintBodyBean mPrintBodyData = mPrintBodyDatas.get(i);
            fee = fee.add(new BigDecimal(mPrintBodyData.getServiceFee())).setScale(2, BigDecimal.ROUND_HALF_DOWN);//工时费合计
            total = total.add(new BigDecimal(mPrintBodyData.getPrice()).multiply(new BigDecimal(mPrintBodyData.getNum()))).setScale(2, BigDecimal.ROUND_HALF_DOWN);
            printSettings.printTable(columnType);
            printSettings.printText(mPrintBodyData.getItemName());//名称
            if (columnType == TABLE_4_58 && mPrintBodyData.getItemName().length() > 7) {
                printSettings.nextLine();
            }
            if (columnType == TABLE_5_80 && mPrintBodyData.getItemName().length() > 9) {
                printSettings.nextLine();
            }
            printSettings.printHT();
            printSettings.printText(mPrintBodyData.getNum());//数量
            printSettings.printHT();
            printSettings.printText(mPrintBodyData.getPrice());//单价
            printSettings.printHT();
            printSettings.printText(new BigDecimal(mPrintBodyData.getPrice()).multiply(new BigDecimal(mPrintBodyData.getNum())).setScale(2, BigDecimal.ROUND_HALF_DOWN).toString());//金额
            if (columnType == TABLE_5_80) {
                printSettings.printHT();
                printSettings.printText(mPrintBodyData.getServiceFee());//工时费
            }
        }
        printSettings.nextLine();
        if (columnType == TABLE_4_58) {
            printSettings.setAlignPosition(2);
            printSettings.printText("小计：" + total);
            printSettings.nextLine();
            printSettings.printText("总工时费：" + fee);
            printSettings.nextLine();
//            outputStream.write(("小计：" + total).getBytes("GBK"));
//            outputStream.write("\r\n".getBytes("GBK"));
//            outputStream.write(("总工时费：" + fee).getBytes("GBK"));
//            outputStream.write("\r\n".getBytes("GBK"));
        }
        printSettings.printText(divider);
        printSettings.nextLine();
        total();
    }

    private void total() throws IOException {
        if (footType == TABLE_2_58)
            total_58();
        else
            total_80();
    }

    private void total_80() throws IOException {
        printSettings.printTable(TABLE_2_80);
        printSettings.printHT();
        printSettings.setAlignPosition(2);
        printSettings.printText("合计：" + new BigDecimal(zonge).setScale(2, BigDecimal.ROUND_HALF_DOWN));
        printSettings.nextLine();
//        outputStream.write(("合计：" + new BigDecimal(zonge).setScale(2)).getBytes("GBK"));
//        outputStream.write("\r\n".getBytes("GBK"));
        printSettings.printText(divider);
        foot();
    }


    private void total_58() throws IOException {
        printSettings.printTable(TABLE_2_58);
        printSettings.printHT();
        printSettings.setAlignPosition(2);
        printSettings.printText("合计：" + new BigDecimal(zonge).setScale(2, BigDecimal.ROUND_HALF_DOWN));
        printSettings.nextLine();
//        outputStream.write(("合计：" + new BigDecimal(zonge).setScale(2)).getBytes("GBK"));
//        outputStream.write("\r\n".getBytes("GBK"));
        printSettings.printText(divider);
        foot();
    }

    private void foot() throws IOException {
        printSettings.setLineGap(0x21);
        printSettings.setAlignPosition(0);
        for (int i = 0; i < mPrintFootSettings.size(); i++) {
            PrintSettingBean printFootSetting = mPrintFootSettings.get(i);
            if (printFootSetting.getText().equals("备注")) {
                printSettings.setAlignPosition(1);
                outputStream.write(printFootSetting.getContent().getBytes("GBK"));
                outputStream.write("\r\n".getBytes("GBK"));
                printSettings.setAlignPosition(0);
            } else if (printFootSetting.getText().equals("二维码")) {
                printSettings.setAlignPosition(1);
                printSettings.nextLine();
                if (printFootSetting.getContent() != null && !printFootSetting.getContent().equals("") && printFootSetting.getContent().length() > 0) {
                }
//                    printSettings.draw2PxPoint(compressPic(generateBitmap(printFootSetting.getContent(), 230, 230, 0xffffffff, 0xff000000)));
//                    printSettings.draw2PxPoint(compressPic(syncEncodeQRCode(printFootSetting.getContent(), 350, 0xff000000)));
            }
//            else if (printFootSetting.getText().equals("图片")) {
//                Bitmap bitmap = SharedSetting.getLogo();
//                if (bitmap != null)
//                    printSettings.draw2PxPoint(compressPic(SharedSetting.getLogo()));
//            }
            else if (!printFootSetting.getText().equals("备注") && printFootSetting.isChecked()) {
                outputStream.write((printFootSetting.getText() + ":" + printFootSetting.getContent()).getBytes("GBK"));
                outputStream.write("\r\n".getBytes("GBK"));
            }
        }
        printSettings.nextLine();
        printSettings.nextLine();
        printSettings.nextLine();
        stubLine();
    }

    private void stubLine() throws IOException {
        printSettings.setLineGap(0x21);
        printSettings.setAlignPosition(1);
        printSettings.printText("多谢惠顾，欢迎下次光临！");
        printSettings.nextLine();
        printSettings.printText("****" + SPUtils.getInstance().getString("shopName") + "****");
        printSettings.nextLine();
        outputStream.write("\r\n".getBytes("GBK"));
        printSettings.nextLine();
        outputStream.write("\r\n".getBytes("GBK"));
        printSettings.nextLine();
        outputStream.write("\r\n".getBytes("GBK"));
        printSettings.nextLine();
        outputStream.write("\r\n".getBytes("GBK"));
    }


}
