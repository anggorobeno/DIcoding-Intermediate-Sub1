package com.example.submission1androidintermediate.helper

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.domain.model.stories.StoriesModel
import com.example.domain.model.stories.StoriesUploadModel
import com.example.domain.model.user.login.LoginModel
import com.example.domain.model.user.login.LoginRequest
import com.example.domain.model.user.register.RegisterModel
import com.example.domain.model.user.register.RegisterRequest

object TestHelper {
    // RegisterViewModel
    fun provideSuccessRegisterModel(): RegisterModel {
        return RegisterModel(
            false,
            "No Error",
        )
    }

    fun provideRegisterRequest() = RegisterRequest(
        "Anggoro",
        "anggoro@gmail.com",
        "testaja"
    )

    // LoginViewModel
    fun provideSuccessLoginModel() = LoginModel(
        false,
        "No Error",
        LoginModel.LoginItemModel(
            "asd123",
            "Anggoro",
            "123"
        )
    )

    fun provideLoginRequest() = LoginRequest("anggoro131098@gmail.com", "testaja")


    // HomeViewModel
    fun provideStoriesModelItem() = arrayListOf(
        StoriesModel.StoriesModelItem(
            "1", "Anggoro", "Ini test aja", "blabla", "kapankapan", "123z",
            "222d"
        )
    )

    fun provideSuccessStoriesModel() = StoriesModel(
        provideStoriesModelItem(), "No Error", false
    )

    fun provideUploadStoriesSuccessModel() = StoriesUploadModel(
        "No Error", false
    )


}

class NoopListCallback : ListUpdateCallback {
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
}

class MyDiffCallback : DiffUtil.ItemCallback<StoriesModel.StoriesModelItem>() {
    override fun areItemsTheSame(
        oldItem: StoriesModel.StoriesModelItem,
        newItem: StoriesModel.StoriesModelItem
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: StoriesModel.StoriesModelItem,
        newItem: StoriesModel.StoriesModelItem
    ): Boolean {
        return oldItem == newItem
    }
}