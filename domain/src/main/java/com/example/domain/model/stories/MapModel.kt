package com.example.domain.model.stories

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MapModel(
    var name: String? = null,
    var lat: Double? = null,
    var lon: Double? = null
) : Parcelable