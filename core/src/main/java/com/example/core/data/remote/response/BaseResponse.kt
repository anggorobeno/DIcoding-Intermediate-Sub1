package com.example.core.data.remote.response

import com.google.gson.annotations.SerializedName

open class BaseResponse {
    @SerializedName("error")
    val error: Boolean? = null

    @field:SerializedName("message")
    val message: String? = null
}