package com.example.core.data.remote.response

import com.google.gson.annotations.SerializedName

open class GenericStatusResponse(
    @SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)


