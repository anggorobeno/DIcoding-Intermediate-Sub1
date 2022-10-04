package com.example.domain.model.stories

data class StoriesModel(
    val data: ArrayList<StoriesModelItem>
) {
    data class StoriesModelItem(
        val id: String?,
        val name: String?,
        val description: String?,
        val photoUrl: String?,
        val createdAt: String?
    )
}
