package com.example.core.data.remote.response

import com.example.domain.model.GenericStatusModel
import com.google.gson.annotations.SerializedName

data class GenericStatusResponse(
    @SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
) {
    companion object {
        fun Transform(dto: GenericStatusResponse): GenericStatusModel {
            return GenericStatusModel(
                error = dto.error,
                message = dto.message
            )
        }
    }
}
