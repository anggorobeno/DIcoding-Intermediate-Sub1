package com.example.core.data.utils

import com.example.core.data.remote.response.GenericStatusResponse
import com.example.core.data.remote.response.story.StoriesResponse
import com.example.core.data.remote.response.story.StoriesUploadResponse
import com.example.core.data.remote.response.user.login.LoginResponse
import com.example.core.data.remote.response.user.register.RegisterResponse
import com.example.domain.model.GenericStatusModel
import com.example.domain.model.stories.StoriesModel
import com.example.domain.model.stories.StoriesUploadModel
import com.example.domain.model.user.login.LoginModel
import com.example.domain.model.user.register.RegisterModel

object Mapper {
    fun GenericStatusResponse.toModel(): GenericStatusModel {
        return GenericStatusModel(
            this.error,
            this.message
        )
    }

    fun RegisterResponse.toModel(): RegisterModel {
        return RegisterModel(
            this.error,
            this.message
        )
    }

    fun LoginResponse.LoginItem.toModel(): LoginModel.LoginItemModel {
        return LoginModel.LoginItemModel(
            this.userId,
            this.name,
            this.token
        )
    }

    fun LoginResponse.toModel(): LoginModel {
        return LoginModel(
            this.error,
            this.message,
            this.data?.toModel()
        )
    }

    fun StoriesResponse.StoriesResponseItem.toModel(): StoriesModel.StoriesModelItem {
        return StoriesModel.StoriesModelItem(
            this.id,
            this.name,
            this.description,
            this.photoUrl,
            this.createdAt,
        )
    }

    fun StoriesResponse.toModel(): StoriesModel {
        val arrayListModel = arrayListOf<StoriesModel.StoriesModelItem>()
        this.data.forEach {
            arrayListModel.add(it.toModel())
        }
        return StoriesModel(
            arrayListModel,
            this.message,
            this.error
        )
    }

    fun StoriesUploadResponse.toModel(): StoriesUploadModel {
        return StoriesUploadModel(
            this.message,
            this.error
        )
    }
}