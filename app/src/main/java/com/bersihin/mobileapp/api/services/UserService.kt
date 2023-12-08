package com.bersihin.mobileapp.api.services

import com.bersihin.mobileapp.api.SuccessResponse
import com.bersihin.mobileapp.models.Report
import retrofit2.http.GET

interface UserService {
    companion object {
        const val PATH = "submission"
    }

    @GET("${PATH}/self")
    suspend fun getSelfSubmission(): SuccessResponse<List<Report>>
}