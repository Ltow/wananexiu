package com.bossed.waej.base

interface BaseContract {
    interface BaseMvpPresenter {
        fun attachView(view: BaseView)
        fun detachView()
        fun cancelAll()
    }

    interface BaseView {
        /**
         * @param flag 用于标记对应接口
         * @param e 错误信息
         */
        fun showError( e: String)
    }



}