package com.example.core.data.remote.response

import com.example.core.data.utils.Mappable
import com.example.domain.model.GenericStatusModel
import com.google.gson.annotations.SerializedName

open class GenericStatusResponse(
    @SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)


