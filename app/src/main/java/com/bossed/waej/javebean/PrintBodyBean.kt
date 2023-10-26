package com.bossed.waej.javebean

open class PrintBodyBean(
    var itemName: String,
    var num: String,
    var price: String,
    var money: String,
    var serviceFee: String
) {
    constructor() : this("", "", "", "", "")

}