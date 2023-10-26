package com.bossed.waej.http

interface RetrofitCallback<T> {
    fun onSuccess(t: T)
    fun onError(errorStr: String)
}