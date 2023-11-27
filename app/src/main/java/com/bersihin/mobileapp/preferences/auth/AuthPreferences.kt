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
    }

    private val AUTH_TOKEN = stringPreferencesKey("auth_token")
    private val USER_ROLE = stringPreferencesKey("user_role")
    private val FIRST_NAME = stringPreferencesKey("first_name")
    private val LAST_NAME = stringPreferencesKey("last_name")
    private val EMAIL = stringPreferencesKey("email")

    fun getAuthToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[AUTH_TOKEN]
        }
    }

    fun getUserRole(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[USER_ROLE]
        }
    }

    fun getFirstName(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[FIRST_NAME]
        }
    }

    fun getLastName(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[LAST_NAME]
        }
    }

    fun getEmail(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[EMAIL]
        }
    }

    suspend fun saveAuthInfo(
        authToken: String?,
        userRole: String?,
        firstName: String?,
        lastName: String?,
        email: String?
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