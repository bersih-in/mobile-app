package com.bersihin.mobileapp.utils

enum class UserRole(val role: String) {
    WORKER("WORKER"),
    USER("USER")
}

enum class ReportStatus(val status: String) {
    PENDING("PENDING"),
    VERIFIED("VERIFIED"),
    REJECTED_BY_SYSTEM("REJECTED_BY_ML"),
    REJECTED_BY_ADMIN("REJECTED_BY_ADMIN"),
    REJECTED_BY_WORKER("REJECTED_BY_WORKER"),
    IN_PROGRESS("IN_PROGRESS"),
    FINISHED("FINISHED")
}