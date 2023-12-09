package com.bersihin.mobileapp.models

import com.bersihin.mobileapp.utils.ReportStatus
import com.google.gson.annotations.SerializedName

data class Report(
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("imageUrl")
    val imageUrl: String,

    @field:SerializedName("lat")
    val latitude: Double,

    @field:SerializedName("lon")
    val longitude: Double,

    @field:SerializedName("status")
    val status: ReportStatus,

    @field:SerializedName("statusReason")
    val statusReason: String? = "",

    @field:SerializedName("distance")
    val distance: Double? = 0.0,

    @field:SerializedName("workedBy")
    val workedBy: String? = ""
)