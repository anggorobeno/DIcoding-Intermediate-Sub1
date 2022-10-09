package com.example.domain.model.stories

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class StoriesModel(
    val data: ArrayList<StoriesModelItem>,
    val message: String?,
    val errror: Boolean?
) {
    @Parcelize
    data class StoriesModelItem(
        val id: String?,
        val name: String?,
        val description: String?,
        val photoUrl: String?,
        val createdAt: String?
    ) : Parcelable
}
