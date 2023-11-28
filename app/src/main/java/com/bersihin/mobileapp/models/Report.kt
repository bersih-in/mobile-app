package com.bersihin.mobileapp.models

data class Report(
    val id: String,
    val title: String,
    val description: String,
    val pictureUrl: String,
    val latitude: Double,
    val longitude: Double,
    val status: ReportStatus,
    val statusReason: String?,
    val finishedBy: String?
)