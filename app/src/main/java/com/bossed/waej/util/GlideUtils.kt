package com.bossed.waej.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import androidx.core.content.FileProvider
import com.blankj.utilcode.util.LogUtils
import com.bossed.waej.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import okio.buffer
import okio.sink
import okio.source
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class GlideUtils private constructor() {
    companion object {
        private var currentFile: File? = null
        private var instance: GlideUtils? = null
            get() {
                if (field == null)
                    instance = GlideUtils()
                return field
            }

        @Synchronized
        fun get(): GlideUtils {
            return instance!!
        }
    }

    interface ImageDownLoadCallBack {
        fun onDownLoadSuccess(file: File)
        fun onDownLoadFailed()
    }

    /**
     * 下载图片
     */
    fun startDownload(context: Context, url: String, callBack: ImageDownLoadCallBack) {
        var bitmap: Bitmap? = null
        try {
//            file = Glide.with(context)
//                    .load(url)
//                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                    .get();
            bitmap = Glide.with(context)
                .asBitmap()
                .load(url)
                .into(
                    com.bumptech.glide.request.target.Target.SIZE_ORIGINAL,
                    com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
                )
                .get()
            bitmap?.let { saveImageToGallery(context, it) }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
//            if (file != null) {
//                callBack.onDownLoadSuccess(file);
//            } else {
//                callBack.onDownLoadFailed();
//            }
            if (bitmap != null && currentFile!!.exists()) {
                callBack.onDownLoadSuccess(currentFile!!)
            } else {
                callBack.onDownLoadFailed()
            }
        }
    }

    /**
     * 保存图片到本地相册  /storage/emulated/0/Android/data/com.bossed.waej/files/Documents/万鞍E修/1697607035224.jpeg
     */
    private fun saveImageToGallery(context: Context, bmp: Bitmap) {
        // 首先保存图片
        val file =
            if (Build.VERSION.SDK_INT > 29)
                File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!.absolutePath)
            else
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absoluteFile //注意小米手机必须这样获得public绝对路径
        var fileName = "万鞍E修"
        val appDir = File(file, fileName)
        if (!appDir.exists()) {
            appDir.mkdirs()
        }
        fileName = System.currentTimeMillis().toString() + ".jpg"
        currentFile = File(appDir, fileName)
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(currentFile)
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            LogUtils.e(e.message)
        } finally {
            try {
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        // 最后通知图库更新
        if (Build.VERSION.SDK_INT > 29) {
            saveToGallery(currentFile!!, context, fileName)
        } else
            context.sendBroadcast(
                Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(File(currentFile!!.absolutePath))
                )
            )
    }

    /**
     * android 10以后用此方法刷新相册
     */
    private fun saveToGallery(photoFile: File, context: Context, displayName: String) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > 29) {
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        }
        val contentResolver = context.contentResolver
        val collectionUri = if (Build.VERSION.SDK_INT > 29) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        val imageUri = contentResolver.insert(collectionUri, contentValues)
        imageUri?.let { uri ->
            try {
                contentResolver.openOutputStream(uri)?.use { outputStream ->
                    val inputStream = FileInputStream(photoFile)
                    inputStream.copyTo(outputStream)
                    inputStream.close()
                    outputStream.close()
                }
                if (Build.VERSION.SDK_INT > 29) {
                    contentValues.clear()
                    contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                    contentResolver.update(uri, contentValues, null, null)
                }
            } catch (e: IOException) {
                LogUtils.e("tag", e.message)
            }
        }
    }

    /**
     * 加载车型图标
     */
    fun loadCarLogo(context: Context, url: String, imageView: ImageView) {
        Glide.with(context).clear(imageView)
        val options: RequestOptions = RequestOptions()
            .placeholder(R.mipmap.icon_car_logo) //图片加载出来前，显示的图片
            .fallback(R.mipmap.icon_car_logo) //url为空的时候,显示的图片
            .error(R.mipmap.icon_car_logo) //图片加载失败后，显示的图片
        Glide.with(context).load(url).apply(options).into(imageView)
    }

    /**
     * 加载项目图标
     */
    fun loadItemPic(context: Context, url: String, imageView: ImageView) {
        Glide.with(context).clear(imageView)
        val options: RequestOptions = RequestOptions()
            .placeholder(R.mipmap.icon521) //图片加载出来前，显示的图片
            .fallback(R.mipmap.icon52) //url为空的时候,显示的图片
            .error(R.mipmap.icon52) //图片加载失败后，显示的图片
        Glide.with(context).load(url).apply(options).into(imageView)
    }

    /**
     * 加载营业执照
     */
    fun loadLicensePic(context: Context, url: String, imageView: ImageView) {
        Glide.with(context).clear(imageView)
        val options: RequestOptions = RequestOptions()
            .placeholder(R.mipmap.icon2) //图片加载出来前，显示的图片
            .fallback(R.mipmap.icon2) //url为空的时候,显示的图片
            .error(R.mipmap.icon2) //图片加载失败后，显示的图片
        Glide.with(context).load(url).apply(options).into(imageView)
    }

    /**
     * 加载门头照
     */
    fun loadDoorTitlePic(context: Context, url: String, imageView: ImageView) {
        Glide.with(context).clear(imageView)
        val options: RequestOptions = RequestOptions()
            .placeholder(R.mipmap.icon676) //图片加载出来前，显示的图片
            .fallback(R.mipmap.icon676) //url为空的时候,显示的图片
            .error(R.mipmap.icon676) //图片加载失败后，显示的图片
        Glide.with(context).load(url).apply(options).into(imageView)
    }

    /**
     * 员工头像
     */
    fun loadHead(context: Context, url: String, imageView: ImageView) {
        Glide.with(context).clear(imageView)
        val options: RequestOptions = RequestOptions()
            .placeholder(R.mipmap.icon_head1) //图片加载出来前，显示的图片
            .fallback(R.mipmap.icon_head1) //url为空的时候,显示的图片
            .error(R.mipmap.icon_head1) //图片加载失败后，显示的图片
        Glide.with(context).load(url).apply(options).into(imageView)
    }

    /**
     * 加载门店照
     */
    fun loadShopPic(context: Context, url: String, imageView: ImageView) {
        Glide.with(context).clear(imageView)
        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.shape_corners_f5f5f5_dp0) //图片加载出来前，显示的图片
            .fallback(R.drawable.shape_corners_f5f5f5_dp0) //url为空的时候,显示的图片
            .error(R.drawable.shape_corners_f5f5f5_dp0) //图片加载失败后，显示的图片
        Glide.with(context).load(url).apply(options).into(imageView)
    }

    /**
     * 加载门头Logo
     */
    fun loadShopLogo(context: Context, url: String, imageView: ImageView) {
        Glide.with(context).clear(imageView)
        val options: RequestOptions = RequestOptions()
            .placeholder(R.mipmap.ic_launcher) //图片加载出来前，显示的图片
            .fallback(R.mipmap.ic_launcher) //url为空的时候,显示的图片
            .error(R.mipmap.ic_launcher) //图片加载失败后，显示的图片
        Glide.with(context).load(url).apply(options).into(imageView)
    }

    /**
     * 商品logo
     */
    fun loadGoodLogo(context: Context, url: String, imageView: ImageView) {
        Glide.with(context).clear(imageView)
        val options: RequestOptions = RequestOptions()
            .placeholder(R.mipmap.icon_support_item) //图片加载出来前，显示的图片
            .fallback(R.mipmap.icon_support_item) //url为空的时候,显示的图片
            .error(R.mipmap.icon_support_item) //图片加载失败后，显示的图片
        Glide.with(context).load(url).apply(options).into(imageView)
    }


}