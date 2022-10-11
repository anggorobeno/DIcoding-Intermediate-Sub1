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
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("PreferencesDataStore")

class PreferencesDataStore @Inject constructor(val context: Context) {
    companion object {
        val USER_TOKEN: Preferences.Key<String> = stringPreferencesKey("user_token")
        val LOGIN_STATUS: Preferences.Key<Boolean> = booleanPreferencesKey("login_status")
    }

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

    suspend fun getUserToken() = USER_TOKEN.getValue()
    suspend fun saveUserToken(value: String) = USER_TOKEN.setValue(value)

    suspend fun getLoginStatus() = LOGIN_STATUS.getValue()
    suspend fun setLoginStatus(value: Boolean) = LOGIN_STATUS.setValue(value)

    suspend fun clear(){
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
}