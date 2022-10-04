package com.example.core.data.remote.response.story

import com.example.core.data.remote.response.GenericStatusResponse
import com.google.gson.annotations.SerializedName

data class StoriesResponse(
    @field:SerializedName("listStory")
    val data: ArrayList<StoriesResponseItem>
): GenericStatusResponse() {
    data class StoriesResponseItem(
        val id: String?,
        val name: String?,
        val description: String?,
        val photoUrl: String?,
        val createdAt: String?
    )
}
