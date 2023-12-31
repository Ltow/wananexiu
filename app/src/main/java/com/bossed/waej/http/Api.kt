package com.bossed.waej.http

import android.util.Log
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.Proxy
import java.util.concurrent.TimeUnit

class Api {
    companion object {
        @Volatile
        private var instance: Api? = null
        private var service: ApiService? = null

        //访问超时时间
        private const val TIMEOUT: Long = 60

        fun getInstance(): Api {
            return instance ?: synchronized(this) {
                instance ?: Api().also { instance = it }
            }
        }

        /**
         * 自定义TypeAdapter ,null对象将被解析成空字符串
         */
        val STRING: TypeAdapter<String?> = object : TypeAdapter<String?>() {
            override fun read(reader: JsonReader): String? {
                try {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull()
                        // 原先是返回null，这里改为返回空字符串
                        return ""
                    }
                    return reader.nextString()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return ""
            }

            override fun write(
                writer: JsonWriter,
                value: String?
            ) {
                try {
                    if (value == null) {
                        writer.nullValue()
                        return
                    }
                    writer.value(value)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun getApiService(): ApiService {
        val gson = GsonBuilder() //配置你的Gson
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .registerTypeHierarchyAdapter(String::class.java, STRING) //设置解析的时候null转成""
            .create()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(UrlConstants.BASE_URL) //添加转换器支持返回结果为String类型
            //.baseUrl("http://192.168.8.67:8080/") //添加转换器支持返回结果为String类型
            .addConverterFactory(StringConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(getOkHttpClient()) //添加rxjava2适配器
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        service = retrofit.create(ApiService::class.java)
        return service!!
    }

    /**
     * 获取OkHttpClient实例
     *
     * @return
     */
    private fun getOkHttpClient(): OkHttpClient {
        // Retrofit是基于OkHttpClient的，可以创建一个OkHttpClient进行一些配置
        return OkHttpClient.Builder() //打印接口信息，方便接口调试
            .addInterceptor(HttpLoggingInterceptor { message ->
                Log.e(
                    "TAG",
                    "log: $message"
                )
            }.setLevel(HttpLoggingInterceptor.Level.BASIC))
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(AddInterceptor())//添加header
            .addInterceptor(ReceivedCookiesInterceptor())//拦截
            .proxy(Proxy.NO_PROXY)
            .build()
    }

    /**
     * 添加header
     */
    private class AddInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val authorization: String = SPUtils.getInstance().getString("authorization")
            val builder: Request.Builder = chain.request().newBuilder()
            if (authorization != null && "" != authorization) {
                builder.addHeader("authorization", authorization)
            }
            return chain.proceed(builder.build())
        }
    }

    /**
     * 拦截header
     */
    private class ReceivedCookiesInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalResponse = chain.proceed(chain.request())
            val authorization = originalResponse.header("authorization")
            LogUtils.d("authorization", authorization)
            if (authorization != null && "" != authorization) {
                SPUtils.getInstance().put("authorization", authorization)
            }
            return originalResponse
        }
    }

}