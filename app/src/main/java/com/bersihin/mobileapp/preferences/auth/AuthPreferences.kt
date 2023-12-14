package com.bersihin.mobileapp.preferences.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
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

        val AUTH_TOKEN = stringPreferencesKey("auth_token")
        val USER_ROLE = stringPreferencesKey("user_role")
        val FIRST_NAME = stringPreferencesKey("first_name")
        val LAST_NAME = stringPreferencesKey("last_name")
        val EMAIL = stringPreferencesKey("email")
        val USER_ID = stringPreferencesKey("user_id")
    }

    fun getPrefValue(key: Preferences.Key<String>): Flow<String> {
        return dataStore.data.map { preferences ->
            if (preferences.contains(key)) {
                preferences[key] ?: ""
            } else {
                ""
            }
        }
    }

    fun getAuthToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            if (preferences.contains(AUTH_TOKEN)) {
                preferences[AUTH_TOKEN]
            } else {
                ""
            }
        }
    }

    suspend fun saveAuthInfo(
        authToken: String?,
        userRole: String?,
        firstName: String?,
        lastName: String?,
        email: String?,
        userId: String?
    ) {

        if (authToken != null) {
            dataStore.edit { preferences ->
                preferences[AUTH_TOKEN] = authToken
            }
        }

        if (userRole != null) {
            dataStore.edit { preferences ->
                preferences[USER_ROLE] = userRole
            }
        }

        if (firstName != null) {
            dataStore.edit { preferences ->
                preferences[FIRST_NAME] = firstName
            }
        }

        if (lastName != null) {
            dataStore.edit { preferences ->
                preferences[LAST_NAME] = lastName
            }
        }

        if (email != null) {
            dataStore.edit { preferences ->
                preferences[EMAIL] = email
            }
        }

        if (userId != null) {
            dataStore.edit { preferences ->
                preferences[USER_ID] = userId
            }
        }
    }

    suspend fun clearAuthInfo() {
        dataStore.edit { preferences ->
            preferences[AUTH_TOKEN] = ""
            preferences[USER_ROLE] = ""
            preferences[FIRST_NAME] = ""
            preferences[LAST_NAME] = ""
            preferences[EMAIL] = ""
        }
    }
}