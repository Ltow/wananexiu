package com.bossed.waej.javebean

import com.contrarywind.interfaces.IPickerViewData
import com.google.gson.annotations.SerializedName

data class CitiesBean(
    @SerializedName("cities")
    var cities: List<City>,
)

data class City(
    @SerializedName("children")
    var children: List<Children>,
    @SerializedName("label")
    var label: String?,
    @SerializedName("value")
    var value: String?
) : IPickerViewData {
    override fun getPickerViewText(): String {
        return label!!
    }
}

data class Children(
    @SerializedName("children")
    var children: List<ChildrenX>?,
    @SerializedName("label")
    var label: String?,
    @SerializedName("value")
    var value: String?
) : IPickerViewData {
    override fun getPickerViewText(): String {
        return label!!
    }

}

data class ChildrenX(
    @SerializedName("label")
    var label: String?,
    @SerializedName("value")
    var value: String?
) : IPickerViewData {
    override fun getPickerViewText(): String {
        return label!!
    }

}