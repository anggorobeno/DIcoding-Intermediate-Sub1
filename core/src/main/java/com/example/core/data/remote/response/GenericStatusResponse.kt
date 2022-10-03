package com.example.core.data.remote.response

import com.example.core.data.networkutils.Mappable
import com.example.domain.model.GenericStatusModel
import com.google.gson.annotations.SerializedName

open class GenericStatusResponse(
    @SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
): Mappable<GenericStatusResponse,GenericStatusModel> {
    companion object {
        fun Transform(dto: GenericStatusResponse): GenericStatusModel {
            return GenericStatusModel(
                error = dto.error!!,
                message = dto.message!!
            )
        }
    }

    override fun mapToModel(response: GenericStatusResponse): GenericStatusModel {
        return GenericStatusModel(
            error = response.error!!,
            message = response.message!!
        )
    }
}
