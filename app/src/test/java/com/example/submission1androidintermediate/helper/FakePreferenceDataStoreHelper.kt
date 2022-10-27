package com.example.submission1androidintermediate.helper

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.example.core.data.local.IDataStore
import com.example.core.data.local.PreferencesDataStoreHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.IOException

class FakePreferenceDataStoreHelper() : IDataStore {
    override suspend fun getUserToken(): String? {
        return USER_TOKEN
    }

    override suspend fun saveUserToken(value: String) {
        USER_TOKEN = value
    }

    override suspend fun getLoginStatus(): Boolean? {
        return LOGIN_STATUS
    }

    override suspend fun setLoginStatus(value: Boolean) {
        LOGIN_STATUS = value
    }



    companion object {
        var USER_TOKEN = "token"
        var LOGIN_STATUS = true
    }
}