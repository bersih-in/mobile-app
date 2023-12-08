package com.bersihin.mobileapp.repository

import android.util.Log
import com.bersihin.mobileapp.api.ApiConfig
import com.bersihin.mobileapp.api.Response
import com.bersihin.mobileapp.api.services.UserService
import com.bersihin.mobileapp.models.Report
import retrofit2.HttpException
import java.io.IOException

class UserRepository(
    private val service: UserService
) {
    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null

        fun getInstance(): UserRepository {
            return INSTANCE ?: synchronized(this) {
                val service = ApiConfig.getService(UserService::class.java)
                UserRepository(service).also {
                    INSTANCE = it
                }
            }
        }
    }

    suspend fun getSelfSubmissions(): Response<List<Report>> {
        return try {
            val response = service.getSelfSubmission()

            Response.Success(response)
        } catch (e: HttpException) {
            val errorMessage = e.message()
            Response.Error(errorMessage)
        } catch (e: IOException) {
            Log.i("UserHomeViewModel", "getSelfSubmissions: $e")
            Response.NetworkError
        }
    }
}