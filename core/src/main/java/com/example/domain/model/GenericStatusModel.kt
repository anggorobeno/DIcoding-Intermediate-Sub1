package com.example.domain.model

import com.google.gson.annotations.SerializedName

data class GenericStatusModel(
    val error: Boolean? = null,

    val message: String? = null
)