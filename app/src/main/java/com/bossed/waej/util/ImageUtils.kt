package com.bossed.waej.util

import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.IOException

class ImageUtils {
    companion object {
        val WeChatBitmapTo20K: (Bitmap) -> Bitmap = {
            // 把压缩后的数据存放到baos中
            val output = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.JPEG, 100, output)
            val zoom =
                Math.sqrt((100 * 1024 / output.toByteArray().size.toFloat()).toDouble())
                    .toFloat() //获取缩放比例
            // 设置矩阵数据
            val matrix = Matrix()
            matrix.setScale(zoom, zoom)
            // 根据矩阵数据进行新bitmap的创建
            var resultBitmap = Bitmap.createBitmap(it, 0, 0, it.width, it.height, matrix, true)
            output.reset()
            resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output)
            // 如果进行了上面的压缩后，依旧大于32K，就进行小范围的微调压缩
            while (output.toByteArray().size > 50 * 1024) {
                matrix.setScale(0.9f, 0.9f) //每次缩小 1/10
                resultBitmap = Bitmap.createBitmap(
                    resultBitmap, 0, 0,
                    resultBitmap.width, resultBitmap.height, matrix, true
                )
                output.reset()
                resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output)
            }
            resultBitmap
        }

        /**
         * 将图片转换成Base64编码的字符串
         */
        val bitmapToBase64: (Bitmap) -> String = {
            var result = ""
            var baos: ByteArrayOutputStream? = null
            try {
                if (it != null) {
                    baos = ByteArrayOutputStream()
                    it.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    baos.flush()
                    baos.close()
                    val bitmapBytes = baos.toByteArray()
                    result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    if (baos != null) {
                        baos.flush()
                        baos.close()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            result
        }
    }
}