package com.bersihin.mobileapp.preferences.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_preferences")

class AuthPreferences private constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        @Volatile
        private var INSTANCE: AuthPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): AuthPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = AuthPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

    private val AUTH_TOKEN = stringPreferencesKey("auth_token")
    private val IS_WORKER = booleanPreferencesKey("is_worker")

    fun getAuthToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[AUTH_TOKEN]
        }
    }

    fun getIsWorker(): Flow<Boolean?> {
        return dataStore.data.map { preferences ->
            preferences[IS_WORKER]
        }
    }

    suspend fun saveAuthInfo(authToken: String, isWorker: Boolean) {
        dataStore.edit { preferences ->
            preferences[AUTH_TOKEN] = authToken
            preferences[IS_WORKER] = isWorker
        }
    }

    suspend fun clearAuthInfo() {
        dataStore.edit { preferences ->
            preferences[AUTH_TOKEN] = ""
            preferences[IS_WORKER] = false
        }
    }
}