package com.bossed.waej.util

import android.content.Context
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class GetJsonDataUtil {
    companion object {
        fun getJson(context: Context, fileName: String): String {
            val stringBuilder = StringBuilder()
            //获得assets资源管理器
            val assetManager = context.assets
            //使用IO流读取json文件内容
            try {
                val bufferedReader = BufferedReader(
                    InputStreamReader(assetManager.open(fileName), "utf-8")
                )
                var line: String?
                while (bufferedReader.readLine().also { line = it } != null) {
                    stringBuilder.append(line)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return stringBuilder.toString()
        }
    }
}