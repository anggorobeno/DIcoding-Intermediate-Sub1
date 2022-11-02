package com.example.core.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("PreferencesDataStore")

class PreferencesDataStoreHelper (val context: Context) : IDataStore {
    // Helper extension function
    private suspend fun <T> Preferences.Key<T>.setValue(value: T) {
        context.dataStore.edit { preferences -> preferences[this] = value }
    }

    private fun Flow<Preferences>.catchAndHandleError(): Flow<Preferences> {
        this.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        return this@catchAndHandleError
    }

    override suspend fun getUserToken() = USER_TOKEN.getValue()
    override suspend fun saveUserToken(value: String) = USER_TOKEN.setValue(value)

    override suspend fun getLoginStatus() = LOGIN_STATUS.getValue()
    override suspend fun setLoginStatus(value: Boolean) = LOGIN_STATUS.setValue(value)

    suspend fun clear() {
        context.dataStore.edit {
            it.clear()
        }
    }

    private suspend fun <T> Preferences.Key<T>.getValue(): T? {
        return context.dataStore.data
            .catchAndHandleError()
            .map { preferences -> preferences[this] }
            .firstOrNull()
    }

    private suspend fun <T> Preferences.Key<T>.getValue(defaultValue: T): T {
        return context.dataStore.data
            .catchAndHandleError()
            .map { preferences -> preferences[this] }
            .firstOrNull() ?: defaultValue
    }
    companion object {
        val USER_TOKEN: Preferences.Key<String> = stringPreferencesKey("user_token")
        val LOGIN_STATUS: Preferences.Key<Boolean> = booleanPreferencesKey("login_status")
    }
}

interface IDataStore {
    suspend fun getUserToken(): String?
    suspend fun saveUserToken(value: String)
    suspend fun getLoginStatus(): Boolean?
    suspend fun setLoginStatus(value: Boolean)
}
