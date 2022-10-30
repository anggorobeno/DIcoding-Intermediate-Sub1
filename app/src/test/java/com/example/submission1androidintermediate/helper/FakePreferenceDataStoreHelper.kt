package com.example.submission1androidintermediate.helper

import com.example.core.data.local.IDataStore

class FakePreferenceDataStoreHelper : IDataStore {
    override suspend fun getUserToken(): String {
        return USER_TOKEN
    }

    override suspend fun saveUserToken(value: String) {
        USER_TOKEN = value
    }

    override suspend fun getLoginStatus(): Boolean {
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