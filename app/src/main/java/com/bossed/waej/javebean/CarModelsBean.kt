package com.bossed.waej.javebean

data class CarModelsBean(
    val code: Int,
    val `data`: List<CarModelData>,
    val msg: String
)

data class CarModelData(
    val `data`: ArrayList<CarModelDataX>,
    val name: String
)

data class CarModelDataX(
    val `data`: ArrayList<DataXX>,
    val name: String
)

data class DataXX(
    val `data`: ArrayList<DataXXX>,
    val name: String
)

data class DataXXX(
    val `data`: ArrayList<DataXXXX>,
    val name: String
)

data class DataXXXX(
    val `data`: ArrayList<DataXXXXX>,
    val name: String,
    val logo: String,
    val brandId:Int
)

data class DataXXXXX(
    val brand: String,
    val displacement: String,
    val id: Int,
    val mjsid: String,
    val modelYear: String,
    val name: String,
    val nameSu: String,
    val nameTag: Any,
    val subBrand: String,
    val vehicleGroup: String,
    val vehicleSys: String
)