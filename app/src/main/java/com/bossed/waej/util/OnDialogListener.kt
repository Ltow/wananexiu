package com.bossed.waej.util

interface OnDialogListener {
    fun onConfirm()
    fun onCancel()

    class Builder : OnDialogListener {
        private lateinit var onConfirm: () -> Unit

        fun onConfirm(onConfirm: () -> Unit) {
            this.onConfirm = onConfirm
        }

        override fun onConfirm() {
            onConfirm.invoke()
        }

        private lateinit var onCancel: () -> Unit

        fun onCancel(onCancel: () -> Unit) {
            this.onCancel = onCancel
        }

        override fun onCancel() {
            onCancel.invoke()
        }
    }
}

